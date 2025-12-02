import java.util.ArrayList;
import java.util.List;

// Need to look over Hand UML
public class Hand {
    private final List<Card> cards;
    private Card lastPlayedCard; // Track the last card played
    
    public Hand() {
        this.cards = new ArrayList<>();
        this.lastPlayedCard = null;
    }
    
    public void addCard(Card card) {
        if (card != null) {
            cards.add(card);
        }
    }
    
    public Card playCard() {
        if (cards.isEmpty()) {
            lastPlayedCard = null;
            return null;
        }
        lastPlayedCard = cards.remove(0); // Store the card we're about to return
        return lastPlayedCard;
    }
    
    public Card peekCard() {
        if (cards.isEmpty()) {
            return null;
        }
        return cards.get(0);
    }

    // NEW METHOD: Get the last card that was played
    public Card getLastPlayedCard() {
        return lastPlayedCard;
    }
    
    // NEW METHOD: Clear the last played card (useful for game reset)
    public void clearLastPlayedCard() {
        lastPlayedCard = null;
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