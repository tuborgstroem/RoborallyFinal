package dk.dtu.compute.se.pisd.roborally.model.fieldActions;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

public class Checkpoint extends FieldAction {
    public int checkPointNo;

    public Checkpoint(int checkPointNo){
        this.checkPointNo=checkPointNo;
    }

    @Override
    public boolean doAction(@NotNull GameController gameController, @NotNull Space space) {
        Player player = space.getPlayer();

        if (player != null){
            player.setPrevCheckpoint(this.checkPointNo);
            if(player.getPrevCheckpoint() == gameController.board.getCheckpoints().size()){
                gameController.gameFinished();
            }
        }

        // TODO needs to be implemented
        return false;
    }





}
