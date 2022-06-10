package dk.dtu.compute.se.pisd.roborally.controller.gameRequests;

public class OngoingGameResponse {


    private String boardName;
    private String hostName;
    private int currentPlayers;
    private int maxPlayers;

    private String gameId;

    public OngoingGameResponse(String boardName, String hostName, int currentPlayers, int maxPlayers, String gameId){
        this.boardName = boardName;
        this.hostName = hostName;
        this.currentPlayers = currentPlayers;
        this.maxPlayers = maxPlayers;
        this.gameId = gameId;
    }

    public String getId() {
        return gameId;
    }

    public boolean isFull(){
        return currentPlayers >= maxPlayers;
    }
}
