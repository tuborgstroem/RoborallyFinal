package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class GameInfo {

    private final String id;
    private final int maxPlayers;
    private int turn;
    private List<Player> players;
    private List<Player> lastPhasePlayers;

    public GameInfo(GameController gameController){
        this.id = gameController.gameId;
        this.maxPlayers = gameController.getNumberOfPlayers();
        this.turn = 0;
        players = new ArrayList<>(maxPlayers);
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

    public void nextTurn(){
        turn++;
        lastPhasePlayers = players;
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

    public void updatePlayer(Player player) {

        Optional<Player> targetPlayer = players.stream().filter(Objects::nonNull).filter(
                player1 -> player1.getName().equals(player.getName())).findFirst();
        targetPlayer.ifPresent(value -> players.remove(value));
        players.add(player);
        System.out.println(players);
    }

    public List<Player> getLastPhasePlayers() {
        return lastPhasePlayers;
    }
}
