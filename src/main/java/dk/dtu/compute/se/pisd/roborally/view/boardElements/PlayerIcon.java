package dk.dtu.compute.se.pisd.roborally.view.boardElements;

import dk.dtu.compute.se.pisd.roborally.model.Player;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class PlayerIcon {
    public static Polygon draw(Pane pane, Player player, double width) {

        Polygon arrow = new Polygon(width/3, width/3,
                width-(width/3), width/3,
                (width/2), 4*(width/6));
        try {
            arrow.setFill(Color.valueOf(player.getColor()));
        } catch (Exception e) {
            arrow.setFill(Color.MEDIUMPURPLE);
        }

        arrow.setRotate((90*player.getHeading().ordinal())%360);
        return arrow;
    }
}
