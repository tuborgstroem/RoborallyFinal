package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.Exceptions.NotFoundException;
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

    FileHandler fileHandler;
    ArrayList<GameResponse> ongoingGameResponses;
    ArrayList<GameResponse> savedGameResponses;


    public GameHandler(FileHandler fileHandler) {

        this.fileHandler = fileHandler;
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
        List<GameResponse> list;
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

    public void updateList(boolean isOngoing){
        if (isOngoing){
            ongoingGameResponses = new ArrayList<>();
        }
        else {
            savedGameResponses = new ArrayList<>();
        }
        List<String> allIds = fileHandler.getGamesList(isOngoing);
        getGameResponses(allIds, isOngoing);
    }
    public String getOngoingGames() {

        updateList(true);
        return fileHandler.gameResponseToJson(ongoingGameResponses);
    }

    public String getSavedGames() {
        savedGameResponses = new ArrayList<>();
        updateList(false);
        return fileHandler.gameResponseToJson(savedGameResponses);
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

    public String createInfo(GameController game, String id)  {
        GameInfo info = new GameInfo(game);
        return fileHandler.createInfo(info, id);
    }

    public boolean updatePlayer(String id, Player player) {
        GameController gameController = fileHandler.getGame(id, true);
        GameInfo info = fileHandler.getInfo(id);
        info.updatePlayer(player);
        gameController.board.setPlayers(info.getPlayers());
        fileHandler.gameUpdated(gameController) ;
        if (info.isFull()){
            info.nextTurn();
            fileHandler.updateInfo(info, id);
            return true;
        }
        fileHandler.updateInfo(info, id);

        return false;

    }
}
