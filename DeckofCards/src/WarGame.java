// Need to fix the logic of this code.

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
            // Add all cards from warPile to player1's hand
            for (Card card : warPile) {
                player1Hand.addCard(card);
            }
            warPile.clear();
        } else if (comparison < 0) {
            // Player 2 wins the round
            result.append("Player 2 wins the round!\n");
            // Add all cards from warPile to player2's hand
            for (Card card : warPile) {
                player2Hand.addCard(card);
            }
            warPile.clear();
        } else {
            // WAR!
            result.append("WAR!\n");
            return handleWar(result.toString());
        }
        
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
            // Add 3 face-down cards from each player
            for (int i = 0; i < 3; i++) {
                warPile.add(player1Hand.playCard());
                warPile.add(player2Hand.playCard());
            }
        } else {
            // Not enough cards for full war, use all remaining cards
            result.append("Not enough cards for full war! Using remaining cards.\n");
        }
        
        // Play the face-up war cards
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
        // In War, we only compare rank, not suit
        // Note: Assuming Card.getValue() returns rank value (2=2, 3=3, ..., J=11, Q=12, K=13, A=14)
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
    
    public Hand getPlayer1Hand() {
        return player1Hand;
    }
    
    public Hand getPlayer2Hand() {
        return player2Hand;
    }
}