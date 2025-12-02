// From BirsaLR 11/16
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class DeckView {

    // Button events that player creates
    private final Button shuffleButton = new Button("New Game");
    private final Button drawButton = new Button("Play Round");

    // added for cards!
    private final ImageView player1CardImage = new ImageView();
    private final ImageView player2CardImage = new ImageView();

    // Labels to help the player
    private final Label cardOutput = new Label("Draw a card!");
    private final Label deckSize = new Label("Player 1: 26 | Player 2: 26");

    public VBox buildUI() {
        VBox root = new VBox(15);

        // scale images
        player1CardImage.setFitWidth(120);
        player1CardImage.setPreserveRatio(true);

        player2CardImage.setFitWidth(120);
        player2CardImage.setPreserveRatio(true);

        // horizontal box to show both cards
        HBox cardBox = new HBox(20);
        cardBox.getChildren().addAll(player1CardImage, player2CardImage);

        root.getChildren().addAll(
                drawButton,  // plays the next round
                shuffleButton, // new game button
                cardOutput,  // shows round info
                cardBox,     // shows top cards of both players
                deckSize     // shows players' card counts
        );

        return root;
    }

    // getters for buttons, images, and labels
    public ImageView getPlayer1CardImage() {
        return player1CardImage;
    }

    public ImageView getPlayer2CardImage() {
        return player2CardImage;
    }

    public Button getShuffleButton() {
        return shuffleButton;
    }

    public Button getDrawButton() {
        return drawButton;
    }

    public Label getCardOutput() {
        return cardOutput;
    }

    public Label getDeckSize() {
        return deckSize;
    }
}
