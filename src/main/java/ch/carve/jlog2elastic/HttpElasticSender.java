package ch.carve.jlog2elastic;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.time.OffsetDateTime;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpElasticSender {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static void send(String message) throws IOException {
        OkHttpClient client = new OkHttpClient();

        String json = jsonBuilder()
                .startObject()
                .field("@datetime", OffsetDateTime.now().toString())
                .field("application", "test")
                .field("logmessage", message)
                .endObject().string();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url("http://localhost:9200/log/log")
                .post(body)
                .build();
        System.out.println(client.newCall(request).execute());
    }

}
