import java.util.List;
import java.util.ArrayList;

public class WarGame {
    private Deck deck;
    private Hand player1Hand;
    private Hand player2Hand;
    private List<Card> warPile;
    private boolean gameOver;
    private String winner;
    
    public WarGame() {
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
        
        Card player1Card = player1Hand.playCard();
        Card player2Card = player2Hand.playCard();
        
        warPile.add(player1Card);
        warPile.add(player2Card);
        
        StringBuilder result = new StringBuilder();
        result.append("Player 1 plays: ").append(player1Card).append("\n");
        result.append("Player 2 plays: ").append(player2Card).append("\n");
        
        int comparison = compareCards(player1Card, player2Card);
        
        if (comparison > 0) {
            // Player 1 wins the round
            result.append("Player 1 wins the round!\n");
            player1Hand.addCard(player1Card);
            player1Hand.addCard(player2Card);
            for (Card card : warPile) {
                player1Hand.addCard(card);
            }
        } else if (comparison < 0) {
            // Player 2 wins the round
            result.append("Player 2 wins the round!\n");
            player2Hand.addCard(player1Card);
            player2Hand.addCard(player2Card);
            for (Card card : warPile) {
                player2Hand.addCard(card);
            }
        } else {
            // WAR!
            result.append("WAR!\n");
            return handleWar(result.toString());
        }
        
        warPile.clear();
        checkGameStatus();
        
        result.append("Player 1 cards: ").append(player1Hand.size()).append("\n");
        result.append("Player 2 cards: ").append(player2Hand.size());
        
        return result.toString();
    }
    
    private String handleWar(String initialMessage) {
        StringBuilder result = new StringBuilder(initialMessage);
        
        // Check if players have enough cards for war
        if (player1Hand.size() < 4 || player2Hand.size() < 4) {
            result.append("Not enough cards for war! Game over.\n");
            determineWinner();
            return result.toString();
        }
        
        // Add 3 face-down cards and 1 face-up card from each player
        for (int i = 0; i < 3; i++) {
            warPile.add(player1Hand.playCard());
            warPile.add(player2Hand.playCard());
        }
        
        Card player1WarCard = player1Hand.playCard();
        Card player2WarCard = player2Hand.playCard();
        
        warPile.add(player1WarCard);
        warPile.add(player2WarCard);
        
        result.append("War cards: ").append(player1WarCard).append(" vs ").append(player2WarCard).append("\n");
        
        int warComparison = compareCards(player1WarCard, player2WarCard);
        
        if (warComparison > 0) {
            result.append("Player 1 wins the war!\n");
            for (Card card : warPile) {
                player1Hand.addCard(card);
            }
        } else if (warComparison < 0) {
            result.append("Player 2 wins the war!\n");
            for (Card card : warPile) {
                player2Hand.addCard(card);
            }
        } else {
            result.append("Another WAR! Continuing...\n");
            return handleWar(result.toString());
        }
        
        warPile.clear();
        checkGameStatus();
        
        result.append("Player 1 cards: ").append(player1Hand.size()).append("\n");
        result.append("Player 2 cards: ").append(player2Hand.size());
        
        return result.toString();
    }
    
    private int compareCards(Card card1, Card card2) {
        // In War, we only compare rank, not suit
        return Integer.compare(card1.getValue(), card2.getValue());
    }
    
    private void checkGameStatus() {
        if (player1Hand.isEmpty()) {
            gameOver = true;
            winner = "Player 2";
        } else if (player2Hand.isEmpty()) {
            gameOver = true;
            winner = "Player 1";
        }
    }
    
    private void determineWinner() {
        if (player1Hand.size() > player2Hand.size()) {
            winner = "Player 1";
        } else if (player2Hand.size() > player1Hand.size()) {
            winner = "Player 2";
        } else {
            winner = "It's a tie!";
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
    
    public Hand getPlayer1Hand() {
        return player1Hand;
    }
    
    public Hand getPlayer2Hand() {
        return player2Hand;
    }
}