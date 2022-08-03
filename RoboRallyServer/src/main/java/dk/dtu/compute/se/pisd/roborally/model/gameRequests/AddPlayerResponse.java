package dk.dtu.compute.se.pisd.roborally.model.gameRequests;

import dk.dtu.compute.se.pisd.roborally.model.Player;

/**
 * @Author Tobias Borgstrøm s184810
 */
public class AddPlayerResponse {
    Player player;

    /**
     * Adds a player to the AddPlayerResponse
     * @param player
     */
    public AddPlayerResponse(Player player){
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
