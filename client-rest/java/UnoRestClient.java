package clientrest;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class UnoRestClient {
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String baseUrl;

    public UnoRestClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String createGame() throws IOException, InterruptedException {
        return send("POST", "/api/games", "");
    }

    public String getGame(String gameId) throws IOException, InterruptedException {
        return send("GET", "/api/games/" + gameId, null);
    }

    public String addPlayer(String gameId, String name) throws IOException, InterruptedException {
        return send("POST", "/api/games/" + gameId + "/players", "{\"name\":\"" + escape(name) + "\"}");
    }

    public String playCard(String gameId, String playerName, int cardIndex, String chosenColor)
            throws IOException, InterruptedException {
        String json = "{\"playerName\":\"" + escape(playerName) + "\","
                + "\"cardIndex\":" + cardIndex + ","
                + "\"chosenColor\":\"" + escape(chosenColor) + "\"}";
        return send("POST", "/api/games/" + gameId + "/play-card", json);
    }

    public String drawCard(String gameId, String playerName) throws IOException, InterruptedException {
        return send("POST", "/api/games/" + gameId + "/draw-card", playerActionJson(playerName));
    }

    public String callUno(String gameId, String playerName) throws IOException, InterruptedException {
        return send("POST", "/api/games/" + gameId + "/call-uno", playerActionJson(playerName));
    }

    private String send(String method, String path, String body) throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(baseUrl + path))
                .header("Content-Type", "application/json");

        if ("GET".equals(method)) {
            builder.GET();
        } else {
            builder.method(method, HttpRequest.BodyPublishers.ofString(body == null ? "" : body));
        }

        HttpResponse<String> response = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 400) {
            throw new IOException("REST call failed with " + response.statusCode() + ": " + response.body());
        }
        return response.body();
    }

    private String playerActionJson(String playerName) {
        return "{\"playerName\":\"" + escape(playerName) + "\"}";
    }

    private String escape(String value) {
        return value == null ? "" : value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}

