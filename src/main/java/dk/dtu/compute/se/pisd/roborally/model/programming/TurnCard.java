package dk.dtu.compute.se.pisd.roborally.model.programming;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Player;

import java.util.List;

public class TurnCard implements ICommand{

    private final String LEFT   = "Left";
    private final String RIGHT  = "Right";
    private final String U_TURN = "U-Turn";

    private final String displayName;
    private final int direction; // 0 = left, 1 = right, 2 == u-turn

    /**
     * Constructor for TurnCards
     * @param displayName
     * @param direction
     */
    public TurnCard(String displayName, int direction) {
        switch (direction) {
            case 0 -> this.displayName = displayName + LEFT;
            case 1  -> this.displayName = displayName + RIGHT;
            case 2  -> this.displayName = U_TURN;
            default -> this.displayName = displayName;
        }
        this.direction = direction;
    }


    /**
     * Turns the player after the TurnCard has been used
     * @param player
     * @param board
     */
    @Override
    public void doAction(Player player, Board board) {
        switch (direction) {
            case 0 -> player.setHeading(player.getHeading().prev());
            case 1 -> player.setHeading(player.getHeading().next());
            case 2 -> {
                player.setHeading(player.getHeading().next());
                player.setHeading(player.getHeading().next());
            }
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
