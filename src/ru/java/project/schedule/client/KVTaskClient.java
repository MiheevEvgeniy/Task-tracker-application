package ru.java.project.schedule.client;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ru.java.project.schedule.server.KVServer;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final String  uri;
    private final String api_token;
    private final HttpClient client  = HttpClient.newHttpClient();
    public KVTaskClient(String url) throws IOException, InterruptedException {
        uri = url;
        api_token = getApi_token();
    }
    public void put(String key, String json) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(URI.create(uri+"save/"+key+"?API_TOKEN="+api_token))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        sendRequest(request);
    }
    public String load(String key) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uri+"load/"+key+"?API_TOKEN="+api_token))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        return sendRequest(request).body();
    }
    private String getApi_token() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uri+"register"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        return sendRequest(request).body();
    }
    private HttpResponse<String> sendRequest(HttpRequest request) throws IOException, InterruptedException {
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString() ;

        return client.send(request, handler);
    }
}
