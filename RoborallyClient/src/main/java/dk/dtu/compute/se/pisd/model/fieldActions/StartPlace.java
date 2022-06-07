package dk.dtu.compute.se.pisd.model.fieldActions;

import dk.dtu.compute.se.pisd.controller.GameController;
import dk.dtu.compute.se.pisd.model.Space;

public class StartPlace extends FieldAction{
    @Override
    public boolean doAction(GameController gameController, Space space) {
        return false;
    }
}
