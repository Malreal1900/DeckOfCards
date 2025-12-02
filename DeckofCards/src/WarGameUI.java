import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class WarGameUI extends Application {

    // ====UI Section===
    private WarGame warGame;
    private TextArea gameLog;
    private Label player1CountLabel;
    private Label player2CountLabel;
    private Label statusLabel;
    private ImageView player1CardImage;
    private ImageView player2CardImage;
    
    @Override
    public void start(Stage stage) {
        warGame = new WarGame();
        
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));
        
        // Top: Game title
        Label titleLabel = new Label("WAR CARD GAME");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        BorderPane.setAlignment(titleLabel, Pos.CENTER);
        root.setTop(titleLabel);
        
        // Center: Card display
        HBox cardBox = new HBox(30);
        cardBox.setAlignment(Pos.CENTER);
        
        VBox player1Box = new VBox(10);
        player1Box.setAlignment(Pos.CENTER);
        player1CardImage = new ImageView();
        player1CardImage.setFitWidth(100);
        player1CardImage.setPreserveRatio(true);
        Label player1Label = new Label("Player 1");
        player1CountLabel = new Label("Cards: " + warGame.getPlayer1CardCount());
        player1Box.getChildren().addAll(player1Label, player1CardImage, player1CountLabel);
        
        VBox player2Box = new VBox(10);
        player2Box.setAlignment(Pos.CENTER);
        player2CardImage = new ImageView();
        player2CardImage.setFitWidth(100);
        player2CardImage.setPreserveRatio(true);
        Label player2Label = new Label("Player 2");
        player2CountLabel = new Label("Cards: " + warGame.getPlayer2CardCount());
        player2Box.getChildren().addAll(player2Label, player2CardImage, player2CountLabel);
        
        cardBox.getChildren().addAll(player1Box, player2Box);
        root.setCenter(cardBox);
        
        // Bottom: Controls and game log
        VBox bottomBox = new VBox(10);
        
        statusLabel = new Label("Click 'Play Round' to start!");
        statusLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button playButton = new Button("Play Round");
        playButton.setOnAction(e -> playRound());
        
        Button restartButton = new Button("Restart Game");
        restartButton.setOnAction(e -> restartGame());
        
        buttonBox.getChildren().addAll(playButton, restartButton);
        
        gameLog = new TextArea();
        gameLog.setEditable(false);
        gameLog.setPrefHeight(150);
        gameLog.setPrefWidth(500);
        
        bottomBox.getChildren().addAll(statusLabel, buttonBox, new Label("Game Log:"), gameLog);
        root.setBottom(bottomBox);
        
        Scene scene = new Scene(root, 600, 600);
        stage.setScene(scene);
        stage.setTitle("War Card Game");
        stage.show();
    }
    
    // code logic starts here!!!
    private void playRound() {
        if (warGame.isGameOver()) {
            statusLabel.setText("Game Over! Winner: " + warGame.getWinner());
            return;
        }
        
        String result = warGame.playRound();
        gameLog.appendText(result + "\n\n");
        
        // Update card counts
        player1CountLabel.setText("Cards: " + warGame.getPlayer1CardCount());
        player2CountLabel.setText("Cards: " + warGame.getPlayer2CardCount());
        
        // Update card images if possible
        updateCardImages();
        
        if (warGame.isGameOver()) {
            statusLabel.setText("Game Over! Winner: " + warGame.getWinner());
            gameLog.appendText("*** GAME OVER ***\n");
            gameLog.appendText("Winner: " + warGame.getWinner() + "\n");
        } else {
            statusLabel.setText("Round played! Next round ready.");
        }
    }
    
    private void updateCardImages() {
        // Show top cards if available
        Hand player1Hand = warGame.getPlayer1Hand();
        Hand player2Hand = warGame.getPlayer2Hand();
        
        if (!player1Hand.isEmpty()) {
            Card card = player1Hand.peekCard();
            Image img = new Image(getClass().getResourceAsStream(card.getImagePath()));
            player1CardImage.setImage(img);
        }
        
        if (!player2Hand.isEmpty()) {
            Card card = player2Hand.peekCard();
            Image img = new Image(getClass().getResourceAsStream(card.getImagePath()));
            player2CardImage.setImage(img);
        }
    }
    
    private void restartGame() {
        warGame.restart();
        gameLog.clear();
        player1CountLabel.setText("Cards: " + warGame.getPlayer1CardCount());
        player2CountLabel.setText("Cards: " + warGame.getPlayer2CardCount());
        statusLabel.setText("Game restarted! Click 'Play Round' to start!");
        player1CardImage.setImage(null);
        player2CardImage.setImage(null);
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}