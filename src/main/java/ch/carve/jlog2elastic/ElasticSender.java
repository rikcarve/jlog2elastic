package ch.carve.jlog2elastic;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.net.InetAddress;
import java.time.OffsetDateTime;
import java.util.List;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

public class ElasticSender {

    public static void send(List<String> messages) {
        try (TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300))) {
            BulkRequestBuilder bulkRequest = client.prepareBulk();
            for (String message : messages) {
                IndexRequestBuilder request = client.prepareIndex("log", "log")
                        .setSource(jsonBuilder()
                                .startObject()
                                .field("@datetime", OffsetDateTime.now().toString())
                                .field("application", "test")
                                .field("logmessage", message)
                                .endObject(), XContentType.JSON);
                bulkRequest.add(request);
            }
            BulkResponse response = bulkRequest.get();
            System.out.println(response.buildFailureMessage());
            System.out.println(response.status());
            if (response.hasFailures()) {
                response.forEach(bir -> System.out.println(bir.getFailureMessage()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void send(String message) {
        try (TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300))) {
            client.prepareIndex("log", "log")
                    .setSource(jsonBuilder()
                            .startObject()
                            .field("@datetime", OffsetDateTime.now().toString())
                            .field("application", "test")
                            .field("logmessage", message)
                            .endObject())
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
