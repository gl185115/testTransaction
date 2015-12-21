package ncr.res.ue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.servlet.ServletContext;

import org.springframework.mock.web.MockServletContext;

public class TestServer {
    public static Boolean JenkinsBuild = true;
    private Thread testThread = null;
    private static ServerSocket myServer = null;
    public static Boolean stop = false;
    public static int socketToUse;
    public static String Item_Entry_Response = "002200001111000000013300\r\n";
    
    public final void runTestServer() throws Exception {

        if(TestServer.JenkinsBuild == false){
            return;
        }

        if (myServer != null) {
            myServer.close();
            myServer = null;
        }

        TestServer.stop = false;
        
        testThread = new Thread() {
            public void run() { 
                    System.out.println("@Before myThread run()");
                    ServerSocket myServer = null;
                    try {
                            myServer = new ServerSocket(socketToUse); 

                            System.out.println("@Before myThread run() - server socket created."); 
                            Socket testServerSocket = myServer.accept();

                            BufferedReader buff = new BufferedReader(new InputStreamReader(testServerSocket.getInputStream()));
                            PrintStream stream = new PrintStream(testServerSocket.getOutputStream(), true, "UTF-8");
                            
                            while(!TestServer.stop){
                                String strReceivedData = buff.readLine();
                                
                                if(strReceivedData.isEmpty()) continue;
                                
                                String messageType = strReceivedData.substring(20, 22);
                                String response = "";
                                switch (Integer.parseInt(messageType)){
                                    case 91:
                                        response = "004600000031000000009200Store000031      1.0  20\r\n";
                                        break;
                                    case 1:
                                        response = "002200001111000000013100\r\n";
                                        break;
                                    case 3:
                                        response = Item_Entry_Response;
                                        break;
                                    case 5:
                                        response = "004300001111000000013500000000000130000000025\r\n";
                                        break;
                                    case 6:
                                        response = "002200001111000000013600\r\n";
                                        break;    
                                    case 7:
                                        response = "002200001111000000013700\r\n";
                                        break;
                                    case 9:
                                        response = "002200001111000000013900\r\n";
                                    default :
                                        response = "unknown message type";
                                }
                                
                                stream.println(response);
                                System.out.println("@Before myThread run() - received data");
                            }
                            System.out.println("@Before myThread run() - exited");
                    } catch (IOException e) { 
                            e.printStackTrace(); 
                    } finally {
                    	if (myServer != null) {
                    		try {
                    			myServer.close();
                    		} catch (IOException e) {
                    			e.printStackTrace();
                    		}
                    	}
                    }
            }
        };
        testThread.start(); 
    }

    public static ServletContext getMockServletForUeParameters(int socket){
        MockServletContext servletContext = new MockServletContext("");
        socketToUse = socket;
        String port = Integer.toString(socket);
        if (TestServer.JenkinsBuild == false) {
            port = "1900";
        }
        servletContext.addInitParameter("UeIoServerAddress", "127.0.0.1");
        servletContext.addInitParameter("UeIoServerPort", port);
        servletContext.addInitParameter("UeIoServerAddress", "127.0.0.1");
        servletContext.addInitParameter("UeLocationCode", "Store000031");
        servletContext.addInitParameter("UeProtocolVersion", "1.0");
        servletContext.addInitParameter("UeProtocolBuild", "20");

        return servletContext;
    }
}
