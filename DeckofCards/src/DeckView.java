import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;


public class DeckView {

    // Button events that player creates
    private final Button shuffleButton = new Button("Shuffle Deck");
    private final Button drawButton = new Button("Draw Card");

    // added for cards!
    private final ImageView cardImage = new ImageView();


    // Labels to help the player
    private final Label cardOutput = new Label("Draw a card!");
    private final Label deckSize = new Label("Deck size: 52");

    public VBox buildUI() {
    VBox root = new VBox(15);

    // scale image!!! 
    cardImage.setFitWidth(120);
    cardImage.setPreserveRatio(true);

    root.getChildren().addAll(
        shuffleButton,
        drawButton,
        cardOutput,
        cardImage,
        deckSize
    );

    return root;
}

    // getters for the buttons, labels, and cards
    public ImageView getCardImage() {
    return cardImage;
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
