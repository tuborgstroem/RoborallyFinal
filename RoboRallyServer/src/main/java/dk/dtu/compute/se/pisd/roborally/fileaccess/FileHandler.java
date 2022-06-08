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
import java.util.Scanner;
import java.util.Stack;

public class FileHandler {

    public static final String ONGOING_GAMES= "src\\main\\resources\\ongoing_games\\";

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
}
