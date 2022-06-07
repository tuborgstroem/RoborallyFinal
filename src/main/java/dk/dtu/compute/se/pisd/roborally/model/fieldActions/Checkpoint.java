package dk.dtu.compute.se.pisd.roborally.model.fieldActions;

import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

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

        if (player != null){
            player.landOnCheckpoint(this);
            if(isLastCheckpoint){
                AppController.win();
            }
        }

        // TODO needs to be implemented
        return false;
    }

    public int getNext() {
        return next;
    }
}
