package dk.dtu.compute.se.pisd.roborally.model.programming;

import dk.dtu.compute.se.pisd.roborally.model.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MoveCard implements ICommand{

    private final String BACK_UP = "Back up";
    private final String displayName;
    private final int moveNum;

    /**
     * Constructor for MoveCards
     * @param displayName
     * @param moveNum
     */
    public MoveCard(String displayName, int moveNum) {
        if (moveNum == -1) {
            this.displayName = BACK_UP;
        }
        else {
            this.displayName = displayName + moveNum;
        }
        this.moveNum = moveNum;
    }

    /**
     * Moves the player in the direction its facing after the movecard is used
     * Moves the player the opposite way of heading if the move card is BACK_UP
     * @param player
     * @param board
     */
    @Override
    public void doAction(Player player, Board board) {
        try {
            if (moveNum < 0) {
                board.movePlayer(player, player.getHeading().next().next());
            }
            for (int i = 0; i < moveNum; i++) {
                board.movePlayer(player, player.getHeading());
            }
        }catch (InvalidMoveException e){
            e.printStackTrace();
        }
    }

    /**
     * @return displayName
     */
    @Override
    public String displayName() {
        return displayName;
    }

    /**
     * @return false
     */
    @Override
    public boolean isInteractive() {
        return false;
    }

    /**
     * @return null
     */
    @Override
    public List<ICommand> getOptions() {
        return null;
    }

}
