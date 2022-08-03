package dk.dtu.compute.se.pisd.model.fieldActions;

import dk.dtu.compute.se.pisd.RoboRally;
import dk.dtu.compute.se.pisd.controller.AppController;
import dk.dtu.compute.se.pisd.controller.GameController;
import dk.dtu.compute.se.pisd.model.Player;
import dk.dtu.compute.se.pisd.model.Space;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kasper s215792
 * @author Magnus s204447
 */
public class Checkpoint extends FieldAction {
    private int checkPointNo;

    //private final int next;
    //private final boolean isLastCheckpoint;
   /* public Checkpoint(int checkPointNo, int next, boolean isLastCheckpoint){
        this.checkPointNo=checkPointNo;
        this.next=next;
        this.isLastCheckpoint=isLastCheckpoint;
    }*/

    /**
     * Checks if player is on last Checkpoint
     * @param gameController the gameController of the respective game
     * @param space the space this action should be executed for
     * @return false if player is not on last
     */
    @Override
    public boolean doAction(@NotNull GameController gameController, @NotNull Space space) {
        Player player = space.getPlayer();

        if (player != null && player.checkpoints.size() == this.checkPointNo){
            player.checkpoints.add(this);

            System.out.println(space.getPlayer().getName() +" has gathered " + space.getPlayer().checkpoints.size() + " checkpoints out of " + gameController.board.getNOCheckpoints() + " checkpoints");
            if (player != null && space.getPlayer().checkpoints.size()==gameController.board.getNOCheckpoints() && gameController.winnerIs(gameController.board) == null){
                player.setWinner(true);

            }
            return true;
        }

        return false;
    }

    public int getCheckPointNo() {
        return checkPointNo;
    }
}
