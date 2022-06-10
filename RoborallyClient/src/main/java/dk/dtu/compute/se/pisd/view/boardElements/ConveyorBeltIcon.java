package dk.dtu.compute.se.pisd.view.boardElements;

import dk.dtu.compute.se.pisd.model.fieldActions.FieldAction;
import dk.dtu.compute.se.pisd.model.fieldActions.ConveyorBelt;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class ConveyorBeltIcon {
    /**
     *  Creates Conveyor belt Icon view's size and rotation
     * @param action
     * @param width
     * @return arrow
     */
    public static Polygon draw(FieldAction action, double width){
        Polygon arrow = new Polygon(width/6, width/6,
                width/2    , 5*(width/6),
                5*(width/6), width/6 );

        try {
            arrow.setFill(Color.LIGHTGRAY);
        } catch (Exception e) {
            arrow.setFill(Color.MEDIUMPURPLE);
        }
        arrow.setRotate((90*((ConveyorBelt) action).getHeading().ordinal())%360);
        return arrow;
    }
}
