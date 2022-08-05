package dk.dtu.compute.se.pisd.roborally.model.gameRequests;

import dk.dtu.compute.se.pisd.roborally.model.Player;

public class UpdatePlayerRequest {
    private final String id;
    private final Player player;

    public UpdatePlayerRequest() {
        id = null;
        player = null;
    }

    public Player getPlayer() {
        return player;
    }

    public String getId() {
        return id;
    }
}
