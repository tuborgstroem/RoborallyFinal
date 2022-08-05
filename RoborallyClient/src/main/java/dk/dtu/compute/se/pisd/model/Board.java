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
package dk.dtu.compute.se.pisd.model;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.model.dataModels.PlayerData;
import dk.dtu.compute.se.pisd.model.fieldActions.Checkpoint;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static dk.dtu.compute.se.pisd.model.Phase.INITIALISATION;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class Board extends Subject {

    public final int width;

    public final int height;

    public final String boardName;

    private Integer gameId;

    private List<Checkpoint> checkpoints = new ArrayList<>();

    private final Space[][] spaces;

    private final List<Player> players = new ArrayList<>();

    private Player current;

    private Phase phase = INITIALISATION;

    private int step = 0;

    private Board startBoard;
    private Heading startSide;
    private boolean stepMode;
    private int NOCheckpoints = 0;

    /**
     * Constructor for creating a board
     *
     * @param width     how wide should the board be
     * @param height    how high should the board be
     * @param boardName board name
     */
    public Board(int width, int height, @NotNull String boardName, @Nullable Board startBoard) {
        this.boardName = boardName;
        this.width = width;
        this.height = height;
        if (startBoard != null) {
            this.startBoard = startBoard;
        }
        spaces = new Space[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Space space = new Space(this, x, y);
                spaces[x][y] = space;
            }
        }
        this.stepMode = false;
    }

    /**
     * @return the startboard
     */
    public Board getStartBoard() {
        return startBoard;
    }

    /**
     * @param startBoard the startboard
     */
    public void setStartBoard(Board startBoard) {
        this.startBoard = startBoard;
    }

    /**
     * Create default board
     *
     * @param width  width
     * @param height height
     */
    public Board(int width, int height) {
        this(width, height, "defaultboard", null);
    }

    /**
     * @return GameId
     */
    public Integer getGameId() {
        return gameId;
    }

    /**
     * @return spaces
     */
    public Space[][] getSpaces() {
        return spaces;
    }

    /**
     * @param gameId the gameId
     */
    public void setGameId(int gameId) {
        if (this.gameId == null) {
            this.gameId = gameId;
        } else {
            if (!this.gameId.equals(gameId)) {
                throw new IllegalStateException("A game with a set id may not be assigned a new id!");
            }
        }
    }

    /**
     * Moves player in the direction of heading
     * If there is a NeighbourPlayer on the tile move them ot next tile in the current heading of player
     *
     * @param player  the player
     * @param heading players heading
     * @throws InvalidMoveException
     */
    public void movePlayer(@NotNull Player player, Heading heading) throws InvalidMoveException {
        Space space = player.getSpace();
        Space target;
        if (player != null && player.board == this && space != null) {
            try {
                target = getNeighbour(space, heading);
            } catch (InvalidMoveException e) {
                throw new InvalidMoveException();
            }
            if (target != null) {
                Player neighbourPlayer = target.getPlayer();
                if (neighbourPlayer != null && neighbourPlayer != player) {
                    try {
                        movePlayer(neighbourPlayer, heading);
                    } catch (InvalidMoveException e) {
                        target = player.getSpace();
                    }
                }
                target.setPlayer(player);
            }
        }
    }

    /**
     * Gets space from coordinates
     *
     * @param x x coordinate
     * @param y y coordinate
     * @return the space if found else null
     */
    public Space getSpace(int x, int y) {
        if (x >= 0 && x < width &&
                y >= 0 && y < height) {
            return spaces[x][y];
        } else {
            return null;
        }
    }

    /**
     * @return number of players
     */
    public int getPlayersNumber() {
        return players.size();
    }

    public List<Player> getPlayers() {
        return players;
    }

    /**
     * add a player
     *
     * @param player the player
     */


    public void addPlayer(@NotNull Player player) {
        if (player.board == this && !players.contains(player)) {
            players.add(player);
            notifyChange();
        }
    }

    /**
     * Get player from their number
     *
     * @param i the number
     * @return the player or null
     */
    public Player getPlayer(int i) {
        if (i >= 0 && i < players.size()) {
            return players.get(i);
        } else {
            return null;
        }
    }

    /**
     * @return current player
     */
    public Player getCurrentPlayer() {
        return current;
    }

    /**
     * @param player the new current player
     */
    public void setCurrentPlayer(Player player) {
        if (player != this.current && players.contains(player)) {
            this.current = player;
            notifyChange();
        }
    }

    /**
     * @return the current phase
     */
    public Phase getPhase() {
        return phase;
    }

    /**
     * @param phase set the phase to this
     */
    public void setPhase(Phase phase) {
        if (phase != this.phase) {
            this.phase = phase;
            notifyChange();
        }
    }

    /**
     * @return the current step
     */
    public int getStep() {
        return step;
    }

    /**
     * @param step set to this step
     */
    public void setStep(int step) {
        if (step != this.step) {
            this.step = step;
            notifyChange();
        }
    }

    /**
     * @return is it in stepmode
     */
    public boolean isStepMode() {
        return stepMode;
    }

    /**
     * @param stepMode set stepmode
     */
    public void setStepMode(boolean stepMode) {
        if (stepMode != this.stepMode) {
            this.stepMode = stepMode;
            notifyChange();
        }
    }

    /**
     * get playerNumber
     *
     * @param player the player
     * @return their number or -1 if not found
     */
    public int getPlayerNumber(@NotNull Player player) {
        if (player.board == this) {
            return players.indexOf(player);
        } else {
            return -1;
        }
    }

    /**
     * Returns the neighbour of the given space of the board in the given heading.
     * The neighbour is returned only, if it can be reached from the given space
     * (no walls or obstacles in either of the involved spaces); otherwise,
     * null will be returned.
     *
     * @param space   the space for which the neighbour should be computed
     * @param heading the heading of the neighbour
     * @return the space in the given direction; null if there is no (reachable) neighbour
     */
    public Space getNeighbour(@NotNull Space space, @NotNull Heading heading) throws InvalidMoveException {
        int x = space.x;
        int y = space.y;
        List<Heading> walls = space.getWalls();
        if (walls.contains(heading)) {
            throw new InvalidMoveException();
        }
        switch (heading) {
            case SOUTH:
                y = (y + 1) % height;
                break;
            case WEST:
                x = (x + width - 1) % width;
                break;
            case NORTH:
                y = (y + height - 1) % height;
                break;
            case EAST:
                x = (x + 1) % width;
                break;
        }

        Space target = getSpace(x, y);
        if (target.getWalls().contains(heading.next().next())) {
            throw new InvalidMoveException();
        }
        return target;
    }

    /**
     * @return a status message
     */
    public String getStatusMessage() {
        // this is actually a view aspect, but for making assignment V1 easy for
        // the students, this method gives a string representation of the current
        // status of the game

        // XXX: V2 changed the status so that it shows the phase, the player and the step
        return "Phase: " + getPhase().name() +
                ", Player = " + getCurrentPlayer().getName() +
                ", Step: " + getStep();
    }

    public int getNOCheckpoints() {
        return NOCheckpoints;
    }

    public void CheckpointsArray(@NotNull Checkpoint checkpoint) {
        int i = 0;
        while (i < 3) {
            checkpoints.add(i, checkpoint);
            notifyChange();
            i++;
        }
    }
    public void setNOCheckpoints ( int NOCheckpoints){
        if (this.NOCheckpoints == 0) {
            this.NOCheckpoints = NOCheckpoints;
        }
    }

    public List<Checkpoint> getCheckpoints () {
        return this.checkpoints;
    }

    public void updatePlayer(ArrayList<PlayerData> players) {
        if (players != null) {
            for (PlayerData data : players) {
                if (data.getSpace() != null) {
                    for (Player player : this.players) {
                        if (data.getName().equals(player.getName()) && (data.getColor().equals(player.getColor()))) {
                            data.updatePlayer(player);
                        }
                    }
                }
            }
        }
    }
}
