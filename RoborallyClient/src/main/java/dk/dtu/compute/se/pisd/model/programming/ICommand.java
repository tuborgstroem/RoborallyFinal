package dk.dtu.compute.se.pisd.model.programming;

import dk.dtu.compute.se.pisd.model.Board;
import dk.dtu.compute.se.pisd.model.Player;

import java.util.Arrays;
import java.util.List;

public interface ICommand {

    void doAction(Player player, Board board);

    String displayName();

    boolean isInteractive();

    List<ICommand> getOptions();

    static ICommand getInstance(Command command, int val){

            switch (command){
                case MOVE :
                    return new MoveCard(command.displayName,  val);
                case TURN:
                    return new TurnCard(command.displayName, val);
                case AGAIN :
                    return new AgainCard(command.displayName);
                case OPTION_WEASEL: {
                    InteractiveCard card = new InteractiveCard(command, 0);
                    for (int i = 0; i < command.getOptions().size(); i++) {
                        card.addOption(new TurnCard(command.options.get(i).displayName, i));
                    }
                    return card;
                }
                case OPTION_SANDBOX:
                    int[] moveValues = {-1, 1, 2, 3};
                    int[] turnValues = {0, 1, 2};
                    InteractiveCard card = new InteractiveCard(command, 0);
                    int j = 0;
                    for(int i: turnValues){
                        card.addOption(new TurnCard(command.options.get(j).displayName, i));
                    }
                    for(int i : moveValues){
                        card.addOption(new MoveCard(command.options.get(j).displayName, i));
                    }
                    return card;


            }
            return null;


    }

    enum Command {

        // This is a very simplistic way of realizing different commands.

        MOVE("Move "), //Same as MOVE_ONE
        TURN("Turn "),
        OPTION_SANDBOX("Sandbox", TURN, TURN, TURN, MOVE, MOVE, MOVE, MOVE),
        OPTION_WEASEL("Left, Right or U-Turn", TURN, TURN, TURN),
        //        POWER_UP("Power Up"),  //@TODO implement when energy has been implementet
        AGAIN("Again");



        // XXX Assignment P3

        final public String displayName;

        // XXX Assignment P3
        // Command(String displayName) {
        //     this.displayName = displayName;
        // }
        //
        // replaced by the code below:

        final private List<Command> options;

        /**
         * constructor for commandcard
         * @param displayName name for display
         * @param options other options
         */
        Command(String displayName, Command... options) {
            this.displayName = displayName;

            if(options.length > 1){
                this.options = Arrays.stream(options).toList();
            }
            else{
                this.options = null;
            }
        }

        /**
         * @return other options
         */
        public List<Command> getOptions() {
            return options;
        }

    }

}
