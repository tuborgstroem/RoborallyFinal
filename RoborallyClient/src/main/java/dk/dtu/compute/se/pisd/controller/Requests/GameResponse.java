package dk.dtu.compute.se.pisd.controller.Requests;

/**
 * @author Tobias Borgstrøm s184810
 */
public class GameResponse {

    private String boardName;
    private String hostName;
    private int currentPlayers;

    private String gameId;
    private int maxPlayers;

    /**
     * @return board name
     */
    public String getBoardName() {
        return boardName;
    }

    /**
     * @return host name
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * @return number of current players
     */
    public int getCurrentPlayers() {
        return currentPlayers;
    }

    /**
     * @return max number of players
     */
    public int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * @return string to use in dialog for choosing a game
     */
    public String toDialogString(){
        return hostName + "'s game on " + boardName + " with " + currentPlayers + "/ " + maxPlayers+ " players";
    }


    public String getGameId() {
        return gameId;
    }
}
