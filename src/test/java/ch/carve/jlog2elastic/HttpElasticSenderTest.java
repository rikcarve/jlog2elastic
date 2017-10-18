package ch.carve.jlog2elastic;

import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class HttpElasticSenderTest {

    @Test
    @Disabled
    public void testSend() throws IOException {
        HttpElasticSender sender = new HttpElasticSender("user", "password");
        sender.send("Test");
    }

    @Test
    public void testSendBulk() throws IOException {
        HttpElasticSender sender = new HttpElasticSender("user", "password");
        sender.send(Arrays.asList("Test", "Test2"));
    }

}
