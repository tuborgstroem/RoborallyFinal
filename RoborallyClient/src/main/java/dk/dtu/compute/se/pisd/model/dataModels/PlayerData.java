package dk.dtu.compute.se.pisd.model.dataModels;

import dk.dtu.compute.se.pisd.model.*;
import dk.dtu.compute.se.pisd.model.fieldActions.Checkpoint;
import dk.dtu.compute.se.pisd.model.programming.ICommand;

import java.util.ArrayList;

import static dk.dtu.compute.se.pisd.model.Heading.SOUTH;

public class PlayerData {
    final public static int NO_REGISTERS = 5;
    final public static int NO_CARDS = 8;

//    public Board board;

    private final String name;
    private String color;

    private Checkpoint prevCheckpoint;

    private SpaceData space;
    private Heading heading = SOUTH;

    private ArrayList<Checkpoint> checkpoints;

    private ArrayList<CommandCard> hand;
    private ArrayList<CommandCard> programmingDeck;
    private ArrayList<CommandCard> discardPile;

    private CommandCardField[] program;

    private CommandCardField[] cards;

    private ICommand previousCommand;

    public PlayerData(Player player){
        this.name = player.getName();
        this.color = player.getColor();
        this.prevCheckpoint = player.getPrevCheckpoint();
        this.space = new SpaceData(player.getSpace());
        this.checkpoints = player.getCheckpoints();
        this.programmingDeck = player.getProgrammingDeck();
        this.discardPile = player.getDiscardPile();
    }

    public Player updatePlayer(Player player){

        Space newSpace = player.board.getSpace(this.space.x, this.space.y);
        player.setSpace(newSpace);
        return player;
    }

    public String getName(){
        return name;
    }

    public String getColor(){
        return color;
    }

    public SpaceData getSpace() {
        return space;
    }
}
