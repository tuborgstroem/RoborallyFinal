package dk.dtu.compute.se.pisd.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dk.dtu.compute.se.pisd.controller.Adapters.CommandInterfaceAdapter;
import dk.dtu.compute.se.pisd.controller.Adapters.FieldActionAdapter;
import dk.dtu.compute.se.pisd.controller.Requests.PlayerLocationsResponse;
import dk.dtu.compute.se.pisd.controller.Requests.UpdatePlayerRequest;
import dk.dtu.compute.se.pisd.model.Player;
import dk.dtu.compute.se.pisd.model.dataModels.PlayerData;
import dk.dtu.compute.se.pisd.model.fieldActions.FieldAction;
import dk.dtu.compute.se.pisd.model.programming.ICommand;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class GameplayContactService {

    private Gson gson;

    public GameplayContactService() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(FieldAction.class, new FieldActionAdapter());
        builder.registerTypeAdapter(ICommand.class, new CommandInterfaceAdapter());
        gson = builder.create();

    }

    private final String BASE_URL = "http://localhost:8080";
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public boolean updatePlayer(String id, Player currentPlayer)  {
        UpdatePlayerRequest playerRequest = new UpdatePlayerRequest(id, currentPlayer);
        String json = gson.toJson(playerRequest, UpdatePlayerRequest.class);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-type", "application/json")
                .uri(URI.create(BASE_URL + "/updateplayer/" + id))
                .build();

        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        try {
            String result = response.thenApply((r) -> r.body()).get(5, TimeUnit.SECONDS);
            return gson.fromJson(result, boolean.class);

        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<PlayerData> getPlayerLocations(String id) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("Content-type", "application/json")
                .uri(URI.create(BASE_URL + "/playerlocations/" + id))
                .build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        try {
            String result = response.thenApply((r) -> r.body()).get(5, TimeUnit.SECONDS);
          return gson.fromJson(result, PlayerLocationsResponse.class).getPlayers();

        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
