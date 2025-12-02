import java.util.ArrayList;
import java.util.List;

public class Hand {

    private List<Card> cards;

    // Constructor — empty hand
    public Hand() {
        cards = new ArrayList<>();
    }

    // Add card to hand
    public void addCard(Card card) {
        if (card != null) {
            cards.add(card);
        }
    }

    // Remove a card (if needed)
    public void removeCard(Card card) {
        cards.remove(card);
    }

    // Clears all cards
    public void clearHand() {
        cards.clear();
    }

    // Returns list for checking
    public List<Card> getCards() {
        return cards;
    }

    @Override
    public String toString() {
        return cards.toString();
    }
}
