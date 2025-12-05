// Sets the possible options for a Card's rank
// These values will need to be changed based on the game being played
public enum Rank {
    ACE(14), // Ace is high
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    TEN(10),
    JACK(11),
    QUEEN(12),
    KING(13);

    private final int value;

    // Constructor for Rank --- sets numeric value for the rank
    Rank(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}