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

    public String getImagePath() {
        String rankName;

        switch (rank) {
            case ACE:
                rankName = "ace";
                break;
            case JACK:
                rankName = "jack";
                break;
            case QUEEN:
                rankName = "queen";
                break;
            case KING:
                rankName = "king";
                break;
            default:
                // Uses numeric value for 2–10
                rankName = String.valueOf(rank.getValue());
        }

        String suitName = suit.name().toLowerCase(); // hearts, diamonds, clubs, spades

        return "/cards/" + rankName + "_of_" + suitName + ".png";
    }
}