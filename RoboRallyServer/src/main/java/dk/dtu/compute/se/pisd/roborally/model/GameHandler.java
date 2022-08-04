package dk.dtu.compute.se.pisd.roborally.model;

import com.google.gson.Gson;
import dk.dtu.compute.se.pisd.roborally.Exceptions.NotFoundException;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.fileaccess.FileHandler;
import dk.dtu.compute.se.pisd.roborally.model.gameRequests.GameResponse;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static dk.dtu.compute.se.pisd.roborally.fileaccess.FileHandler.*;

public class GameHandler {



    ArrayList<GameResponse> ongoingGameResponses;
    ArrayList<GameResponse> savedGameResponses;
    private FileHandler fileHandler;


    public GameHandler(FileHandler fileHandler) {

        this.fileHandler = fileHandler;
        ongoingGameResponses = new ArrayList<>();
        savedGameResponses = new ArrayList<>();
        List<String> ongoingGames= fileHandler.getGamesList(true);
        if(!ongoingGames.isEmpty()){
            getGameResponses(ongoingGames, true);
        }
        List<String> savedGames = fileHandler.getGamesList(false);
        if(!savedGames.isEmpty()){
            getGameResponses(savedGames, false);
        }
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
        GameResponse gameResponse = new GameResponse(gameController.board.boardName,
                gameController.getHostname(), gameController.board.getPlayersNumber(),
                gameController.getNumberOfPlayers(), gameController.gameId);
        list.add(gameResponse);
    }

    public ArrayList<GameResponse> getOngoingGames() {
        return ongoingGameResponses;
    }

    public ArrayList<GameResponse> getSavedGames() {
        return savedGameResponses;
    }

    public boolean stopGame(String id) throws NotFoundException {
        String path = id + ".json";

        for (GameResponse game: ongoingGameResponses){
            if(game.getId().equals(id)){
                File f = new File(ONGOING_GAMES + path);
                return f.delete();
            }
        }
        throw new NotFoundException();
    }

    public boolean createInfo(GameController game, String id)  {
        GameInfo info = new GameInfo(game);
        return fileHandler.createInfo(info, id);
    }

    public boolean addPlayer(String id, Player player){
        GameInfo info = fileHandler.getInfo(id);
        info.addPlayer(player);
        return fileHandler.updateInfo(info, id);
    }

}
