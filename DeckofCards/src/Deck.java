import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

    // This is a list for the dekc of cards, change as much as you want!
    private final List<Card> cards = new ArrayList<>();

    // constructor
    public Deck() {
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }
    }

    // Will need to add more methods as the UML grows
    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card drawCard() {
        if (cards.isEmpty()) {
            return null;
        }
        return cards.remove(0);
    }

    public int size() {
        return cards.size();
    }
}
