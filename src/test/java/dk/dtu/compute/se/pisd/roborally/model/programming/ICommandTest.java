package dk.dtu.compute.se.pisd.roborally.model.programming;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ICommandTest {


    @Test
    void doMoveAction() {
        Board board = new Board(2,2);
        Player player = new Player(board, "Red", "test");
        board.addPlayer(player);
        board.getSpace(0,0).setPlayer(player);
        Space space = player.getSpace();
        Space targetedSpace = space.board.getNeighbour(space, player.getHeading());
        MoveCard moveCard = new MoveCard("Move", 1);
        moveCard.doAction(player, board);

        assertEquals(player.getSpace(), targetedSpace);
    }

    @Test
    void doTurnAction() {
        Board board = new Board(2,2);
        Player player = new Player(board, "Red", "test");
        board.addPlayer(player);
        Heading heading = player.getHeading();
        TurnCard card = new TurnCard("", 1);
        card.doAction(player, board);

        assertEquals(player.getHeading(), heading.next());
    }
    @Test
    void doAgainAction() {

        Board board = new Board(2,2);
        Player player = new Player(board, "Red", "test");
        board.addPlayer(player);
        Heading heading = player.getHeading();
        TurnCard card = new TurnCard("", 1);
        AgainCard againCard = new AgainCard("");
        card.doAction(player, board);
        player.setPreviousCommand(card);
        againCard.doAction(player, board);

        assertEquals(player.getHeading(), heading.next().next());
    }

    @Test
    void getInstance() {

        ICommand card = ICommand.getInstance(ICommand.Command.OPTION_WEASEL, 0);

        assertEquals(card.getOptions().size(), 3);

        ICommand card2 = ICommand.getInstance(ICommand.Command.OPTION_SANDBOX, 0);

        assertEquals(card2.getOptions().size(), 7);
    }
}