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

import dk.dtu.compute.se.pisd.model.*;
import dk.dtu.compute.se.pisd.model.fieldActions.FieldAction;
import dk.dtu.compute.se.pisd.model.programming.AgainCard;
import dk.dtu.compute.se.pisd.model.programming.ICommand;
import org.jetbrains.annotations.NotNull;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class GameController {
    int counterstart = 0;
    final public Board board;
    private final int numberOfPlayers;

    public final String gameId;


    /**
     * Constructor
     * @param board the board on which the game will play
     */
    public GameController(@NotNull Board board, String id, int numberOfPlayers) {
        this.gameId = id;
        this.board = board;
        this.numberOfPlayers = numberOfPlayers;
    }

    /**
     * This is just some dummy controller operation to make a simple move to see something
     * happening on the board. This method should eventually be deleted!
     *
     * @param space the space to which the current player should move
     */



    /**
     * Starts the programming phase in which the players programs their robot for the next phase.
     */
    public void startProgrammingPhase() {
        board.setPhase(Phase.PROGRAMMING);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);

        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            if (player != null) {
                for (int j = 0; j < Player.NO_REGISTERS; j++) {
                    CommandCardField field = player.getProgramField(j);
                    field.setCard(null);
                    field.setVisible(true);
                }
                for (int j = 0; j < Player.NO_CARDS; j++) {
                    CommandCardField field = player.getCardField(j);
                    CommandCard card = generateRandomCommandCard(player);
                    field.setCard(card);
                    field.setVisible(true);
                    player.discardCard(card); //add them here to make sure they all go into discards
                }
            }
        }
    }

    /**
     * Generates the random command cards
     * @TODO Change so that each player has their own discard pile and programming pile.
     * @return a Command card
     */
    private CommandCard generateRandomCommandCard(@NotNull Player player) {
//        CommandCard card = new CommandCard(ICommand.getInstance(ICommand.Command.OPTION_WEASEL, 0));
//        return card;
        return player.drawCard();
    }

    /**
     * Makes a register visible
     * @param register the register which should be revealed
     */
    private void makeProgramFieldsVisible(int register) {
        if (register >= 0 && register < Player.NO_REGISTERS) {
            for (int i = 0; i < board.getPlayersNumber(); i++) {
                Player player = board.getPlayer(i);
                CommandCardField field = player.getProgramField(register);
                field.setVisible(true);
            }
        }
    }

    /***
     * Executes activation phase without pausing after each move
     */
    public void executePrograms() throws InvalidMoveException {
        board.setStepMode(false);
        continuePrograms();
    }

    /**
     * executes the next step in activationPhase
     */
    public void executeStep() throws InvalidMoveException {
        board.setStepMode(true);
        continuePrograms();
    }

    /**
     * Continues activation phase after an interactive commandCard
     */
    private void continuePrograms() throws InvalidMoveException {
        do {
            executeNextStep();
        } while (board.getPhase() == Phase.ACTIVATION && !board.isStepMode());

        if (board.getPhase() == Phase.PROGRAMMING){
            triggerFieldAction();
        }
    }

    /**
     * Executes the next step
     */
    private void executeNextStep() throws InvalidMoveException {
        counterstart++;
        Player currentPlayer = board.getCurrentPlayer();
        if (board.getPhase() == Phase.ACTIVATION && currentPlayer != null) {
            int step = board.getStep();
            if (step >= 0 && step < Player.NO_REGISTERS) {
                CommandCard card = currentPlayer.getProgramField(step).getCard();
                if (card != null) {
                    ICommand command = card.command;
                    executeCommand(currentPlayer, command);
                    if(command.isInteractive() ){
                        board.setPhase(Phase.PLAYER_INTERACTION);
                        return;
                    }
                }
                int nextPlayerNumber = board.getPlayerNumber(currentPlayer) + 1;
                if (nextPlayerNumber < board.getPlayersNumber()) {
                    board.setCurrentPlayer(board.getPlayer(nextPlayerNumber));
                } else {
                    step++;
                    if (step < Player.NO_REGISTERS) {
                        makeProgramFieldsVisible(step);
                        board.setStep(step);
                        board.setCurrentPlayer(board.getPlayer(0));
                    } else {
                        startProgrammingPhase();
                    }
                }
            } else {
                // this should not happen
                assert false;
            }
        } else {
            // this should not happen
            assert false;
        }
    }

    /**
     * Determines the command and executes it
     * @param player the player
     * @param command the command
     */
    private void executeCommand(@NotNull Player player, ICommand command ) {
        if ( !(command instanceof AgainCard)){
            player.setPreviousCommand(command);
        }
        command.doAction(player, board);
    }
    /**
     * Method which calls the specific fieldAction for the tile a player has landed on
     *
     */
    public void triggerFieldAction() throws InvalidMoveException {
        for (int i = 0; i < board.getPlayersNumber(); i++){
            Player p = board.getPlayer(i);
            Space space = p.getSpace();
            for(FieldAction f : space.getActions()){
                f.doAction(this, space);
            }
        }
    }

    public void readyPlayers() {
        for (Player p: board.getPlayers()){
            p.readyPlayer();
        }
    }
}
