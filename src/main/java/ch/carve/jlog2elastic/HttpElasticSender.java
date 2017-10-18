package ch.carve.jlog2elastic;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.List;

import javax.json.Json;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpElasticSender {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient client;
    private String authToken;

    public HttpElasticSender(String username, String password) {
        client = new OkHttpClient();
        authToken = Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
    }

    public void send(List<String> messages) {
        StringBuilder builder = new StringBuilder();
        for (String message : messages) {
            String header = Json.createObjectBuilder()
                    .add("index", Json.createObjectBuilder()
                            .add("_index", "log")
                            .add("_type", "log")
                            .build())
                    .build().toString();
            String content = createJsonFromMessage(message);
            builder.append(header);
            builder.append(System.lineSeparator());
            builder.append(content);
            builder.append(System.lineSeparator());
        }
        Request request = new Request.Builder()
                .url("http://localhost:9200/_bulk")
                .header("Authorization", "Basic " + authToken)
                .post(RequestBody.create(JSON, builder.toString()))
                .build();
        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String message) {
        String json = createJsonFromMessage(message);
        Request request = new Request.Builder()
                .url("http://localhost:9200/log/log")
                .header("Authorization", "Basic " + authToken)
                .post(RequestBody.create(JSON, json))
                .build();
        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String createJsonFromMessage(String message) {
        return Json.createObjectBuilder()
                .add("@datetime", OffsetDateTime.now().toString())
                .add("application", "test")
                .add("logmessage", message)
                .build().toString();
    }

}
