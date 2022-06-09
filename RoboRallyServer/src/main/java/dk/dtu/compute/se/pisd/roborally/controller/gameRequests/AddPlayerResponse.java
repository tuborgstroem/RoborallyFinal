package dk.dtu.compute.se.pisd.roborally.controller.gameRequests;

import dk.dtu.compute.se.pisd.roborally.model.Player;

public class AddPlayerResponse {
    Player player;

    public AddPlayerResponse(boolean allPlayersIn, Player player){
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
