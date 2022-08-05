package dk.dtu.compute.se.pisd.roborally.fileaccess;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.CommandCardField;
import dk.dtu.compute.se.pisd.roborally.model.GameHandler;
import dk.dtu.compute.se.pisd.roborally.model.GameInfo;
import dk.dtu.compute.se.pisd.roborally.model.fieldActions.FieldAction;
import dk.dtu.compute.se.pisd.roborally.model.gameRequests.GameResponse;
import dk.dtu.compute.se.pisd.roborally.model.gameRequests.PlayerLocationsResponse;
import dk.dtu.compute.se.pisd.roborally.model.gameRequests.UpdatePlayerRequest;
import dk.dtu.compute.se.pisd.roborally.model.programming.ICommand;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Handles the saving loading and reading from the json files games are saved as
 * @author Tobias Borgstrøm s184810
 */
public class FileHandler {

    public static final String JSON = ".json";
    public static final String SEPARATOR = File.separator;
    public static final String BASE_PATH = "src" + SEPARATOR + "main" + SEPARATOR + "resources";
    public static final String ONGOING_GAMES = BASE_PATH + SEPARATOR + "ongoing_games" + SEPARATOR;
    public static final String SAVED_GAMES = BASE_PATH + SEPARATOR + "saved_games" + SEPARATOR;
    public static final String GAME_INFO = BASE_PATH + SEPARATOR + "game_info" + SEPARATOR;
    public static final String INFO_SUFFIX = "_info.json";


    private Gson gson;

    public FileHandler() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(FieldAction.class, new FieldActionAdapter());
        gsonBuilder.registerTypeAdapter(ICommand.class, new CommandInterfaceAdapter());
        gsonBuilder.setPrettyPrinting();

        gson = gsonBuilder.create();
    }

    /**
     * saves a Gamecontroller as json in ongoing_games
     * @param gameController the gameController
     * @return the json String of the gameController
     */
    public String startGame(GameController gameController) {
        String filePath = gameController.gameId + JSON;
        String s = gson.toJson(gameController);
        try {
            FileWriter myWriter = new FileWriter(ONGOING_GAMES + filePath);
            myWriter.write(s);
            System.out.println("Game: " + gameController.gameId + " Has been started!");
            myWriter.close();


        } catch (IOException e) {
            System.err.println("Could not save file: " + e.getMessage());
            e.printStackTrace();
        }

        return s;

    }

    /**
     * Converts a gamecontroller to json string
     * @param gameController gamecontroller
     * @return json string
     */
    public String gameToJson(GameController gameController) {
        String s = gson.toJson(gameController);
        return s;
    }

    /**
     * Updates an ongoing game
     * @param gameController new GameController
     * @return if saved
     */
    public boolean gameUpdated(GameController gameController){

        String filePath =  gameController.gameId + ".json";
        String s =  gson.toJson(gameController);
        try {
            FileWriter myWriter = new FileWriter(ONGOING_GAMES + filePath);
            myWriter.write(s);
            myWriter.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("File not updated");
            return false;
        }
    }

    /**
     * returns a ongoing game
     * @param id the id of the game
     * @return the gamecontroller of the game
     */
    public GameController getGame(String id, boolean ongoingGame){
        String game;
        if (ongoingGame){
            game = ONGOING_GAMES;
        }
        else game = SAVED_GAMES;
        if(!id.contains(".json")){
            id += ".json";
        }
        final String filePath = game + id;
        String gameJson = "";
        try {
            Scanner myReader = new Scanner(new File(filePath));
            while (myReader.hasNextLine()) {
                gameJson += (myReader.nextLine());
            }
            return gson.fromJson(gameJson, GameController.class);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Stops a game
     * @param id the game's id
     * @return whether the game was stoppped or not
     */
    public boolean stopGame(String id) {
        final String filePath = ONGOING_GAMES + id + ".json";
        File file = new File(filePath);
        return file.delete();
    }

    /**
     * Moves a game from ongoing_games to saved_games
     * @param id id of the ongoing game
     * @return wether moved or not
     */
    public Boolean saveGame(String id)  {
        try {
            final String filePath = ONGOING_GAMES + id + JSON;
            Path path = (Path) Paths.get(SAVED_GAMES + id + JSON);
            Path temp = Files.copy(Paths.get(filePath), path, StandardCopyOption.REPLACE_EXISTING);
            //Path temp = Files.move(Paths.get(filePath), Paths.get(SAVED_GAMES + id + JSON));
            return temp != null;

        } catch (IOException e) {
            System.err.println("Game not saved: " + e.getMessage());
            return false;
        }
    }

    public String createInfo(GameInfo info, String id) {
        String path = GAME_INFO + id + INFO_SUFFIX;
        File f = new File(path);
        String json = gson.toJson(info);
        try {
            FileWriter writer = new FileWriter(f);
            writer.write(json);
            writer.close();
            return gson.toJson(true, boolean.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public GameInfo getInfo(String id) {
        String gameInfoString = "";
        String path = GAME_INFO + id + INFO_SUFFIX;
        try {
            Scanner myReader = new Scanner(new File(path));
            while (myReader.hasNextLine()) {
                gameInfoString += (myReader.nextLine());
            }
            return gson.fromJson(gameInfoString, GameInfo.class);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateInfo(GameInfo info, String id) {
        String path = GAME_INFO + id + INFO_SUFFIX;
        File f = new File(path);
        String json = gson.toJson(info);
        try {
            FileWriter writer = new FileWriter(f, false);
            writer.write(json);
            writer.close();
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getGamesList(boolean ongoingGames) {
        File f;
        if (ongoingGames) {
            f = new File(ONGOING_GAMES);
        } else {
            f = new File(SAVED_GAMES);
        }
        String[] strings = f.list();
        return Arrays.asList(strings);
    }

    public String gameResponseToJson(ArrayList<GameResponse> gameResponses) {
        Type listOfGameResponse = new TypeToken<ArrayList<GameResponse>>() {
        }.getType();
        return gson.toJson(gameResponses, listOfGameResponse);
    }

    public boolean loadGame(String id) {
        String filePath = SAVED_GAMES + id + JSON;
        String newPath = ONGOING_GAMES + id + JSON;
        File f = new File(filePath);
        Path path = (Path) Paths.get(newPath);
        try {
            String gameJson = "";
            Path temp = Files.copy(Paths.get(filePath), path, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            System.err.println("game not loaded");
            return false;

        }

    }

    public UpdatePlayerRequest updatePlayerRequest(String playerRequest) {
        return gson.fromJson(playerRequest, UpdatePlayerRequest.class);
    }

    public String getPlayerLocations(String id) {
        GameInfo info = getInfo(id);
        PlayerLocationsResponse response = new PlayerLocationsResponse((ArrayList) info.getLastPhasePlayers());

        return gson.toJson(response, PlayerLocationsResponse.class);
    }
}

