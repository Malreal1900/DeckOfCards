import java.util.ArrayList;
import java.util.List;

// Need to look over Hand UML
public class Hand {
    private final List<Card> cards;
    
    public Hand() {
        this.cards = new ArrayList<>();
    }
    
    public void addCard(Card card) {
        if (card != null) {
            cards.add(card);
        }
    }
    
    public Card playCard() {
        if (cards.isEmpty()) {
            return null;
        }
        return cards.remove(0);
    }
    
    public Card peekCard() {
        if (cards.isEmpty()) {
            return null;
        }
        return cards.get(0);
    }
    
    public boolean isEmpty() {
        return cards.isEmpty();
    }
    
    public int size() {
        return cards.size();
    }
    
    public List<Card> getCards() {
        return new ArrayList<>(cards);
    }
    
    public void clear() {
        cards.clear();
    }
    
    @Override
    public String toString() {
        if (cards.isEmpty()) {
            return "Empty hand";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cards.size(); i++) {
            sb.append(cards.get(i));
            if (i < cards.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}