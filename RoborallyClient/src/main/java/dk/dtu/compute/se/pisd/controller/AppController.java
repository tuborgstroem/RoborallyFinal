/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package dk.dtu.compute.se.pisd.controller;

import dk.dtu.compute.se.pisd.RoboRally;
import dk.dtu.compute.se.pisd.controller.Requests.OngoingGamesRequests;
import dk.dtu.compute.se.pisd.designpatterns.observer.Observer;
import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;

//import dk.dtu.compute.se.pisd.fileaccess.LoadBoard;
//import dk.dtu.compute.se.pisd.model.Board;
//import dk.dtu.compute.se.pisd.model.Player;
import dk.dtu.compute.se.pisd.model.Board;
import dk.dtu.compute.se.pisd.model.Player;
import dk.dtu.compute.se.pisd.view.HostApplication;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.logging.FileHandler;

import static javafx.application.Application.launch;

/**
 * ...
 * @author Ekkart Kindler, ekki@dtu.dk
 * @author Tobias Borgstrøm
 */
public class AppController implements Observer {

    final private List<Integer> PLAYER_NUMBER_OPTIONS = Arrays.asList(1, 2, 3, 4, 5, 6);
    final private List<String> PLAYER_COLORS = Arrays.asList("red", "green", "blue", "orange", "grey", "magenta");


    final private RoboRally roboRally;

    private final RoboRallyService service;

    private GameController gameController;

    /**
     * Constructor
     * @param roboRally an instance of the game that will be assingned to the appController
     **/
    public AppController(@NotNull RoboRally roboRally) {
        this.roboRally = roboRally;
        service = new RoboRallyService();
    }

    /**
     * Starts a new game with the selected number of players.
     */
    public void newGame()  {

//        ChoiceDialog<>
        ChoiceDialog<String> boardDialog =  getBoards();
        boardDialog.setTitle("Board");
        boardDialog.setHeaderText("Select which board you wish to play on");
        Optional<String> boardResult = boardDialog.showAndWait();
        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(PLAYER_NUMBER_OPTIONS.get(0), PLAYER_NUMBER_OPTIONS);
        dialog.setTitle("Player number");
        dialog.setHeaderText("Select number of players");
        Optional<Integer> result = dialog.showAndWait();

        if (result.isPresent()) {
            if (gameController != null) {
                // The UI should not allow this, but in case this happens anyway.
                // give the user the option to save the game or abort this operation!
                if (!stopGame()) {
                    return;
                }
            }


            TextInputDialog nameInput = new TextInputDialog();
            String  playerName = addPlayer(nameInput);

            gameController =  service.newGame(boardResult.get(), result.get(), playerName);
            String id = gameController.gameId;
            startGame(id);

        }
    }

    /**
     * when players are in starts the game
     * @param id of the game
     */
    public void startGame(String id){
        System.out.println(id);
        while(!service.gameReady(id)){
            playersNotReadyAlert();

        }

        gameController = service.getGame(id);
        gameController.readyPlayers();
        System.out.println("game ready " + gameController.board.getPlayersNumber());

        // XXX: V2

        gameController.startStartPhase();

        roboRally.createBoardView(gameController);
    }

    /***
     * save a game to be started later
     */
    public void saveGame() {
        service.saveGame(gameController.gameId);
        System.out.println("saved game?");
    }

    /**
     * Start a later game
     */
    public void loadGame() {

        // XXX needs to be implememted eventually
        // for now, we just create a new game
        if (gameController == null) {
            System.out.println("Starting new game");
            newGame();
        }
    }

    /**
     * Stop playing the current game, giving the user the option to save
     * the game or to cancel stopping the game. The method returns true
     * if the game was successfully stopped (with or without saving the
     * game); returns false, if the current game was not stopped. In case
     * there is no current game, false is returned.
     *
     * @return true if the current game was stopped, false otherwise
     */
    public boolean stopGame() {
        if (gameController != null) {
            service.stopGame(gameController.gameId);
            gameController = null;
            roboRally.createBoardView(null);
            return true;
        }
        return false;
    }

    /**
     * Exit roboRally game and shutdown the application
     */
    public void exit() {
        if (gameController != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Exit RoboRally?");
            alert.setContentText("Are you sure you want to exit RoboRally?");
            Optional<ButtonType> result = alert.showAndWait();
            if (!result.isPresent() || result.get() != ButtonType.OK) {
                return; // return without exiting the application
            }

        }

        // If the user did not cancel, the RoboRally application will exit
        // after the option to save the game
        if (gameController == null || stopGame()) {
            Platform.exit();
        }
    }

    /**
     * An alert for waiting for other players to join
     */
    public void playersNotReadyAlert() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setHeaderText("Players not ready");
        alert.setContentText("All players are not in yet wait and press ok to retry");
        alert.showAndWait();
    }


    /**
     * @return if the game is running
     */
    public boolean isGameRunning() {
        return gameController != null;
    }

    /**
     * @param subject the subject
     */
    @Override
    public void update(Subject subject) {
        // XXX do nothing for now
    }

    /**
     * Choice dialog for the host player to choose which board to play on
     * @return the board name chosen
     */
    public ChoiceDialog<String> getBoards() {
        List names = service.getBoards();
//        ChoiceDialog<String> choices = new ChoiceDialog<String>((String) names.get(0), names);
        ChoiceDialog<String> choices = new ChoiceDialog(names.get(0), names);
        return choices;

    }

    /**
     * a popup for a player to enter name
     * @param dialog Textinputdialog
     * @return the playername
     */
    public String addPlayer( TextInputDialog dialog){

        dialog.setTitle("Add player");
        dialog.setContentText("Enter name of player");
        dialog.showAndWait();
        return dialog.getResult();

    }


    /**
     * join an ongoing game
     */
    public void joinGame() {
        ArrayList<OngoingGamesRequests> games = service.getOngoingGames();
        if (games == null){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText("There are no games that can be joined");
            alert.showAndWait();

        }else {
            OngoingGamesRequests game = chooseGameToJoin(games);
            TextInputDialog nameInput = new TextInputDialog();
            String playerName = addPlayer(nameInput);
            service.addPlayer(game.getGameId(), playerName);
            startGame(game.getGameId());
        }
    }

    /**
     * dialog for getting the ongoing games
     * @param ongoingGames on going games
     * @return game if joined else null
     */
    public OngoingGamesRequests chooseGameToJoin(ArrayList<OngoingGamesRequests> ongoingGames){
        if(ongoingGames.size() > 0) {
            ArrayList<String> gameNames = new ArrayList<>();
            for (int i = 0; i<ongoingGames.size(); i++){
                gameNames.add(ongoingGames.get(i).toDialogString());
            }
            ChoiceDialog<String> gamesDialog = new ChoiceDialog(ongoingGames.get(0).toDialogString(), gameNames);
            gamesDialog.setTitle("Join game");
            gamesDialog.setHeaderText("Select a game to join");
            gamesDialog.setContentText("Game:");
            Optional<String> result = gamesDialog.showAndWait();
            String s = result.get();
            for (OngoingGamesRequests game: ongoingGames){
                if (s.contains(game.toDialogString())){
                    return game;
                }
            }
            return null;
        }else{
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText("There are no games that can be joined");
            alert.showAndWait();
            return null;
        }
    }
}
