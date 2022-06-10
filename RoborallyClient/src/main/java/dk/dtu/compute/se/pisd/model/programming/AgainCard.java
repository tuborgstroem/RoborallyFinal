package dk.dtu.compute.se.pisd.model.programming;

import dk.dtu.compute.se.pisd.model.Board;
import dk.dtu.compute.se.pisd.model.Player;

import java.util.List;

public class AgainCard implements ICommand{

    private final String displayName;
    private final String type;

    /**
     * Constructor for AGAIN card
     * @param displayName
     */
    public AgainCard(String displayName) {
        this.displayName = displayName;
        this.type = "AgainCard";
    }

    /**
     * @return type
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * copys the previous command from the register when AGAIN card is used
     * @param player
     * @param board
     */
    @Override
    public void doAction(Player player, Board board) {
        if(player.getPreviousCommand() != null) {
            player.getPreviousCommand().doAction(player, board);
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
