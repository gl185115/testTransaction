package ncr.res.mobilepos.helper.test;

import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.helper.TodHelper;
import org.jbehave.scenario.annotations.*;
import org.jbehave.scenario.steps.Steps;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TodHelperSteps extends Steps {
    static Charset cp932 = Charset.forName("MS932");
    static final String BAT_NAME = "todhelpertest.bat";
    static final String JOBLOG = "joblog";

    @BeforeScenario
    public void setUp() throws Exception {
        new File(System.getenv("LOG"), JOBLOG).delete();
        String[] bat = {"@echo off", "echo %1", "echo %2"};
        Files.write(Paths.get(System.getProperty("java.io.tmpdir"), BAT_NAME), Arrays.asList(bat), cp932);
    }

    @AfterScenario
    public void tearDown() throws Exception {
        new File(System.getProperty("java.io.tmpdir"), BAT_NAME).delete();
        new File(System.getenv("LOG"), JOBLOG).delete();
    }

    @Given("url {$uri}")
    public void setup(String uri) throws Exception {
        GlobalConstant.setTodUri(uri);
    }

    @When("asked tod to {$port} and got {$tod}")
    public void pattern(int port, String tod) throws Exception {
        String data = "{\"DataTimestamp\":\"" + tod + "\",\"BizDateData\": [ {\"BizDate\":\"2015-08-12T00:00:00\"}]}";
        Thread d = dummyServer(port, data);
        d.start();
        TodHelper helper = new TodHelper();
        helper.setBatchFile(new File(System.getProperty("java.io.tmpdir"), BAT_NAME));
        helper.adjust();
        d.join(30000);
        if (d.getState() != Thread.State.TERMINATED) {
            d.interrupt();
        }
        
    }

    @Then("param should be {$date} and {$time}")
    public void result(String date, String time) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(System.getenv("LOG"), JOBLOG), cp932);
        assertThat(date, is(equalTo(lines.get(0))));
        assertThat(time, is(equalTo(lines.get(1))));
    }

    Thread dummyServer(final int port, final String json) throws Exception {
        Thread t = new Thread(() -> {
            try(ServerSocket svr = new ServerSocket(port)) {
                try (Socket sock = svr.accept();
                     InputStream is = sock.getInputStream()) {
                    byte[] buff = new byte[4000];
                    for (int offset = 0;;) {
                        int len = is.read(buff, offset, buff.length - offset);
                        if (len < 0) break;
                        offset += len;
                        if (new String(buff, 0, offset).indexOf("\r\n\r\n") >= 0) break;
                    }
                    OutputStream os = sock.getOutputStream();
                    os.write("HTTP/1.1 200 OK\r\nContent-Type: application/json;charset=utf-8\r\nConnection: close\r\n\r\n".getBytes());
                    os.write(json.getBytes());
                    os.flush();
                    os.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return t;
    }
}
