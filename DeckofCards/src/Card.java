// Reps a single playing card w/ a suit, rank, and value.
// Will be used by the Deck and Hand in the game.
public class Card {
    
    private final Suit suit;
    private final Rank rank;

    // Constructor for Card --- makes card w/ specific suit & rank
    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }

    public int getValue() {
        return rank.getValue();
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }
}