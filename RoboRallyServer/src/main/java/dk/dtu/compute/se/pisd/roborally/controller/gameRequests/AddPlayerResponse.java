package dk.dtu.compute.se.pisd.roborally.controller.gameRequests;

import dk.dtu.compute.se.pisd.roborally.model.Player;

public class AddPlayerResponse {
    boolean allPlayersIn;
    Player player;

    public AddPlayerResponse(boolean allPlayersIn, Player player){
        this.player = player;
        this.allPlayersIn = allPlayersIn;
    }

}
