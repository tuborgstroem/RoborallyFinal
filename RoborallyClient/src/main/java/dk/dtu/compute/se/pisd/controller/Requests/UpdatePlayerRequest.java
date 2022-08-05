package dk.dtu.compute.se.pisd.controller.Requests;

import dk.dtu.compute.se.pisd.model.Player;
import dk.dtu.compute.se.pisd.model.dataModels.PlayerData;

public class UpdatePlayerRequest {

    private final String id;
    private final PlayerData player;
    public UpdatePlayerRequest(String id, Player player) {
        this.id = id;
        this.player = new PlayerData(player);
    }
}
