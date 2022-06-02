package dk.dtu.compute.se.pisd.roborally.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    Player player;
    CommandCard card;
    int numberCards = 19;
    @BeforeEach
    void setup(){
        Board board = new Board(3,3);
        player = new Player(board, "red", "john");
    }

    @Test
    void generateProgrammingDeck() {
        assertEquals(player.getProgrammingDeck().size(), numberCards);
    }

    @Test
    void drawCard() {
        card = player.drawCard();
        numberCards--;
        assertEquals(numberCards, player.getProgrammingDeck().size());
    }

    @Test
    void discardCards() {
        ArrayList<CommandCard> cards = new ArrayList<>();
        cards.add(card);
        player.discardCards(cards);
        assertEquals(player.getDiscardPile().size(), cards.size());

    }
}