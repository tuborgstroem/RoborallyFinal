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
package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.designpatterns.observer.Observer;
import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;

import dk.dtu.compute.se.pisd.roborally.RoboRally;

import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.fieldActions.Checkpoint;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class AppController implements Observer {

    final private List<Integer> PLAYER_NUMBER_OPTIONS = Arrays.asList(2, 3, 4, 5, 6);
    final private List<String> PLAYER_COLORS = Arrays.asList("red", "green", "blue", "orange", "grey", "magenta");


    final private RoboRally roboRally;

    private GameController gameController;

    /**
     * Constructor
     * @param roboRally an instance of the game that will be assingned to the appController **/
    public AppController(@NotNull RoboRally roboRally) {
        this.roboRally = roboRally;
    }


    /**
     * Starts a new game with the selected number of players.
     */
    public void newGame() {
//        ChoiceDialog<>
        ChoiceDialog<String> boardDialog = getBoards();
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

            // XXX the board should eventually be created programmatically or loaded from a file
            //     here we just create an empty board with the required number of players.
            Board board;
            if(boardResult.isPresent()){
               board = LoadBoard.loadBoard(boardResult.get());
            }else {
                board = new Board(8,8);
            }
            gameController = new GameController(board);
            int no = result.get();
            for (int i = 0; i < no; i++) {
                Player player = new Player(board, PLAYER_COLORS.get(i), "Player " + (i + 1));
                board.addPlayer(player);
            }

            // XXX: V2
            // board.setCurrentPlayer(board.getPlayer(0));
            gameController.startStartPhase();

            roboRally.createBoardView(gameController);
        }
    }

    /***
     * save a game to be started later
     */
    public void saveGame() {
        // XXX needs to be implemented eventually
    }

    /**
     * Start a later game
     */
    public void loadGame() {
        // XXX needs to be implememted eventually
        // for now, we just create a new game
        if (gameController == null) {
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

            // here we save the game (without asking the user).
            saveGame();

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
     *
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

    public ChoiceDialog<String> getBoards(){
        List names = LoadBoard.getBoardNames();
        ChoiceDialog<String> choices = new ChoiceDialog<String>((String) names.get(0), names);
        return choices;

    }

    public void win(){
        if (gameController != null){
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Game Over!");
            alert.setContentText("Player whoever is on last checkpoint has won!");
            exit();

        }
    }


}
