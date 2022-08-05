package dk.dtu.compute.se.pisd.roborally.model.gameRequests;

import dk.dtu.compute.se.pisd.roborally.model.Player;

import java.util.ArrayList;

public class PlayerLocationsResponse {

    private ArrayList<Player> players;

    public PlayerLocationsResponse(ArrayList<Player> players){
        this.players = players;
    }
    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }
}
