package ch.carve.jlog2elastic;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.List;

import javax.json.Json;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpElasticSender {
    private static final Logger logger = LoggerFactory.getLogger(HttpElasticSender.class);

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient client;
    private String authToken;
    private String index;
    private String url;
    private String application;

    public HttpElasticSender(String url, String username, String password, String index, String application) {
        this.url = url;
        this.index = index;
        this.application = application;
        client = new OkHttpClient();
        authToken = Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
    }

    public void send(List<String> messages) {
        StringBuilder builder = new StringBuilder();
        for (String message : messages) {
            String header = Json.createObjectBuilder()
                    .add("index", Json.createObjectBuilder()
                            .add("_index", index)
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
                .url(url + "/_bulk")
                .header("Authorization", "Basic " + authToken)
                .post(RequestBody.create(JSON, builder.toString()))
                .build();
        sendHttpRequest(request);
    }

    private void sendHttpRequest(Request request) {
        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            logger.error("Bulk request exception", e);
        }
    }

    private String createJsonFromMessage(String message) {
        return Json.createObjectBuilder()
                .add("@datetime", OffsetDateTime.now().toString())
                .add("application", application)
                .add("logmessage", message)
                .build().toString();
    }

}
