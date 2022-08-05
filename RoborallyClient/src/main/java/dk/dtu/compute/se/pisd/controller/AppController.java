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
import dk.dtu.compute.se.pisd.controller.Requests.GameResponse;
import dk.dtu.compute.se.pisd.designpatterns.observer.Observer;
import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;

//import dk.dtu.compute.se.pisd.fileaccess.LoadBoard;
//import dk.dtu.compute.se.pisd.model.Board;
//import dk.dtu.compute.se.pisd.model.Player;
import dk.dtu.compute.se.pisd.model.Player;
import dk.dtu.compute.se.pisd.model.Space;
import dk.dtu.compute.se.pisd.model.dataModels.GameControllerData;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

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

            GameControllerData gameController =  service.newGame(boardResult.get(), result.get(), playerName);
            String id = gameController.getGameId();
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
            playersNotReadyAlert("All players not in", "All players have not joined the game yet");

        }

        gameController = new GameController(service.getGame(id));
        gameController.readyPlayers();
        System.out.println("game ready " + gameController.board.getPlayersNumber());

        // XXX: V2

        gameController.startStartPhase(this);

        roboRally.createBoardView(gameController);
    }

    /***
     * save a game to be started later
     */
    public void saveGame() {
        service.saveGame(gameController.gameId);
        System.out.println("saved game");
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
    public void playersNotReadyAlert(String headerText, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setHeaderText(headerText);
        alert.setContentText(message);
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
        if (gameController!=null){
            if (gameController.winnerIs(gameController.board)!= null){
                playerWon();
            }
        }
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
    public void joinGame(boolean joiningGame) {
        ArrayList<GameResponse> games = service.getGamesList(joiningGame);
        if (games == null){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText("There are no games that can be joined");
            alert.showAndWait();

        }else {
            GameResponse game = chooseGameToJoin(games);
            if(joiningGame) {
                TextInputDialog nameInput = new TextInputDialog();
                String playerName = addPlayer(nameInput);
                service.addPlayer(game.getGameId(), playerName);
                startGame(game.getGameId());
            }
            else {
                GameControllerData gameControllerData =service.loadGame(game.getGameId());
                this.gameController = gameControllerData.toGameController();
                Space tempSpace = gameController.board.getPlayer(0).getSpace();
                gameController.readyPlayers();
                switch (gameController.board.getPhase()){

                    case START, INITIALISATION-> {
                        gameController.startStartPhase(this);

                    }

                    case PROGRAMMING -> {
                        gameController.startProgrammingPhase();
                    }
                    case ACTIVATION -> {
                        gameController.finishProgrammingPhase();
                    }
                }

                roboRally.createBoardView(gameController);
                gameController.moveCurrentPlayerToSpace(tempSpace);
                // gameController.board.getCurrentPlayer().
            }
        }
    }



    /**
     * dialog for getting the ongoing games
     * @param ongoingGames on going games
     * @return game if joined else null
     */
    public GameResponse chooseGameToJoin(ArrayList<GameResponse> ongoingGames){
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
            for (GameResponse game: ongoingGames){
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

    public void playerWon() {

        if (gameController.winnerIs(gameController.board) == null) {
            return;
        }
        Player winner = gameController.winnerIs(gameController.board);
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game Over!");
        alert.setHeaderText("The Game has Finished");
        alert.setContentText(winner.getName() + " has won the game!");
        Optional<ButtonType> result = alert.showAndWait();
       /* if (!result.isPresent() || result.get() != ButtonType.OK) {
            return;
        }*/
        if (gameController == null || stopGame()) {
            Platform.exit();
        }
    }
}
