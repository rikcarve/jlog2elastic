package ch.carve.jlog2elastic;

import java.io.IOException;
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

    public HttpElasticSender(String url, String username, String password, String index) {
        this.url = url;
        this.index = index;
        client = new OkHttpClient();
        authToken = Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
    }

    public boolean send(List<LogMessage> messages) {
        StringBuilder builder = new StringBuilder();
        for (LogMessage message : messages) {
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
        return sendHttpRequest(request);
    }

    private boolean sendHttpRequest(Request request) {
        try {
            return client.newCall(request).execute().isSuccessful();
        } catch (IOException e) {
            logger.error("Bulk request exception", e);
        }
        return false;
    }

    private String createJsonFromMessage(LogMessage message) {
        return Json.createObjectBuilder()
                .add("@datetime", message.getDatetime().toString())
                .add("application", message.getApplication())
                .add("logmessage", message.getLogMessage())
                .build().toString();
    }

}
