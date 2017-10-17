package ch.carve.jlog2elastic;

import java.io.IOException;

import org.junit.jupiter.api.Test;

public class HttpElasticSenderTest {

    @Test
    public void testSend() throws IOException {
        HttpElasticSender sender = new HttpElasticSender("user", "password");
        sender.send("Test");
    }
}