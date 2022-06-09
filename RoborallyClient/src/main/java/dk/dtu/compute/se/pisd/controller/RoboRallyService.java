package dk.dtu.compute.se.pisd.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dk.dtu.compute.se.pisd.controller.Adapters.CommandInterfaceAdapter;
import dk.dtu.compute.se.pisd.controller.Adapters.FieldActionAdapter;
import dk.dtu.compute.se.pisd.controller.Requests.NewGameRequest;
import dk.dtu.compute.se.pisd.model.fieldActions.FieldAction;
import dk.dtu.compute.se.pisd.model.programming.ICommand;

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
    public RoboRallyService(){
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter( FieldAction.class, new FieldActionAdapter());
        builder.registerTypeAdapter(ICommand.class, new CommandInterfaceAdapter());
        gson = builder.create();

    }

    private final String BASE_URL = "http://localhost:8080";
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public List getBoards()  {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BASE_URL + "/boards"))
                .build();

        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        try {
            String result = response.thenApply((r) -> r.body()).get(5, TimeUnit.SECONDS);
            return gson.fromJson(result, List.class);

        } catch (ExecutionException | InterruptedException | TimeoutException  e) {
            e.printStackTrace();
            System.err.println("Boards has not been retrieved" + e.getMessage());
        }
        return null;
    }

    public GameController newGame(String boardName, int numberOFPlayers){
        NewGameRequest newGameRequest = new NewGameRequest();
        newGameRequest.boardname = boardName;
        newGameRequest.playerNumber = numberOFPlayers;
        String req = gson.toJson(newGameRequest);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/newgame"))
                .header("Content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(req))
                .build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        try {
            String result = response.thenApply((r) -> r.body()).get(5, TimeUnit.SECONDS);
            GameController gameController = gson.fromJson(result, GameController.class);
            return gameController;


        } catch (ExecutionException | InterruptedException | TimeoutException  e) {
            e.printStackTrace();
            System.err.println("Boards has not been retrieved" + e.getMessage());
        }
        return null;
    }

    public GameController gameReady(String id){
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BASE_URL + "/gameready" + id))
                .build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        try {
            String result = response.thenApply((r) -> r.body()).get(5, TimeUnit.SECONDS);
            GameController gameController = gson.fromJson(result, GameController.class);
            return gameController;

        }catch (ExecutionException | InterruptedException | TimeoutException  e) {
            e.printStackTrace();
            System.err.println("game has not ready" + e.getMessage());
        }
        return null;
    }
}

