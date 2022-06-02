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
package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.model.programming.ICommand.Command;
import dk.dtu.compute.se.pisd.roborally.model.programming.ICommand;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import static dk.dtu.compute.se.pisd.roborally.model.Heading.SOUTH;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class Player extends Subject {

    final public static int NO_REGISTERS = 5;
    final public static int NO_CARDS = 8;

    final public Board board;

    private String name;
    private String color;

    private Space space;
    private Heading heading = SOUTH;

    private CommandCardField[] program;
    private CommandCardField[] cards;
    private ArrayList<CommandCard> programmingDeck;
    private ArrayList<CommandCard> discardPile;

    private ICommand previousCommand;

    /**
     *
     * @param board which board
     * @param color player color
     * @param name player name
     */
    public Player(@NotNull Board board, String color, @NotNull String name) {
        this.board = board;
        this.name = name;
        this.color = color;

        this.space = null;

        program = new CommandCardField[NO_REGISTERS];
        for (int i = 0; i < program.length; i++) {
            program[i] = new CommandCardField(this);
        }

        cards = new CommandCardField[NO_CARDS];
        for (int i = 0; i < cards.length; i++) {
            cards[i] = new CommandCardField(this);
        }

        generateProgrammingDeck();
        Collections.shuffle(this.programmingDeck);

    }

    /**
     * @return player name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name new player name
     */
    public void setName(String name) {
        if (name != null && !name.equals(this.name)) {
            this.name = name;
            notifyChange();
            if (space != null) {
                space.playerChanged();
            }
        }
    }

    /**
     * @return player color
     */
    public String getColor() {
        return color;
    }

    /**
     * @param color new color
     */
    public void setColor(String color) {
        this.color = color;
        notifyChange();
        if (space != null) {
            space.playerChanged();
        }
    }

    /**
     * @return player location
     */
    public Space getSpace() {
        return space;
    }

    /**
     * @param space new player location
     */
    public void setSpace(Space space) {
        Space oldSpace = this.space;
        if (space != oldSpace &&
                (space == null || space.board == this.board)) {
            this.space = space;
            if (oldSpace != null) {
                oldSpace.setPlayer(null);
            }
            if (space != null) {
                space.setPlayer(this);
            }
            notifyChange();
        }
    }

    /**
     * @return direction player is headed
     */
    public Heading getHeading() {
        return heading;
    }

    /**
     * @param heading new direction
     */
    public void setHeading(@NotNull Heading heading) {
        if (heading != this.heading) {
            this.heading = heading;
            notifyChange();
            if (space != null) {
                space.playerChanged();
            }
        }
    }

    /**
     * return a programField
     * @param i number of programfield
     * @return programfield
     */
    public CommandCardField getProgramField(int i) {
        return program[i];
    }

    /**
     * return a cardField
     * @param i number of cardfield
     * @return cardfield
     */
    public CommandCardField getCardField(int i) {
        return cards[i];
    }

    public ICommand getPreviousCommand(){
        return previousCommand;
    }

    public void setPreviousCommand(ICommand previousCommand) {
        this.previousCommand = previousCommand;
    }

    /**
     * generates the 20 cards for the programming deck. At the start there should be:
     * 2 Again
     * 1 U-Turn
     * 3 turn Right
     * 3 Turn left
     * 1 back up
     * 5 move 1
     * 3 move 2
     * 1 move 3
     * 1 power up
     */
    public void generateProgrammingDeck(){
        this.programmingDeck = new ArrayList<>();
        int j = 0;
        int[] numberCards =  {5, 3, 1,  1};
        int[] valueCards  =  {1, 2, 3, -1};
        for(int num : numberCards){
            for(int i = 0; i<num; i++){
                CommandCard card = new CommandCard(Objects.requireNonNull(ICommand.getInstance(Command.MOVE, valueCards[j])));
                this.programmingDeck.add(card);
            }
            j++;
        }
        numberCards = new int[]{3, 3, 1};
        valueCards  = new int[]{0, 1, 2};
        j = 0;
        for(int num : numberCards){
            for(int i = 0; i<num; i++){
                CommandCard card = new CommandCard(Objects.requireNonNull(ICommand.getInstance(Command.TURN, valueCards[i])));
                this.programmingDeck.add(card);
            }
            j++;
        }
        for (int i =0; i<2; i++){
            CommandCard card = new CommandCard(Objects.requireNonNull(ICommand.getInstance((Command.AGAIN), 0)));
            this.programmingDeck.add(card);
        }
        this.discardPile = new ArrayList<>();

        // @TODO add one for power up when energy is implementet
    }

    /**
     * Draws top card from programming deck
     * @return top card
     */
    public CommandCard drawCard(){
        if(programmingDeck.isEmpty()){
            programmingDeck = discardPile;
            discardPile = new ArrayList<>();
            Collections.shuffle(programmingDeck);

        }
        return this.programmingDeck.remove(0);
    }

    /**
     * @return player's programming deck
     */
    public ArrayList<CommandCard> getProgrammingDeck(){
        return programmingDeck;
    }

    /**
     * @return player's discard pile
     */
    public ArrayList<CommandCard> getDiscardPile(){
        return discardPile;
    }

    /**
     * adds cards to discard pile
     * @param cards the cards to be added
     */
    public void discardCards(ArrayList<CommandCard> cards){
        this.discardPile.addAll(cards);
    }

    /**
     * adds one card to discard pile
     * @param card the card to be added
     */
    public void discardCard(CommandCard card){
        this.discardPile.add(card);
    }

}
