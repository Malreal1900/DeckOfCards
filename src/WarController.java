import javafx.scene.image.Image;
import javafx.animation.PauseTransition;
import javafx.scene.control.TextInputDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.util.Duration;

public class WarController {

    private final DeckView view;

    private List<Card> player1; // Player 1's hand
    private List<Card> player2; // Player 2's hand
    private List<Card> player1WinPile; // Cards won by Player 1
    private List<Card> player2WinPile; // Cards won by Player 2

    private List<Card> spoils; // cards at stake during a round

    private boolean deckSplit = false; // ensure Split Deck is only used once per game
    private boolean roundPlayed = false;

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
    // Called when user clicks Split Deck
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

                // Buttons: enable Play Round, disable Split Deck & New Game
                view.getDrawButton().setDisable(false); // can play rounds now
                view.getShuffleButton().setDisable(true); // New Game disabled until round played
                view.getSplitDeckButton().setDisable(true);
                roundPlayed = false; // first round not yet played

                deckSplit = true;

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

    // Called when user clicks Play Round
    private void playRound() {
        checkAndRefillHands();

        if (player1.isEmpty() || player2.isEmpty()) {
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
            view.getGameLog().appendText(message + "\n");
            updateDeckSizes();
            showTopCards(p1Card, p2Card);
        } else if (p1Card.getValue() < p2Card.getValue()) {
            player2WinPile.addAll(spoils);
            message = "Player 2 wins the round!";
            view.getGameLog().appendText(message + "\n");
            updateDeckSizes();
            showTopCards(p1Card, p2Card);
        } else {
            // WAR
            handleWar(p1Card, p2Card, "Tie! Starting WAR...");
        }

        // Enable New Game button after the first round
        if (!roundPlayed) {
            view.getShuffleButton().setDisable(false);
            roundPlayed = true;
        }
    }

    private void handleWar(Card p1Card, Card p2Card, String prevMessage) {
        // Disable Play Round button temporarily
        view.getDrawButton().setDisable(true);
        view.getShuffleButton().setDisable(true);

        // Show the tied cards first
        spoils.add(p1Card);
        spoils.add(p2Card);

        view.getPlayer1CardImage().setImage(new Image(getClass().getResourceAsStream(p1Card.getImagePath())));
        view.getPlayer2CardImage().setImage(new Image(getClass().getResourceAsStream(p2Card.getImagePath())));
        view.getGameLog().appendText(prevMessage + " WAR! Cards tied: " + p1Card + " vs " + p2Card + "\n");

        // Pause 2 seconds before continuing the war
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> continueWar());
        pause.play();
    }

    private void continueWar() {
        List<Card> p1War = new ArrayList<>();
        List<Card> p2War = new ArrayList<>();

        // Draw 3 face-down cards for each player
        for (int i = 0; i < 3; i++) {
            checkAndRefillHands();
            if (!player1.isEmpty())
                p1War.add(player1.remove(0));
            if (!player2.isEmpty())
                p2War.add(player2.remove(0));
        }

        spoils.addAll(p1War);
        spoils.addAll(p2War);

        // Draw the final face-up card for each player
        checkAndRefillHands();
        if (player1.isEmpty()) {
            player2WinPile.addAll(spoils);
            updateDeckSizes();
            view.getGameLog().appendText("Player 2 wins the war because Player 1 ran out of cards!\n");
            view.getDrawButton().setDisable(false); // re-enable for next round
            return;
        }
        if (player2.isEmpty()) {
            player1WinPile.addAll(spoils);
            updateDeckSizes();
            view.getGameLog().appendText("Player 1 wins the war because Player 2 ran out of cards!\n");
            view.getDrawButton().setDisable(false); // re-enable for next round
            return;
        }

        Card p1Final = player1.remove(0);
        Card p2Final = player2.remove(0);
        spoils.add(p1Final);
        spoils.add(p2Final);

        // Update GUI to show final face-up cards
        view.getPlayer1CardImage().setImage(new Image(getClass().getResourceAsStream(p1Final.getImagePath())));
        view.getPlayer2CardImage().setImage(new Image(getClass().getResourceAsStream(p2Final.getImagePath())));

        // Decide winner of war
        String message;
        if (p1Final.getValue() > p2Final.getValue()) {
            player1WinPile.addAll(spoils);
            message = "Player 1 wins the war!";
        } else if (p1Final.getValue() < p2Final.getValue()) {
            player2WinPile.addAll(spoils);
            message = "Player 2 wins the war!";
        } else {
            handleWar(p1Final, p2Final, "Double WAR again!");
            return;
        }

        view.getGameLog().appendText(message + "\n");
        updateDeckSizes();

        // Re-enable Play Round button after war finishes
        view.getDrawButton().setDisable(false);
        view.getShuffleButton().setDisable(false);
    }

    // Helper to show cards
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

    private void updateDeckSizes() {
        int p1Win = player1WinPile.size();
        int p2Win = player2WinPile.size();

        view.getDeckSize().setText(
                "P1: " + player1.size() + " (" + p1Win + ")"
                        + "    |    "
                        + "P2: " + player2.size() + " (" + p2Win + ")");
    }

    // Start a completely new game
    private void startNewGame() {
        // Determine winner from previous game before clearing
        int prevP1Count = (player1 != null ? player1.size() : 0) + (player1WinPile != null ? player1WinPile.size() : 0);
        int prevP2Count = (player2 != null ? player2.size() : 0) + (player2WinPile != null ? player2WinPile.size() : 0);

        String winnerMessage;
        if (prevP1Count > prevP2Count) {
            winnerMessage = "Player 1 won the previous game with " + prevP1Count + " cards!";
        } else if (prevP2Count > prevP1Count) {
            winnerMessage = "Player 2 won the previous game with " + prevP2Count + " cards!";
        } else if (prevP1Count + prevP2Count > 0) {
            winnerMessage = "The previous game ended in a tie!";
        } else {
            winnerMessage = "No previous game played yet.";
        }

        view.getGameLog().appendText(winnerMessage + "\n");

        // Clear hands and win piles for new game
        player1 = new ArrayList<>();
        player2 = new ArrayList<>();
        player1WinPile = new ArrayList<>();
        player2WinPile = new ArrayList<>();

        // Create and shuffle new deck
        Deck deck = new Deck();
        deck.shuffle();

        // Reset UI images & deck sizes
        view.getPlayer1CardImage().setImage(null);
        view.getPlayer2CardImage().setImage(null);
        updateDeckSizes();

        // Enable Split Deck button, disable Play Round and New Game
        deckSplit = false;
        view.getSplitDeckButton().setDisable(false);
        view.getDrawButton().setDisable(true);
        view.getShuffleButton().setDisable(true);
    }
}