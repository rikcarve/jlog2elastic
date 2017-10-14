package ch.carve.jlog2elastic;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.InetAddress;
import java.time.LocalDateTime;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

public class ElasticSender {

    public static void main(String[] args) {
        int start = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/test.log"));
            String message = reader.readLine();
            start += (message.getBytes().length + 2);
            reader.close();
            System.out.println(message);
            new ElasticSender().send(message);
            reader = new BufferedReader(new FileReader("src/main/resources/test.log"));
            reader.skip(start);
            message = reader.readLine();
            System.out.println(message);
          new ElasticSender().send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(String message) throws Exception {
        try (TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300))) {
            client.prepareIndex("log", "log")
                    .setSource(jsonBuilder()
                            .startObject()
                            .field("@datetime", LocalDateTime.now().toString())
                            .field("application", "test")
                            .field("logmessage", message)
                            .endObject())
                    .get();
        }
    }
    
}
