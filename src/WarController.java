import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WarController {

    private final DeckView view;

    private List<Card> player1; // Player 1's hand
    private List<Card> player2; // Player 2's hand
    private List<Card> player1WinPile; // Cards won by Player 1
    private List<Card> player2WinPile; // Cards won by Player 2

    private List<Card> spoils; // cards at stake during a round

    public WarController(DeckView view) {
        this.view = view;
        startNewGame();
        connectActions();
    }

    // Starts a new game: full deck, shuffle, split between players
    private void startNewGame() {
        Deck deck = new Deck(); // full deck
        deck.shuffle();

        player1 = new ArrayList<>();
        player2 = new ArrayList<>();
        player1WinPile = new ArrayList<>();
        player2WinPile = new ArrayList<>();

        // split the deck evenly
        boolean turn = true;
        Card card;
        while ((card = deck.drawCard()) != null) {
            if (turn)
                player1.add(card);
            else
                player2.add(card);
            turn = !turn;
        }

        view.getCardOutput().setText("New War game started!");
        view.getPlayer1CardImage().setImage(null);
        view.getPlayer2CardImage().setImage(null);
        updateDeckSizes();
    }

    private void connectActions() {
        view.getDrawButton().setOnAction(e -> playRound());
        view.getShuffleButton().setOnAction(e -> startNewGame());
    }

    // Ensure the player has cards in hand, refill from winPile if empty
    private void checkAndRefillHands() {
        if (player1.isEmpty() && !player1WinPile.isEmpty()) {
            Collections.shuffle(player1WinPile);
            player1.addAll(player1WinPile);
            player1WinPile.clear();
        }
        if (player2.isEmpty() && !player2WinPile.isEmpty()) {
            Collections.shuffle(player2WinPile);
            player2.addAll(player2WinPile);
            player2WinPile.clear();
        }
    }

    private void playRound() {
        checkAndRefillHands();

        if (player1.isEmpty()) {
            view.getCardOutput().setText("Player 2 wins the game!");
            return;
        }
        if (player2.isEmpty()) {
            view.getCardOutput().setText("Player 1 wins the game!");
            return;
        }

        spoils = new ArrayList<>();
        Card p1Card = player1.remove(0);
        Card p2Card = player2.remove(0);

        spoils.add(p1Card);
        spoils.add(p2Card);

        String message = "Player 1 plays: " + p1Card + " | Player 2 plays: " + p2Card + "\n";

        if (p1Card.getValue() > p2Card.getValue()) {
            player1WinPile.addAll(spoils);
            message += "Player 1 wins the round!";
        } else if (p1Card.getValue() < p2Card.getValue()) {
            player2WinPile.addAll(spoils);
            message += "Player 2 wins the round!";
        } else {
            message += "WAR!";
            handleWar(message);
            return;
        }

        view.getCardOutput().setText(message);
        updateDeckSizes();
        showTopCards(p1Card, p2Card);
    }

    private void handleWar(String prevMessage) {
        List<Card> p1War = new ArrayList<>();
        List<Card> p2War = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            checkAndRefillHands();
            if (!player1.isEmpty())
                p1War.add(player1.remove(0));
            if (!player2.isEmpty())
                p2War.add(player2.remove(0));
        }

        spoils.addAll(p1War);
        spoils.addAll(p2War);

        checkAndRefillHands();
        if (player1.isEmpty()) {
            player2WinPile.addAll(spoils);
            view.getCardOutput().setText(prevMessage + "\nPlayer 2 wins the game!");
            updateDeckSizes();
            return;
        }
        if (player2.isEmpty()) {
            player1WinPile.addAll(spoils);
            view.getCardOutput().setText(prevMessage + "\nPlayer 1 wins the game!");
            updateDeckSizes();
            return;
        }

        Card p1Card = player1.remove(0);
        Card p2Card = player2.remove(0);
        spoils.add(p1Card);
        spoils.add(p2Card);

        String message = prevMessage + "\nWAR cards: Player 1 plays " + p1Card + " | Player 2 plays " + p2Card;

        if (p1Card.getValue() > p2Card.getValue()) {
            player1WinPile.addAll(spoils);
            message += "\nPlayer 1 wins the war!";
        } else if (p1Card.getValue() < p2Card.getValue()) {
            player2WinPile.addAll(spoils);
            message += "\nPlayer 2 wins the war!";
        } else {
            handleWar(message);
            return;
        }

        view.getCardOutput().setText(message);
        updateDeckSizes();
        showTopCards(p1Card, p2Card);
    }

    private void updateDeckSizes() {
        view.getDeckSize().setText(
                "Player 1 hand: " + player1.size() +
                        " || Player 2 hand: " + player2.size());
    }

    private void showTopCards(Card p1Card, Card p2Card) {
        Image img1 = new Image(getClass().getResourceAsStream(p1Card.getImagePath()));
        Image img2 = new Image(getClass().getResourceAsStream(p2Card.getImagePath()));

        view.getPlayer1CardImage().setImage(img1);
        view.getPlayer2CardImage().setImage(img2);
    }
}