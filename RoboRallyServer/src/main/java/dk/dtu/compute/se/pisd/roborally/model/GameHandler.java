package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.fileaccess.FileHandler;
import dk.dtu.compute.se.pisd.roborally.model.gameRequests.GameResponse;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static dk.dtu.compute.se.pisd.roborally.fileaccess.FileHandler.ONGOING_GAMES;
import static dk.dtu.compute.se.pisd.roborally.fileaccess.FileHandler.SAVED_GAMES;

public class GameHandler {

    ArrayList<GameResponse> ongoingGameResponses;
    ArrayList<GameResponse> savedGameResponses;


    public GameHandler() {

        ongoingGameResponses = new ArrayList<>();
        savedGameResponses = new ArrayList<>();
        List<String> ongoingGames= getGames(true);
        if(!ongoingGames.isEmpty()){
            getGameResponses(ongoingGames, true);
        }
        List<String> savedGames = getGames(false);
        if(!savedGames.isEmpty()){
            getGameResponses(savedGames, false);
        }
    }

    public List<String> getGames(boolean ongoingGames) {
        File f;
        if(ongoingGames){
           f = new File(ONGOING_GAMES);
        }
        else {
            f = new File(SAVED_GAMES);
        }
        String[] strings = f.list();
        return Arrays.asList(strings);
    }

    public void getGameResponses(List<String> gameIds, boolean ongoingGames) {
        FileHandler fileHandler = new FileHandler();
        for(String game : gameIds){
            GameController gameController = fileHandler.getGame(game, ongoingGames);
            addToList(gameController, ongoingGames);
        }

    }

    public void addToList(GameController gameController, boolean ongoingGame){
        List list;
        if (ongoingGame){
            list = ongoingGameResponses;
        }
        else {
            list = savedGameResponses;
        }
        GameResponse gameResponse = new GameResponse(gameController.board.boardName, gameController.getHostname(), gameController.board.getPlayersNumber(), gameController.getNumberOfPlayers(), gameController.gameId);
        list.add(gameResponse);
    }
}
