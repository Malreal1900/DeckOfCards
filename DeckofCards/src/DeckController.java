public class DeckController {

    private Deck deck;
    private final DeckView view;

    public DeckController(DeckView view) {
        this.view = view;
        this.deck = new Deck();
        connectActions();
    }

    private void connectActions() {

        view.getShuffleButton().setOnAction(e -> {
            deck = new Deck();
            deck.shuffle();
            view.getCardOutput().setText("Deck shuffled!");
            updateDeckSize();
        });

        view.getDrawButton().setOnAction(e -> {
            Card card = deck.drawCard();
            if (card == null) {
                view.getCardOutput().setText("No more cards!");
            } else {
                view.getCardOutput().setText("You drew: " + card);
            }
            updateDeckSize();
        });
    }

    private void updateDeckSize() {
        view.getDeckSize().setText("Deck size: " + deck.size());
    }
}
