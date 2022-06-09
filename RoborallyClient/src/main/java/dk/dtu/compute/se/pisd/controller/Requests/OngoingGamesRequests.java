package dk.dtu.compute.se.pisd.controller.Requests;

public class OngoingGamesRequests {

    private String boardName;
    private String hostName;
    private int currentPlayers;

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
}
