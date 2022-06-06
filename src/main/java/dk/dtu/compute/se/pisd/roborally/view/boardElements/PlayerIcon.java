package dk.dtu.compute.se.pisd.roborally.view.boardElements;

import dk.dtu.compute.se.pisd.roborally.model.Player;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class PlayerIcon {
    public static Polygon draw(Pane pane, Player player, double width) {

        Polygon arrow = new Polygon(0.0, 0.0,
                10.0, 20.0,
                20.0, 0.0);
        try {
            arrow.setFill(Color.valueOf(player.getColor()));
        } catch (Exception e) {
            arrow.setFill(Color.MEDIUMPURPLE);
        }

        arrow.setRotate((90*player.getHeading().ordinal())%360);
        return arrow;
    }
}
