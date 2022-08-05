package dk.dtu.compute.se.pisd.controller.Requests;

import dk.dtu.compute.se.pisd.model.dataModels.PlayerData;

import java.util.ArrayList;

public class PlayerLocationsResponse {

    private ArrayList<PlayerData> players;

    public PlayerLocationsResponse(ArrayList<PlayerData> players){
        this.players = players;
    }
    public ArrayList<PlayerData> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<PlayerData> players) {
        this.players = players;
    }
}
