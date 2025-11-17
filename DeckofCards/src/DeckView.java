import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DeckView {

    private final Button shuffleButton = new Button("Shuffle Deck");
    private final Button drawButton = new Button("Draw Card");
    private final Label cardOutput = new Label("Draw a card!");
    private final Label deckSize = new Label("Deck size: 52");

    public VBox buildUI() {
        VBox root = new VBox(15);
        root.getChildren().addAll(shuffleButton, drawButton, cardOutput, deckSize);
        return root;
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
