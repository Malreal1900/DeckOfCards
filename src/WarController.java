import javafx.scene.image.Image;
import javafx.scene.control.TextInputDialog;

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

    private boolean deckSplit = false; // ensure Split Deck is only used once per game

    public WarController(DeckView view) {
        this.view = view;

        // Initialize hands and win piles as empty
        player1 = new ArrayList<>();
        player2 = new ArrayList<>();
        player1WinPile = new ArrayList<>();
        player2WinPile = new ArrayList<>();
        spoils = new ArrayList<>();

        // Buttons initial state
        view.getDrawButton().setDisable(true); // can't play until deck is split
        view.getSplitDeckButton().setDisable(false);
        view.getShuffleButton().setDisable(true);

        connectActions();
        updateDeckSizes();
        view.getGameLog().appendText("Welcome! Split the deck to start.\n");
    }

    private void connectActions() {
        view.getDrawButton().setOnAction(e -> playRound());
        view.getShuffleButton().setOnAction(e -> startNewGame());
        view.getSplitDeckButton().setOnAction(e -> {
            if (!deckSplit) {
                promptDeckSplit();
            }
        });
    }

    // Prompt user to choose deck split
    private void promptDeckSplit() {
        TextInputDialog dialog = new TextInputDialog("26");
        dialog.setTitle("Split Deck");
        dialog.setHeaderText("Enter number of cards for Player 1:");
        dialog.setContentText("Cards for Player 1:");

        dialog.showAndWait().ifPresent(input -> {
            try {
                int p1Count = Integer.parseInt(input);

                if (p1Count < 0 || p1Count > 52) {
                    view.getGameLog().appendText("Invalid number! Must be 0-52.\n");
                    return;
                }

                Deck deck = new Deck();
                deck.shuffle();

                player1.clear();
                player2.clear();
                player1WinPile.clear();
                player2WinPile.clear();

                for (int i = 0; i < 52; i++) {
                    Card card = deck.drawCard();
                    if (i < p1Count)
                        player1.add(card);
                    else
                        player2.add(card);
                }

                view.getGameLog().appendText("Deck split: P1 " + player1.size() + " | P2 " + player2.size() + "\n");

                updateDeckSizes();
                view.getPlayer1CardImage().setImage(null);
                view.getPlayer2CardImage().setImage(null);

                // Enable play and new game buttons after split
                view.getDrawButton().setDisable(false);
                view.getShuffleButton().setDisable(false);

                // Disable split button after used
                deckSplit = true;
                view.getSplitDeckButton().setDisable(true);

            } catch (NumberFormatException e) {
                view.getGameLog().appendText("Invalid input!\n");
            }
        });
    }

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
            view.getGameLog().appendText("Player 2 wins the game!\n");
            view.getDrawButton().setDisable(true);
            return;
        }
        if (player2.isEmpty()) {
            view.getGameLog().appendText("Player 1 wins the game!\n");
            view.getDrawButton().setDisable(true);
            return;
        }

        spoils = new ArrayList<>();
        Card p1Card = player1.remove(0);
        Card p2Card = player2.remove(0);
        spoils.add(p1Card);
        spoils.add(p2Card);

        String message;

        if (p1Card.getValue() > p2Card.getValue()) {
            player1WinPile.addAll(spoils);
            message = "Player 1 wins the round!";
        } else if (p1Card.getValue() < p2Card.getValue()) {
            player2WinPile.addAll(spoils);
            message = "Player 2 wins the round!";
        } else {
            message = "WAR!";
            handleWar(message);
            return;
        }

        view.getGameLog().appendText(message + "\n");
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
            view.getGameLog().appendText(prevMessage + "\nPlayer 2 wins the game!\n");
            updateDeckSizes();
            view.getDrawButton().setDisable(true);
            return;
        }
        if (player2.isEmpty()) {
            player1WinPile.addAll(spoils);
            view.getGameLog().appendText(prevMessage + "\nPlayer 1 wins the game!\n");
            updateDeckSizes();
            view.getDrawButton().setDisable(true);
            return;
        }

        Card p1Card = player1.remove(0);
        Card p2Card = player2.remove(0);
        spoils.add(p1Card);
        spoils.add(p2Card);

        String message = prevMessage + " WAR cards!";

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

        view.getGameLog().appendText(message + "\n");
        updateDeckSizes();
        showTopCards(p1Card, p2Card);
    }

    private void updateDeckSizes() {
        int p1Win = player1WinPile.size();
        int p2Win = player2WinPile.size();

        view.getDeckSize().setText(
                "P1: " + player1.size() + " (" + p1Win + ")"
                        + "    |    "
                        + "P2: " + player2.size() + " (" + p2Win + ")");
    }

    private void showTopCards(Card p1Card, Card p2Card) {
        try {
            Image img1 = new Image(getClass().getResourceAsStream(p1Card.getImagePath()));
            Image img2 = new Image(getClass().getResourceAsStream(p2Card.getImagePath()));

            view.getPlayer1CardImage().setImage(img1);
            view.getPlayer2CardImage().setImage(img2);
        } catch (Exception e) {
            System.err.println("Error loading card images: " + e);
        }
    }

    // Start a completely new game
    private void startNewGame() {
        Deck deck = new Deck(); // full deck
        deck.shuffle();

        player1 = new ArrayList<>();
        player2 = new ArrayList<>();
        player1WinPile = new ArrayList<>();
        player2WinPile = new ArrayList<>();

        // default split evenly
        boolean turn = true;
        Card card;
        while ((card = deck.drawCard()) != null) {
            if (turn)
                player1.add(card);
            else
                player2.add(card);
            turn = !turn;
        }

        // Reset UI
        view.getPlayer1CardImage().setImage(null);
        view.getPlayer2CardImage().setImage(null);
        updateDeckSizes();

        // Enable Split Deck button again
        deckSplit = false;
        view.getSplitDeckButton().setDisable(false);

        // Disable Play Round until Split Deck is clicked
        view.getDrawButton().setDisable(true);

        // Determine who "wins" based on current card counts
        int p1Count = player1.size();
        int p2Count = player2.size();
        String winnerMessage;
        if (p1Count > p2Count) {
            winnerMessage = "Player 1 would win the current setup!";
        } else if (p2Count > p1Count) {
            winnerMessage = "Player 2 would win the current setup!";
        } else {
            winnerMessage = "It's a tie based on current card counts!";
        }

        // Log message
        view.getGameLog().appendText(winnerMessage);
    }
}