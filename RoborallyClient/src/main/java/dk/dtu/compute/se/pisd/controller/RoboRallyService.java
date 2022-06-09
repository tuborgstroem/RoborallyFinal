package dk.dtu.compute.se.pisd.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dk.dtu.compute.se.pisd.controller.Adapters.CommandInterfaceAdapter;
import dk.dtu.compute.se.pisd.controller.Adapters.FieldActionAdapter;
import dk.dtu.compute.se.pisd.controller.Requests.AddPlayerRequest;
import dk.dtu.compute.se.pisd.controller.Requests.AddPlayerResponse;
import dk.dtu.compute.se.pisd.controller.Requests.NewGameRequest;
import dk.dtu.compute.se.pisd.model.Player;
import dk.dtu.compute.se.pisd.model.fieldActions.FieldAction;
import dk.dtu.compute.se.pisd.model.programming.ICommand;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RoboRallyService {

    Gson gson;

    public RoboRallyService() {
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

    public List getBoards() {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BASE_URL + "/boards"))
                .build();

        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        try {
            String result = response.thenApply((r) -> r.body()).get(5, TimeUnit.SECONDS);
            return gson.fromJson(result, List.class);

        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
            System.err.println("Boards has not been retrieved" + e.getMessage());
        }
        return null;
    }

    public GameController newGame(String boardName, int numberOFPlayers, String hostName) {
        NewGameRequest newGameRequest = new NewGameRequest();
        newGameRequest.boardname = boardName;
        newGameRequest.playerNumber = numberOFPlayers;
        newGameRequest.hostName = hostName;
        String req = gson.toJson(newGameRequest);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/newgame"))
                .header("Content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(req))
                .build();
        return getGameController(request);
    }

    public boolean gameReady(String id) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BASE_URL + "/gameready/" + id))
                .build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        try {
            String result = response.thenApply((r) -> r.body()).get(5, TimeUnit.SECONDS);
            return gson.fromJson(result, Boolean.class);

        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
            System.err.println("game has not ready" + e.getMessage());
            return false;
        }
    }

    public Player addPlayer(String id, String playerName) {
        AddPlayerRequest addPlayerRequest = new AddPlayerRequest();
        addPlayerRequest.name = playerName;
        String req = gson.toJson(addPlayerRequest, AddPlayerRequest.class);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/addplayer/" + id))
                .header("Content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(req))
                .build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        try {
            String result = response.thenApply((r) -> r.body()).get(5, TimeUnit.SECONDS);
            AddPlayerResponse playerResponse = gson.fromJson(result, AddPlayerResponse.class);

            return playerResponse.getPlayer();
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
            System.err.println("game has not ready" + e.getMessage());
            return null;
        }
    }

    public GameController getGame(String id) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/getgame/" + id))
                .header("Content-type", "application/json")
                .GET()
                .build();
        return getGameController(request);
    }

    private GameController getGameController(HttpRequest request) {
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        try {
            String result = response.thenApply((r) -> r.body()).get(5, TimeUnit.SECONDS);
            GameController gameController = gson.fromJson(result, GameController.class);
            return gameController;


        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
            System.err.println("Boards has not been retrieved" + e.getMessage());
        }
        return null;
    }

    public void getOngoingGames() {
    }

    public void stopGame(String id) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/stopgame/" + id))
                .DELETE().build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        try {
            String result = response.thenApply((r) -> r.body()).get(5, TimeUnit.SECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            System.err.println("something went wrong game not stopped" + e.getMessage());
            e.printStackTrace();
        }
    }
}


