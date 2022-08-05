package dk.dtu.compute.se.pisd.model.dataModels;

import dk.dtu.compute.se.pisd.controller.GameController;
import dk.dtu.compute.se.pisd.model.Board;

public class GameControllerData {
    private final int numberOfPlayers;
    private final String gameId;
    private final Board board;

    public GameControllerData(int numberOfPlayers, String gameId, Board board) {
        this.numberOfPlayers = numberOfPlayers;
        this.gameId = gameId;
        this.board = board;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public String getGameId() {
        return gameId;
    }

    public Board getBoard() {
        return board;
    }

    public GameController toGameController(){
        return new GameController(this);
    }
}
