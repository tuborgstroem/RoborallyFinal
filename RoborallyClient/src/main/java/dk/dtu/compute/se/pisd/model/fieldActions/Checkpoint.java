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
    public int checkPointNo;
    private final int next;
    private final boolean isLastCheckpoint;
    public Checkpoint(int checkPointNo, int next, boolean isLastCheckpoint){
        this.checkPointNo=checkPointNo;
        this.next=next;
        this.isLastCheckpoint=isLastCheckpoint;
    }

    /**
     * Checks if player is on last Checkpoint
     * @param gameController the gameController of the respective game
     * @param space the space this action should be executed for
     * @return false if player is not on last
     */
    @Override
    public boolean doAction(@NotNull GameController gameController, @NotNull Space space) {
        Player player = space.getPlayer();

        // We need communication with AppController


        if (player != null){
            player.landOnCheckpoint(this);
            if(isLastCheckpoint){
                //appController.win();
            }
        }

        // TODO needs to be implemented
        return false;
    }

    /**
     * Gets next Checkpoint
     * @return next
     */
    public int getNext() {
        return next;
    }
}
