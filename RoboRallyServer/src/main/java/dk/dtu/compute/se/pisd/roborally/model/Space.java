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
package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.model.fieldActions.ConveyorBelt;
import dk.dtu.compute.se.pisd.roborally.model.fieldActions.FieldAction;

import java.util.ArrayList;
import java.util.List;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class Space extends Subject {

//    public final Board board;

    public final int x;
    public final int y;

    private List<Heading> walls = new ArrayList<>();
    private List<FieldAction> actions = new ArrayList<>();

//    private Player player;

    /**
     * constructure of space
     * @param x x coordinate
     * @param y y coordinate
     */
    public Space( int x, int y) {
//        this.board = board;
        this.x = x;
        this.y = y;
//        player = null;
    }

    /**
     * @return player on the field
     */
//    public Player getPlayer() {
//        return player;
//    }

    /**
     * @param player new player on field
     */
//    public void setPlayer(Board board, Player player) {
////        Player oldPlayer = this.player;
////        if (player != oldPlayer && player == null) {
////            this.player = player;
////            if (oldPlayer != null) {
//                // this should actually not happen
////                oldPlayer.setSpace(null, board);
//            }
//            if (player != null) {
//                player.setSpace(this, board);
//            }
//            notifyChange();
//        }
//    }

    /**
     * notify that player changed
     */
    void playerChanged() {
        // This is a minor hack; since some views that are registered with the space
        // also need to update when some player attributes change, the player can
        // notify the space of these changes by calling this method.
        notifyChange();
    }

    public List<Heading> getWalls() {
        return walls;
    }

    public List<FieldAction> getActions() {
        return actions;
    }

}
