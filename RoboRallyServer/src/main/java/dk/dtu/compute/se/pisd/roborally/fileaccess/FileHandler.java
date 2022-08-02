package dk.dtu.compute.se.pisd.roborally.fileaccess;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.fieldActions.FieldAction;
import dk.dtu.compute.se.pisd.roborally.model.programming.ICommand;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

/**
 * Handles the saving loading and reading from the json files games are saved as
 * @author Tobias Borgstrøm s184810
 */
public class FileHandler {

    public static final String ONGOING_GAMES= "src\\main\\resources\\ongoing_games\\";
    public static final String SAVED_GAMES= "src\\main\\resources\\saved_games\\";

    /**
     * saves a Gamecontroller as json in ongoing_games
     * @param gameController the gameController
     * @return the json String of the gameController
     */
    public static String startGame(GameController gameController)  {
        String filePath =  gameController.gameId + ".json";
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(FieldAction.class, new FieldActionAdapter());
        Gson gson = gsonBuilder.create();
        String s =  gson.toJson(gameController);
        try {
            FileWriter myWriter = new FileWriter(ONGOING_GAMES + filePath );
            myWriter.write(s);
            System.out.println("Game: " + gameController.gameId + " Has been started!");
            myWriter.close();


        }catch (IOException e ){
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
    public static String gameToJson(GameController gameController){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(FieldAction.class, new FieldActionAdapter());
        Gson gson = gsonBuilder.create();
        String s =  gson.toJson(gameController);
        return s;
    }

    /**
     * Updates an ongoing game
     * @param gameController new GameController
     * @return if saved
     */
    public static boolean gameUpdated(GameController gameController){

        String filePath =  gameController.gameId + ".json";
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(FieldAction.class, new FieldActionAdapter());
        gsonBuilder.registerTypeAdapter(ICommand.class, new CommandInterfaceAdapter());
        Gson gson = gsonBuilder.create();
        String s =  gson.toJson(gameController);
        try {
            FileWriter myWriter = new FileWriter(ONGOING_GAMES + filePath);
            myWriter.write(s);
            myWriter.close();
            return true;
        }catch (IOException e){
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
    public static GameController getOngoingGame(String id){
        final String filePath = ONGOING_GAMES + id + ".json";
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(FieldAction.class, new FieldActionAdapter());
        gsonBuilder.registerTypeAdapter(ICommand.class, new CommandInterfaceAdapter());
        Gson gson = gsonBuilder.create();
        String gameJson = "";
        try {
            Scanner myReader = new Scanner( new File(filePath));
            while (myReader.hasNextLine()){
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
    public static boolean stopGame(String id) {
        final String filePath = ONGOING_GAMES + id + ".json";
        File file = new File(filePath);
        return file.delete();
    }

    /**
     * Moves a game from ongoing_games to saved_games
     * @param id id of the ongoing game
     * @return wether moved or not
     */
    public static Boolean saveGame(String id)  {
        try {
            final String filePath = ONGOING_GAMES + id + ".json";
            Path path = (Path) Paths.get(SAVED_GAMES + id + ".json");
            Path temp = Files.copy(Paths.get(filePath), path, StandardCopyOption.REPLACE_EXISTING);
            //Path temp = Files.move(Paths.get(filePath), Paths.get(SAVED_GAMES + id + ".json"));
            return temp!=null;

        }catch (IOException e){
            System.err.println("Game not saved: " + e.getMessage());
            return false;
        }
    }
}
