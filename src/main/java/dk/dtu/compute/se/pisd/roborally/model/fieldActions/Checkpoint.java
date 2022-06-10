package dk.dtu.compute.se.pisd.roborally.model.fieldActions;

import dk.dtu.compute.se.pisd.roborally.RoboRally;
import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.application.Application;
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

    public int getNext() {
        return next;
    }
}
