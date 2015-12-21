package ncr.res.mobilepos.devicelog;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

import javax.naming.InitialContext;
import javax.ws.rs.core.MediaType;

import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.springframework.mock.web.MockHttpServletRequest;

public class DeviceLogTestHelper {

    public static void loadCleanTable(){
        new DBInitiator("Device_Log_Clean",
                "test/ncr/res/mobilepos/devicelog/DEVICE_LOG_clean.xml", DATABASE.RESMaster);
    }
    
    public static void loadTableWithData(){
        new DBInitiator("Device_Log_With_Data",
                "test/ncr/res/mobilepos/devicelog/DEVICE_LOG_with_data.xml", DATABASE.RESMaster);
    }
    
    public static byte[] createLogFileByteArray(final String logFileName)
    throws IOException{
        if(null == logFileName || logFileName.isEmpty()
                || logFileName.equals("empty log")){
            return new byte[]{};
        } else {
            return IOUtils.toByteArray(
                    new FileInputStream(sampleLogFilePath() + logFileName));
        }
    }
    
    public static void createUploadFormRequest(
            final MockHttpServletRequest request, final String fileField,
            final byte[] aLogFile){
        String boundary         = "---------------------------12052273501150";
        String boundaryMark     = "--";
        String startboundary     = boundaryMark + boundary;
        String endBoundary         = boundaryMark + boundary + boundaryMark;
        String fileContentdisp     = "Content-Disposition: form-data; name=\""
            + fileField + "\"; filename=\"TestDeviceLog.log\"";
        String fileContentType    = "Content-Type: text/plain";
        String newLine             = new String(new byte[]{13,10});
        
        byte[] prefix =    (newLine + startboundary + newLine +
                        fileContentdisp + newLine +
                        fileContentType + newLine +    newLine).getBytes();
        
        byte[] suffix = (newLine + endBoundary + newLine).getBytes();
        
        byte[] forUpload = new byte[prefix.length + aLogFile.length
                                    + suffix.length];
        mergeBytes(forUpload, prefix, aLogFile, suffix);
        
        request.setContent(forUpload);
        request.setMethod("POST");
        request.setContentType(MediaType.MULTIPART_FORM_DATA
                + "; boundary=" + boundary);
    }
    
    public static void mergeBytes(final byte[] dest, final byte[] ... src){
        int p = 0;
        for (int i = 0; i < src.length; i++){
            System.arraycopy(src[i], 0, dest, p, src[i].length);
            p += src[i].length;
        }
    }
    
    public static FileItem createFileItem(final String logFileName)
    throws IOException, FileUploadException{
        
        byte [] logFile = null;
        
        if (null == logFileName){
            return null;
        } else if(logFileName.isEmpty() || logFileName.equals("empty")
                || logFileName.equals("empty log")){
            logFile = new byte[]{};
        } else {
            logFile = IOUtils.toByteArray(
                    new FileInputStream(sampleLogFilePath() + logFileName));
        }
        
        DiskFileItemFactory factory = new DiskFileItemFactory();
        MockHttpServletRequest f = new MockHttpServletRequest();
        createUploadFormRequest(f, "log_file", logFile);
        
        ServletFileUpload upload = new ServletFileUpload(factory);
        
        @SuppressWarnings("unchecked")
        List<FileItem> items = upload.parseRequest(f);
        Iterator<FileItem> iter = items.iterator();
        
        // get log_file field
        while(iter.hasNext()){
            FileItem item = iter.next();
            if (!item.isFormField()
                    && item.getFieldName().equalsIgnoreCase("log_file")){
                return item;
            }
        }
        
        return null;
    }
    
    public static OutputStream createOutpuStream(){
        return new OutputStream() {
            @Override
            public void write(final int b) throws IOException {
                // no-op                
            }
        };
    }
    
    public static String sampleLogFilePath() {
        return "test/ncr/res/mobilepos/devicelog/";
    }
    
    public static boolean deleteLogFiles(String fileName){
        try {
            javax.naming.Context env =
                    (javax.naming.Context) new InitialContext()
                    .lookup("java:comp/env");
            String path = (String) env.lookup("deviceLogPath");
            String target = path + "\\" + fileName;
            File file = new File(target);
            if (file.isFile() && file.exists()) {
                file.delete();
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}