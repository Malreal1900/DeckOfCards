import javafx.scene.image.Image;
import javafx.animation.PauseTransition;
import javafx.scene.control.TextInputDialog;
import javafx.util.Duration;

public class WarController {

    private final DeckView view;
    private WarGame warGame;

    public WarController(DeckView view) {
        this.view = view;
        this.warGame = new WarGame();

        // Set initial button states
        view.getDrawButton().setDisable(true);
        view.getSplitDeckButton().setDisable(false);
        view.getShuffleButton().setDisable(true);

        connectActions();
        updateDeckSizes();
        view.getGameLog().appendText("Welcome! Split the deck to start.\n");
    }

    // Links btn clicks to actions
    private void connectActions() {
        view.getDrawButton().setOnAction(e -> {
            playRound();
        });

        view.getShuffleButton().setOnAction(e -> {
            startNewGame();
        });

        view.getSplitDeckButton().setOnAction(e -> {
            promptDeckSplit();
        });
    }

    // Prompt for cards player 1 should get
    private void promptDeckSplit() {
        TextInputDialog dialog = new TextInputDialog("26");
        dialog.setTitle("Split Deck");
        dialog.setHeaderText("Enter number of cards for Player 1:");
        dialog.setContentText("Cards for Player 1:");

        dialog.showAndWait().ifPresent(input -> {
            try {
                int p1Count = Integer.parseInt(input);

                if (p1Count < 1 || p1Count > 51) {
                    view.getGameLog().appendText("Invalid number! Must be between 1 and 51.\n");
                    return;
                }

                warGame.splitDeck(p1Count);

                view.getGameLog().appendText(
                        "Deck split: P1 " + warGame.getPlayer1Count() +
                                " | P2 " + warGame.getPlayer2Count() + "\n");

                updateDeckSizes();

                view.getPlayer1CardImage().setImage(null);
                view.getPlayer2CardImage().setImage(null);

                view.getDrawButton().setDisable(false);
                view.getShuffleButton().setDisable(true);
                view.getSplitDeckButton().setDisable(true);

            } catch (NumberFormatException e) {
                view.getGameLog().appendText("Invalid input!\n");
            }
        });
    }

    // Single rd
    private void playRound() {
        if (warGame.isGameOver() == true) {
            view.getDrawButton().setDisable(true);
            return;
        }

        Card[] playedCards = warGame.playRound();

        // Show cards drawn
        if (playedCards.length == 2) {
            showTopCards(playedCards[0], playedCards[1]);
        }

        // Rd message in log
        view.getGameLog().appendText(warGame.getLastRoundMessage() + "\n");

        updateDeckSizes();

        // Handle war
        if (warGame.isWarTriggered() == true) {
            handleWar();
            return;
        }

        view.getShuffleButton().setDisable(false);
    }

    private void handleWar() {
        view.getDrawButton().setDisable(true);
        view.getShuffleButton().setDisable(true);

        // Stop the game
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> {
            warGame.resolveWar();
            Card[] finalCards = warGame.getWarCards();

            // Show final card
            if (finalCards.length == 2) {
                showTopCards(finalCards[0], finalCards[1]);
            }

            view.getGameLog().appendText(warGame.getLastRoundMessage() + "\n");

            updateDeckSizes();

            view.getDrawButton().setDisable(false);
            view.getShuffleButton().setDisable(false);
        });

        pause.play();
    }

    private void showTopCards(Card p1Card, Card p2Card) {
        try {
            Image p1Image = new Image(getClass().getResourceAsStream(p1Card.getImagePath()));
            Image p2Image = new Image(getClass().getResourceAsStream(p2Card.getImagePath()));

            view.getPlayer1CardImage().setImage(p1Image);
            view.getPlayer2CardImage().setImage(p2Image);

        } catch (Exception e) {
            System.err.println("Error loading card images: " + e);
        }
    }

    private void updateDeckSizes() {
        String deckInfo = "P1: " + warGame.getPlayer1Count() + " (" + warGame.getPlayer1WinCount() + ")" +
                "    |    " +
                "P2: " + warGame.getPlayer2Count() + " (" + warGame.getPlayer2WinCount() + ")";

        view.getDeckSize().setText(deckInfo);
    }

    private void startNewGame() {
        String winnerMessage = warGame.getWinnerMessage();
        view.getGameLog().appendText(winnerMessage + "\n");

        warGame.resetGame();
        updateDeckSizes();

        view.getPlayer1CardImage().setImage(null);
        view.getPlayer2CardImage().setImage(null);

        view.getDrawButton().setDisable(true);
        view.getShuffleButton().setDisable(true);
        view.getSplitDeckButton().setDisable(false);
    }
}