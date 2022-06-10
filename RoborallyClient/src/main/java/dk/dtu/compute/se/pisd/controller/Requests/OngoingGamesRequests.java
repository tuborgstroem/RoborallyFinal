package dk.dtu.compute.se.pisd.controller.Requests;

public class OngoingGamesRequests {

    private String boardName;
    private String hostName;
    private int currentPlayers;

    private String gameId;
    private int maxPlayers;

    public String getBoardName() {
        return boardName;
    }

    public String getHostName() {
        return hostName;
    }

    public int getCurrentPlayers() {
        return currentPlayers;
    }
    public int getMaxPlayers() {
        return maxPlayers;
    }

    public String toDialogString(){
        return hostName + "'s game on " + boardName + " with " + currentPlayers + "/ " + maxPlayers+ " players";
    }


    public String getGameId() {
        return gameId;
    }
}
