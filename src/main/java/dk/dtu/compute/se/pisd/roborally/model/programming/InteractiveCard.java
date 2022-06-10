package dk.dtu.compute.se.pisd.roborally.model.programming;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Player;

import java.util.ArrayList;
import java.util.List;

public class InteractiveCard implements ICommand{

    private final String displayName;
    private final List<ICommand> options;

    /**
     * Constructor for InteractiveCards
     * @param command
     * @param val
     */
    public InteractiveCard(Command command, int val){
        this.displayName = command.displayName;
        options = new ArrayList<>();
    }

    @Override
    public void doAction(Player player, Board board) {

    }

    public void addOption(ICommand command){
        this.options.add(command);
    }

    /**
     * @return displayName
     */
    @Override
    public String displayName() {
        return displayName;
    }

    /**
     * @return true
     */
    @Override
    public boolean isInteractive() {
        return true;
    }

    /**
     * @return options
     */
    @Override
    public List<ICommand> getOptions() {
        return options;
    }
}
