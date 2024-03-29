package dk.dtu.compute.se.pisd.roborally.model.gameRequests;

/**
 * @Author Tobias Borgstrøm s184810
 */
public class GameResponse {


    private final String boardName;
    private final String hostName;
    private int currentPlayers;
    private final int maxPlayers;

    private final String gameId;

    /**
     * Constructor for this Response
     * @param boardName name of the board
     * @param hostName name of the host
     * @param currentPlayers number of players currently in game
     * @param maxPlayers number of players that should be in game
     * @param gameId id of the game
     */
    public GameResponse(String boardName, String hostName, int currentPlayers, int maxPlayers, String gameId){
        this.boardName = boardName;
        this.hostName = hostName;
        this.currentPlayers = currentPlayers;
        this.maxPlayers = maxPlayers;
        this.gameId = gameId;
    }

    /**
     * @return the id of the game
     */
    public String getId() {
        return gameId;
    }

    /**
     * @return true if the game is full
     */
    public boolean isFull(){
        return currentPlayers >= maxPlayers;
    }
}
