package dk.dtu.compute.se.pisd.roborally.view.boardElements;

import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.fieldActions.ConveyorBelt;
import dk.dtu.compute.se.pisd.roborally.model.fieldActions.FieldAction;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import javax.swing.*;

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
