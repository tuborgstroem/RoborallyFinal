package dk.dtu.compute.se.pisd.roborally.model.programming;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MoveCard implements ICommand{

    private final String BACK_UP = "Back up";
    private final String displayName;
    private final int moveNum;

    public MoveCard(String displayName, int moveNum) {
        if (moveNum == -1) {
            this.displayName = BACK_UP;
        }
        else {
            this.displayName = displayName + moveNum;
        }
        this.moveNum = moveNum;
    }

    @Override
    public void doAction(Player player, Board board) {
        if(moveNum < 0){
            moveOne(player, board, player.getHeading().next().next());
        }
        for(int i = 0; i<moveNum; i++){
            moveOne(player, board, player.getHeading());
        }
    }


    public void moveOne(@NotNull Player player, Board board, Heading heading) {
        Space space = player.getSpace();
        if (player != null && player.board == board && space != null) {
            Space target = board.getNeighbour(space, heading);
            if (target != null) {
                Player neighbourPlayer = target.getPlayer();
                if (neighbourPlayer != null) {
                    Heading prevHeading = neighbourPlayer.getHeading();
                    neighbourPlayer.setHeading(heading);
                    moveOne(neighbourPlayer, board, heading);
                    neighbourPlayer.setHeading(prevHeading);
                }
                target.setPlayer(player);
            }
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
