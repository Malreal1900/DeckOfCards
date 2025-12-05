// From BirsaLR 11/16
import javafx.scene.image.Image;

public class DeckController {

    private Deck deck;
    private final DeckView view;

    public DeckController(DeckView view) {
        this.view = view;
        this.deck = new Deck();
        connectActions();
    }

    private void connectActions() {

        // shuffle the deck
        view.getShuffleButton().setOnAction(e -> {
            deck = new Deck();
            deck.shuffle();
            updateDeckSize();
        });

        // draw a card from the deck - only implemented the top one for now
        view.getDrawButton().setOnAction(e -> {
            Card card = deck.drawCard();

            if (card == null) {
                view.getPlayer2CardImage().setImage(null);
                return;
            }


            // ⭐ Load image from resources/cards/
            Image img = new Image(getClass().getResourceAsStream(card.getImagePath()));
            view.getPlayer2CardImage().setImage(img);

            updateDeckSize();
        });

    }

    // tells the user how many cards are left in the deck, resets if you shuffle
    private void updateDeckSize() {
        view.getDeckSize().setText("Deck size: " + deck.size());
    }
}