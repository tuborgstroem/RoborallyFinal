package dk.dtu.compute.se.pisd.view.boardElements;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class StartPlaceIcon {
    public static Circle draw(double width) {
        Circle circle = new Circle(width/2, width/2, (5*(width/6))/2);
        try {
            circle.setFill(Color.WHITE);
            circle.setStroke(Color.BLACK);
        } catch (Exception e) {
            circle.setFill(Color.MEDIUMPURPLE);
        }

        return circle;
    }
}
