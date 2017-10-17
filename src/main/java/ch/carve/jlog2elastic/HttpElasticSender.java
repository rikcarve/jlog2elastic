package ch.carve.jlog2elastic;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Base64;

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

    public void send(String message) {
        String json = Json.createObjectBuilder()
                .add("@datetime", OffsetDateTime.now().toString())
                .add("application", "test")
                .add("logmessage", message)
                .build().toString();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url("http://localhost:9200/log/log")
                .header("Authorization", "Basic " + authToken)
                .post(body)
                .build();
        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
