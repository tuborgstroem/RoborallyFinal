package dk.dtu.compute.se.pisd.controller.Requests;

import dk.dtu.compute.se.pisd.model.Player;

/**
 * @author Tobias Borgstrøm s184810
 */
public class AddPlayerResponse {
    boolean allPlayersIn;
    Player player;

    /**
     * constructor
     * @param allPlayersIn boolean
     * @param player player
     */
    public AddPlayerResponse(boolean allPlayersIn, Player player){
        this.player = player;
        this.allPlayersIn = allPlayersIn;
    }

    public Player getPlayer() {
        return player;
    }
}
