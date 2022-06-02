package dk.dtu.compute.se.pisd.roborally.model.programming;

import dk.dtu.compute.se.pisd.roborally.model.*;
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
        try {

            if (moveNum < 0) {

                moveOne(player, board, player.getHeading().next().next());
            }
            for (int i = 0; i < moveNum; i++) {
                moveOne(player, board, player.getHeading());
            }
        }catch (InvalidMoveException e){
            e.printStackTrace();
        }
    }


    public void moveOne(@NotNull Player player, Board board, Heading heading)  throws InvalidMoveException {
        Space space = player.getSpace();
        Space target;
        if (player != null && player.board == board && space != null) {
            try {
                target = board.getNeighbour(space, heading);
            }catch (InvalidMoveException e){
                throw new InvalidMoveException();
            }
            if (target != null) {
                Player neighbourPlayer = target.getPlayer();
                if (neighbourPlayer != null && neighbourPlayer != player) {
                    try {
                        moveOne(neighbourPlayer, board, heading);
                    }
                    catch (InvalidMoveException e){
                        target = player.getSpace();
                    }
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
