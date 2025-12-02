import java.util.List;
import java.util.ArrayList;

public class WarGame {
    private Deck deck;
    private Hand player1Hand;
    private Hand player2Hand;
    private List<Card> warPile;
    private boolean gameOver;
    private String winner;
    private Card lastPlayer1Card;
    private Card lastPlayer2Card;
    
    public WarGame() {
        lastPlayer1Card = null;
        lastPlayer2Card = null;
        initializeGame();
    }
    
    private void initializeGame() {
        deck = new Deck();
        deck.shuffle();
        player1Hand = new Hand();
        player2Hand = new Hand();
        warPile = new ArrayList<>();
        gameOver = false;
        winner = null;
        lastPlayer1Card = null;
        lastPlayer2Card = null;
        
        // Deal cards to both players
        while (!deck.isEmpty()) {
            player1Hand.addCard(deck.drawCard());
            if (!deck.isEmpty()) {
                player2Hand.addCard(deck.drawCard());
            }
        }
    }
    
    public String playRound() {
        if (gameOver) {
            return "Game over! Winner: " + winner;
        }
        
        if (player1Hand.isEmpty() || player2Hand.isEmpty()) {
            determineWinner();
            return "Game over! Winner: " + winner;
        }
        
        // Clear war pile at start of normal round
        warPile.clear();
        
        // Play cards and store them as last played
        lastPlayer1Card = player1Hand.playCard();
        lastPlayer2Card = player2Hand.playCard();
        
        // Add cards to war pile
        warPile.add(lastPlayer1Card);
        warPile.add(lastPlayer2Card);
        
        StringBuilder result = new StringBuilder();
        result.append("Player 1 plays: ").append(lastPlayer1Card).append("\n");
        result.append("Player 2 plays: ").append(lastPlayer2Card).append("\n");
        
        int comparison = compareCards(lastPlayer1Card, lastPlayer2Card);
        
        if (comparison > 0) {
            // Player 1 wins the round
            result.append("Player 1 wins the round!\n");
            // Add all cards from warPile to player1's hand
            for (Card card : warPile) {
                player1Hand.addCard(card);
            }
        } else if (comparison < 0) {
            // Player 2 wins the round
            result.append("Player 2 wins the round!\n");
            // Add all cards from warPile to player2's hand
            for (Card card : warPile) {
                player2Hand.addCard(card);
            }
        } else {
            // WAR!
            result.append("WAR!\n");
            // Don't clear war pile yet - we'll add more cards during war
            return handleWar(result.toString());
        }
        
        // Clear war pile after normal round win
        warPile.clear();
        
        checkGameStatus();
        
        result.append("Player 1 cards: ").append(player1Hand.size()).append("\n");
        result.append("Player 2 cards: ").append(player2Hand.size());
        
        return result.toString();
    }
    
    private String handleWar(String initialMessage) {
        StringBuilder result = new StringBuilder(initialMessage);
        
        // Check if players have enough cards for war
        if (player1Hand.isEmpty() || player2Hand.isEmpty()) {
            result.append("Not enough cards for war! Game over.\n");
            determineWinner();
            return result.toString();
        }
        
        // Check if players have at least 1 card for the face-up war card
        if (player1Hand.size() < 1 || player2Hand.size() < 1) {
            result.append("Not enough cards for war! Game over.\n");
            determineWinner();
            return result.toString();
        }
        
        // Check if players have enough cards for face-down cards (3 face-down + 1 face-up)
        boolean canDoFullWar = player1Hand.size() >= 4 && player2Hand.size() >= 4;
        
        if (canDoFullWar) {
            // Add 3 face-down cards from each player to war pile
            for (int i = 0; i < 3; i++) {
                warPile.add(player1Hand.playCard());
                warPile.add(player2Hand.playCard());
            }
        } else {
            // Not enough cards for full war
            result.append("Not enough cards for full war! ");
            // Add whatever cards are left
            int cardsToAdd = Math.min(player1Hand.size() - 1, player2Hand.size() - 1);
            cardsToAdd = Math.min(cardsToAdd, 3); // Max 3 face-down cards
            
            for (int i = 0; i < cardsToAdd; i++) {
                warPile.add(player1Hand.playCard());
                warPile.add(player2Hand.playCard());
            }
        }
        
        // Play the face-up war cards
        lastPlayer1Card = player1Hand.playCard();
        lastPlayer2Card = player2Hand.playCard();
        
        warPile.add(lastPlayer1Card);
        warPile.add(lastPlayer2Card);
        
        result.append("War cards: ").append(lastPlayer1Card).append(" vs ").append(lastPlayer2Card).append("\n");
        
        int warComparison = compareCards(lastPlayer1Card, lastPlayer2Card);
        
        if (warComparison > 0) {
            result.append("Player 1 wins the war!\n");
            for (Card card : warPile) {
                player1Hand.addCard(card);
            }
            warPile.clear();
        } else if (warComparison < 0) {
            result.append("Player 2 wins the war!\n");
            for (Card card : warPile) {
                player2Hand.addCard(card);
            }
            warPile.clear();
        } else {
            result.append("Another WAR! Continuing...\n");
            return handleWar(result.toString());
        }
        
        checkGameStatus();
        
        result.append("Player 1 cards: ").append(player1Hand.size()).append("\n");
        result.append("Player 2 cards: ").append(player2Hand.size());
        
        return result.toString();
    }
    
    private int compareCards(Card card1, Card card2) {
        return Integer.compare(card1.getValue(), card2.getValue());
    }
    
    private void checkGameStatus() {
        if (player1Hand.isEmpty() || player2Hand.isEmpty()) {
            determineWinner();
        }
    }
    
    private void determineWinner() {
        if (player1Hand.isEmpty() && player2Hand.isEmpty()) {
            winner = "It's a tie!";
        } else if (player1Hand.isEmpty()) {
            winner = "Player 2";
        } else if (player2Hand.isEmpty()) {
            winner = "Player 1";
        } else {
            // Game isn't actually over yet
            return;
        }
        gameOver = true;
    }
    
    public void restart() {
        initializeGame();
    }
    
    public boolean isGameOver() {
        return gameOver;
    }
    
    public String getWinner() {
        return winner;
    }
    
    public int getPlayer1CardCount() {
        return player1Hand.size();
    }
    
    public int getPlayer2CardCount() {
        return player2Hand.size();
    }
    
    public Card getLastPlayer1Card() {
        return lastPlayer1Card;
    }
    
    public Card getLastPlayer2Card() {
        return lastPlayer2Card;
    }
    
    public Hand getPlayer1Hand() {
        return player1Hand;
    }
    
    public Hand getPlayer2Hand() {
        return player2Hand;
    }
}