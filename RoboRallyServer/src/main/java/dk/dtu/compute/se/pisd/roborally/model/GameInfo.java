package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;

import java.util.ArrayList;
import java.util.List;

public class GameInfo {

    private final String id;
    private final int maxPlayers;
    private int turn;
    private List<Player> players;

    public GameInfo(GameController gameController){
        this.id = gameController.gameId;
        this.maxPlayers = gameController.getNumberOfPlayers();
        this.turn = 0;
        players = new ArrayList<>(maxPlayers);
        if(gameController.board.getPlayers().size() > 0){
            players.addAll(gameController.board.getPlayers());
        }
    }

    public void addPlayer(Player player){
        players.add(player);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public boolean isFull(){
        return players.size() >= maxPlayers;
    }

    public void nextPhase(){
        turn++;
        players = new ArrayList<>(maxPlayers);
    }

    public String getId() {
        return id;
    }

    public int getTurn() {
        return turn;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

}
