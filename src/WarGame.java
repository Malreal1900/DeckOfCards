import java.util.ArrayList;
import java.util.List;

// Full War game using Deck, Card, Hand
public class WarGame {

    private Hand player1;
    private Hand player2;
    private Deck deck;

    public WarGame() {
        deck = new Deck();
        deck.shuffle();

        player1 = new Hand();
        player2 = new Hand();

        // Deal cards evenly to players
        dealCards();
    }

    // Deal the deck evenly between two players
    private void dealCards() {
        List<Card> allCards = new ArrayList<>();
        Card card;
        while ((card = deck.drawCard()) != null) {
            allCards.add(card);
        }

        for (int i = 0; i < allCards.size(); i++) {
            if (i % 2 == 0) {
                player1.addCard(allCards.get(i));
            } else {
                player2.addCard(allCards.get(i));
            }
        }
    }

    // Play the game until one player wins
    public void playGame() {
        int round = 1;

        while (!player1.getCards().isEmpty() && !player2.getCards().isEmpty()) {
            System.out.println("Round " + round);
            playRound();
            round++;
            System.out.println("Player 1 cards: " + player1.getCards().size());
            System.out.println("Player 2 cards: " + player2.getCards().size());
            System.out.println("-----------------------------------");
        }

        if (player1.getCards().isEmpty()) {
            System.out.println("Player 2 wins the game!");
        } else {
            System.out.println("Player 1 wins the game!");
        }
    }

    // Play a single round
    private void playRound() {
        Card p1Card = player1.getCards().remove(0);
        Card p2Card = player2.getCards().remove(0);

        System.out.println("Player 1 plays: " + p1Card);
        System.out.println("Player 2 plays: " + p2Card);

        List<Card> spoils = new ArrayList<>();
        spoils.add(p1Card);
        spoils.add(p2Card);

        if (p1Card.getValue() > p2Card.getValue()) {
            player1.getCards().addAll(spoils);
            System.out.println("Player 1 wins the round!");
        } else if (p1Card.getValue() < p2Card.getValue()) {
            player2.getCards().addAll(spoils);
            System.out.println("Player 2 wins the round!");
        } else {
            System.out.println("WAR!");
            handleWar(spoils);
        }
    }

    // Handle war (tie)
    private void handleWar(List<Card> spoils) {
        // Check if players have enough cards for war
        if (player1.getCards().size() < 4) {
            player2.getCards().addAll(spoils);
            player2.getCards().addAll(player1.getCards());
            player1.clearHand();
            return;
        } 
        if (player2.getCards().size() < 4) {
            player1.getCards().addAll(spoils);
            player1.getCards().addAll(player2.getCards());
            player2.clearHand();
            return;
        }

        // Each player puts 3 cards face-down
        List<Card> p1War = new ArrayList<>();
        List<Card> p2War = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            p1War.add(player1.getCards().remove(0));
            p2War.add(player2.getCards().remove(0));
        }

        spoils.addAll(p1War);
        spoils.addAll(p2War);

        // Each player plays one card face-up
        Card p1Card = player1.getCards().remove(0);
        Card p2Card = player2.getCards().remove(0);

        System.out.println("War cards: Player 1 plays " + p1Card + ", Player 2 plays " + p2Card);

        spoils.add(p1Card);
        spoils.add(p2Card);

        if (p1Card.getValue() > p2Card.getValue()) {
            player1.getCards().addAll(spoils);
            System.out.println("Player 1 wins the war!");
        } else if (p1Card.getValue() < p2Card.getValue()) {
            player2.getCards().addAll(spoils);
            System.out.println("Player 2 wins the war!");
        } else {
            // War again recursively
            handleWar(spoils);
        }
    }

    // Main method to start the game
    public static void main(String[] args) {
        WarGame game = new WarGame();
        game.playGame();
    }
}
