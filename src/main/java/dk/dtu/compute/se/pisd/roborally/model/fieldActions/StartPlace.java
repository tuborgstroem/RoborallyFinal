package dk.dtu.compute.se.pisd.roborally.model.fieldActions;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Space;

public class StartPlace extends FieldAction{
    @Override
    public boolean doAction(GameController gameController, Space space) {
        return false;
    }
}
