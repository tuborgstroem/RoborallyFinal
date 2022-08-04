package dk.dtu.compute.se.pisd.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dk.dtu.compute.se.pisd.controller.Adapters.CommandInterfaceAdapter;
import dk.dtu.compute.se.pisd.controller.Adapters.FieldActionAdapter;
import dk.dtu.compute.se.pisd.model.Player;
import dk.dtu.compute.se.pisd.model.fieldActions.FieldAction;
import dk.dtu.compute.se.pisd.model.programming.ICommand;

import java.net.http.HttpClient;
import java.time.Duration;

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

    public void updatePlayer(Player currentPlayer) {
    }
}
