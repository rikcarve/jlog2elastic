package ch.carve.jlog2elastic;

import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

public class HttpElasticSenderTest {

    @Test
    public void testSendBulk() throws IOException {
        HttpElasticSender sender = new HttpElasticSender("http://localhost:9200", "user", "password", "log", "Test");
        sender.send(Arrays.asList("Test", "Test2"));
    }

}
