package dk.dtu.compute.se.pisd.model.programming;


import dk.dtu.compute.se.pisd.model.Board;
import dk.dtu.compute.se.pisd.model.Player;

import java.util.ArrayList;
import java.util.List;

public class InteractiveCard implements ICommand{

    private final String displayName;
    private final List<ICommand> options;

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

    @Override
    public String displayName() {
        return displayName;
    }

    @Override
    public boolean isInteractive() {
        return true;
    }

    @Override
    public List<ICommand> getOptions() {
        return options;
    }
}
