/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package dk.dtu.compute.se.pisd.view;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.model.Heading;
import dk.dtu.compute.se.pisd.model.Player;
import dk.dtu.compute.se.pisd.model.Space;
import dk.dtu.compute.se.pisd.model.fieldActions.ConveyorBelt;
import dk.dtu.compute.se.pisd.model.fieldActions.FieldAction;
import dk.dtu.compute.se.pisd.model.fieldActions.StartPlace;
import dk.dtu.compute.se.pisd.view.boardElements.PlayerIcon;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class SpaceView extends StackPane implements ViewObserver {

    final public static int SPACE_HEIGHT = 60; // 75;
    final public static int SPACE_WIDTH = 60; // 75;

    private Pane pane;

    private int spaceAngle = 0;
    public final Space space;


    public SpaceView(@NotNull Space space) {
        this.pane = new Pane();
        this.space = space;

        // XXX the following styling should better be done with styles
        this.setPrefWidth(SPACE_WIDTH);
        this.setMinWidth(SPACE_WIDTH);
        this.setMaxWidth(SPACE_WIDTH);

        this.setPrefHeight(SPACE_HEIGHT);
        this.setMinHeight(SPACE_HEIGHT);
        this.setMaxHeight(SPACE_HEIGHT);


        this.setStyle("-fx-background-image: url('pictures/tile.jpg'); -fx-background-size: " + SPACE_HEIGHT + " " + SPACE_WIDTH + ";");


        Rectangle rectangle =
                new Rectangle(0.0, 0.0, SPACE_WIDTH, SPACE_HEIGHT);
        rectangle.setFill(Color.TRANSPARENT);

        updatePlayer();

        // This space view should listen to changes of the space
        space.attach(this);
    }

    private void updatePlayer() {
        this.getChildren().clear();
        pane.getChildren().clear();

        if(space.getActions() != null){
            List<FieldAction> actions = space.getActions();
            for (FieldAction action : actions){
                if(action instanceof ConveyorBelt conveyorBelt){
                    this.spaceAngle = switch (conveyorBelt.getHeading()){
                        case EAST -> 0;
                        case SOUTH -> 90;
                        case WEST -> 180;
                        case NORTH -> 270;
                    };
                    this.setStyle("-fx-background-image: url('pictures/conveyourBelt.png'); -fx-background-size: " + SPACE_HEIGHT + " " + SPACE_WIDTH + "; -fx-rotate: " + spaceAngle + ";");
                }
                if(action instanceof StartPlace){
                    this.setStyle("-fx-background-image: url('pictures/startpoint.png'); -fx-background-size: "
                            + SPACE_HEIGHT + " " + SPACE_WIDTH + ";");

                }
            }
        }

        if((space.getWalls() != null)){
            ImagePattern imagePattern = new ImagePattern(new Image("pictures/wall.png"));
            for(Heading heading: space.getWalls()){
                Rectangle rectangle = new Rectangle(0.0, 0.0, SPACE_WIDTH, SPACE_HEIGHT);
                rectangle.setFill(imagePattern);

                int angle = switch (heading){
                    case SOUTH -> 0;
                    case WEST -> 90;
                    case NORTH -> 180;
                    case EAST -> 270;
                };
                rectangle.setRotate(angle-this.spaceAngle);
                this.getChildren().add(rectangle);
                //Line line = createWall(heading);
                //pane.getChildren().add(line);
            }
        }

        Player player = space.getPlayer();
        if (player != null) {
            String mechName = switch (player.getName()){
                case ("Player 1") -> "pictures/Mech1.png";
                case ("Player 2") -> "pictures/Mech2.png";
                case ("Player 3") -> "pictures/Mech3.png";
                case ("Player 4") -> "pictures/Mech4.png";
                case ("Player 5") -> "pictures/Mech5.png";
                case ("Player 6") -> "pictures/Mech6.png";
                default -> "pictures/wall.png";
            };

            ImagePattern imagePattern = new ImagePattern(new Image(mechName));

            Rectangle rectangle = new Rectangle(0.0, 0.0, SPACE_WIDTH-20, SPACE_HEIGHT-20);
            rectangle.setFill(imagePattern);

            int angle = switch (player.getHeading()){
                case NORTH -> 0;
                case EAST -> 90;
                case SOUTH -> 180;
                case WEST -> 270;

            };
            rectangle.setRotate(angle-this.spaceAngle);
            this.getChildren().add(rectangle);
            //Line line = createWall(heading);
            //pane.getChildren().add(line);


            //Polygon arrow = PlayerIcon.draw(pane, player, SPACE_WIDTH);

            //this.getChildren().add(arrow);
        }

        this.getChildren().add(pane);



    }

    @Override
    public void updateView(Subject subject) {
        if (subject == this.space) {
            updatePlayer();
        }
    }


    public Line createWall(Heading heading) {
        Line line = null;
        switch (heading){
            case SOUTH -> line = new Line(2, SPACE_HEIGHT-2, SPACE_WIDTH-2,
                    SPACE_HEIGHT-2);

            case NORTH ->
                line = new Line(SPACE_HEIGHT-2, 2, 2,
                        2);
            case WEST ->
                 line = new Line (2, 2, 2,
                        SPACE_WIDTH -2);
            case EAST ->
                 line = new Line (SPACE_HEIGHT -2, SPACE_HEIGHT-2, SPACE_HEIGHT -2,
                        2);
        }

        if (line!= null) {
            line.setStroke(Color.DARKBLUE);
            line.setStrokeWidth(5);
        }
        return line;
    }
}
