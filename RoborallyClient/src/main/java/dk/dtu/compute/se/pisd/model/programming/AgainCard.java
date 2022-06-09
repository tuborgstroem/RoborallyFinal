package dk.dtu.compute.se.pisd.model.programming;

import dk.dtu.compute.se.pisd.model.Board;
import dk.dtu.compute.se.pisd.model.Player;

import java.util.List;

public class AgainCard implements ICommand{

    private final String displayName;
    private final String type;

    public AgainCard(String displayName) {
        this.displayName = displayName;
        this.type = "AgainCard";
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void doAction(Player player, Board board) {
        if(player.getPreviousCommand() != null) {
            player.getPreviousCommand().doAction(player, board);
        }
    }

    @Override
    public String displayName() {
        return displayName;
    }

    @Override
    public boolean isInteractive() {
        return false;
    }

    @Override
    public List<ICommand> getOptions() {
        return null;
    }
}
