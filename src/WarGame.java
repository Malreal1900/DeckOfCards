import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WarGame {

    private Hand player1;
    private Hand player2;
    private Hand player1WinPile;
    private Hand player2WinPile;
    private Deck deck;
    private List<Card> spoils;
    private String lastRoundMessage;
    private boolean warTriggered;

    public WarGame() {
        deck = new Deck();
        deck.shuffle();

        player1 = new Hand();
        player2 = new Hand();
        player1WinPile = new Hand();
        player2WinPile = new Hand();
        spoils = new ArrayList<Card>();
        warTriggered = false;
        lastRoundMessage = "";
    }

    public void splitDeck(int p1Count) {
        deck.shuffle();

        player1.clearHand();
        player2.clearHand();
        player1WinPile.clearHand();
        player2WinPile.clearHand();

        for (int i = 0; i < 52; i++) {
            Card card = deck.drawCard();
            if (i < p1Count) {
                player1.addCard(card);
            } else {
                player2.addCard(card);
            }
        }

        warTriggered = false;
    }

    public Card[] playRound() {
        refillHandsIfEmpty();

        if (isGameOver() == true) {
            return new Card[0];
        }

        spoils.clear();

        Card p1Card = player1.drawTopCard();
        Card p2Card = player2.drawTopCard();

        spoils.add(p1Card);
        spoils.add(p2Card);

        if (p1Card.getValue() > p2Card.getValue()) {
            player1WinPile.addCards(spoils);
            lastRoundMessage = "Player 1 wins the round with " + p1Card + " beating " + p2Card + "!";
            warTriggered = false;
        } else if (p1Card.getValue() < p2Card.getValue()) {
            player2WinPile.addCards(spoils);
            lastRoundMessage = "Player 2 wins the round with " + p2Card + " beating " + p1Card + "!";
            warTriggered = false;
        } else {
            warTriggered = true;
            lastRoundMessage = "Tie! WAR triggered!";
        }

        Card[] roundCards = new Card[2];
        roundCards[0] = p1Card;
        roundCards[1] = p2Card;
        return roundCards;
    }

    public void resolveWar() {
        if (warTriggered == false) {
            return;
        }

        List<Card> p1War = drawWarCards(player1, player1WinPile);
        List<Card> p2War = drawWarCards(player2, player2WinPile);

        for (int i = 0; i < p1War.size(); i++) {
            spoils.add(p1War.get(i));
        }

        for (int i = 0; i < p2War.size(); i++) {
            spoils.add(p2War.get(i));
        }

        // Check if player is out of cards
        if (player1.getCards().isEmpty() && player1WinPile.getCards().isEmpty()) {
            player2WinPile.addCards(spoils);
            lastRoundMessage = "Player 2 wins the war because Player 1 ran out of cards!";
        } else if (player2.getCards().isEmpty() && player2WinPile.getCards().isEmpty()) {
            player1WinPile.addCards(spoils);
            lastRoundMessage = "Player 1 wins the war because Player 2 ran out of cards!";
        } else {
            Card p1Final = drawFinalWarCard(player1, player1WinPile);
            Card p2Final = drawFinalWarCard(player2, player2WinPile);

            if (p1Final != null) {
                spoils.add(p1Final);
            }

            if (p2Final != null) {
                spoils.add(p2Final);
            }

            if (p1Final != null && p2Final != null) {
                if (p1Final.getValue() > p2Final.getValue()) {
                    player1WinPile.addCards(spoils);
                } else if (p1Final.getValue() < p2Final.getValue()) {
                    player2WinPile.addCards(spoils);
                } else {
                    resolveWar();
                    return;
                }

                lastRoundMessage = "WAR resolved: Player 1 " + p1Final + " vs Player 2 " + p2Final;
            }
        }

        warTriggered = false;
    }

    // Compare final war card
    private Card drawFinalWarCard(Hand player, Hand winPile) {
        if (player.getCards().isEmpty() && winPile.getCards().isEmpty() == false) {
            Collections.shuffle(winPile.getCards());
            player.addCards(winPile.getCards());
            winPile.clearHand();
        }

        if (player.getCards().isEmpty() == false) {
            return player.drawTopCard();
        } else {
            return null;
        }
    }

    // Final war card for a player, refill from win pile if needed
    private List<Card> drawWarCards(Hand player, Hand winPile) {
        List<Card> warCards = new ArrayList<Card>();

        for (int i = 0; i < 3; i++) {
            if (player.getCards().isEmpty() && winPile.getCards().isEmpty() == false) {
                Collections.shuffle(winPile.getCards());
                player.addCards(winPile.getCards());
                winPile.clearHand();
            }

            if (player.getCards().isEmpty() == false) {
                Card c = player.drawTopCard();
                warCards.add(c);
            } else {
                break; // No more cards
            }
        }

        return warCards;
    }

    // Reefill hands if empty from win piles
    private void refillHandsIfEmpty() {
        refillHandIfEmpty(player1, player1WinPile);
        refillHandIfEmpty(player2, player2WinPile);
    }

    private void refillHandIfEmpty(Hand hand, Hand winPile) {
        if (hand.getCards().isEmpty() && winPile.getCards().isEmpty() == false) {
            Collections.shuffle(winPile.getCards());
            hand.addCards(winPile.getCards());
            winPile.clearHand();
        }
    }

    public void resetGame() {
        player1.clearHand();
        player2.clearHand();
        player1WinPile.clearHand();
        player2WinPile.clearHand();

        deck = new Deck();
        deck.shuffle();

        spoils.clear();
        warTriggered = false;
        lastRoundMessage = "";
    }

    // Getters for game state
    public boolean isWarTriggered() {
        return warTriggered;
    }

    public boolean isGameOver() {
        boolean p1Dead = player1.getCards().isEmpty() && player1WinPile.getCards().isEmpty();
        boolean p2Dead = player2.getCards().isEmpty() && player2WinPile.getCards().isEmpty();

        if (p1Dead == true || p2Dead == true) {
            return true;
        } else {
            return false;
        }
    }

    public Card[] getWarCards() {
        if (spoils.size() >= 2) {
            Card[] war = new Card[2];
            war[0] = spoils.get(spoils.size() - 2);
            war[1] = spoils.get(spoils.size() - 1);
            return war;
        } else {
            return new Card[0];
        }
    }

    public String getLastRoundMessage() {
        return lastRoundMessage;
    }

    public int getPlayer1Count() {
        return player1.getCards().size();
    }

    public int getPlayer2Count() {
        return player2.getCards().size();
    }

    public int getPlayer1WinCount() {
        return player1WinPile.getCards().size();
    }

    public int getPlayer2WinCount() {
        return player2WinPile.getCards().size();
    }

    public String getWinnerMessage() {
        int totalP1 = player1.getCards().size() + player1WinPile.getCards().size();
        int totalP2 = player2.getCards().size() + player2WinPile.getCards().size();

        if (totalP1 > totalP2) {
            return "Player 1 won the previous game with " + totalP1 + " cards!";
        } else if (totalP2 > totalP1) {
            return "Player 2 won the previous game with " + totalP2 + " cards!";
        } else if ((totalP1 + totalP2) > 0) {
            return "The previous game ended in a tie!";
        } else {
            return "No previous game played yet.";
        }
    }
}