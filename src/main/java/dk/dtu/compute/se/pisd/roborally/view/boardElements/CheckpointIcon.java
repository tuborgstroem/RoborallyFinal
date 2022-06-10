package dk.dtu.compute.se.pisd.roborally.view.boardElements;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * @author Kasper s215792
 * @author Magnus s204447
 */
public class CheckpointIcon {
    /**
     * Creates checkpoint Icon size and color
     * @param width how wide the circle should be
     * @return circle
     */
    public static Circle draw(double width) {
        Circle circle = new Circle(width/2, width/2, (5*(width/6))/2);
        try {
            circle.setFill(Color.BLACK);
            circle.setStroke(Color.WHITE);
        } catch (Exception e) {
            circle.setFill(Color.MEDIUMPURPLE);
        }

        return circle;
    }
}
