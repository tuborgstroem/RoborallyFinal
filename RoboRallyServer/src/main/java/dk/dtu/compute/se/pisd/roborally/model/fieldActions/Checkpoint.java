package dk.dtu.compute.se.pisd.roborally.model.fieldActions;

//import dk.dtu.compute.se.pisd.roborally.RoboRally;
//import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

import org.jetbrains.annotations.NotNull;

/**
 * @author Kasper s215792
 * @author Magnus s204447
 */
public class Checkpoint extends FieldAction {
    private int checkPointNo;
    private  int next;
    private boolean isLastCheckpoint=false;
    public Checkpoint(){
        this.setType("Checkpoint");
    }

    public void setcheckPointNo(int checkPointNo) {
        this.checkPointNo = checkPointNo;
    }

    public int getcheckPointNo() {
        return this.checkPointNo;
    }
    public void setnext(int next) {
        if (next==-1)
            isLastCheckpoint=true;
        this.next = next;
    }
    /**
     * Checks if player is on last Checkpoint
     * @param gameController the gameController of the respective game
     * @param space the space this action should be executed for
     * @return false if player is not on last
     */
    @Override
    public boolean doAction(@NotNull GameController gameController, @NotNull Space space) {
//        Player player = space.getPlayer();

        // We need communication with AppController


//        if (player != null){
//            player.landOnCheckpoint(this);
//            if(isLastCheckpoint){
//                //appController.win();
//            }
//        }

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
