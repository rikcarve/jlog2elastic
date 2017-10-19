package ch.carve.jlog2elastic;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

public class HttpElasticSenderTest {

    @Test
    public void testSendBulk() throws IOException {
        LogMessage m1 = new LogMessage();
        m1.setApplication("test");
        m1.setDatetime(OffsetDateTime.now());
        m1.setLogMessage("Test1");
        LogMessage m2 = new LogMessage();
        m2.setApplication("test");
        m2.setDatetime(OffsetDateTime.now());
        m2.setLogMessage("Test2");
        HttpElasticSender sender = new HttpElasticSender("http://localhost:9200", "user", "password", "log");
        sender.send(Arrays.asList(m1, m2));
    }

}
