package dk.dtu.compute.se.pisd.controller.Requests;

import dk.dtu.compute.se.pisd.model.Player;

public class AddPlayerResponse {
    boolean allPlayersIn;
    Player player;

    public AddPlayerResponse(boolean allPlayersIn, Player player){
        this.player = player;
        this.allPlayersIn = allPlayersIn;
    }

    public Player getPlayer() {
        return player;
    }
}
