package dk.dtu.compute.se.pisd.model.dataModels;

import dk.dtu.compute.se.pisd.model.Heading;
import dk.dtu.compute.se.pisd.model.Space;
import dk.dtu.compute.se.pisd.model.fieldActions.FieldAction;

import java.util.ArrayList;
import java.util.List;

public class SpaceData {

    public final int x;
    public final int y;

    private List<Heading> walls = new ArrayList<>();
    private List<FieldAction> actions = new ArrayList<>();

    public SpaceData(Space space) {
        this.x = space.x;
        this.y = space.y;
        this.actions = space.getActions();
        this.walls = space.getWalls();

    }
}
