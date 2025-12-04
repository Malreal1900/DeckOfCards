import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class DeckView {

    private final Button shuffleButton = new Button("New Game");
    private final Button drawButton = new Button("Play Round");
    private final Button splitDeckButton = new Button("Split Deck");

    private final ImageView player1CardImage = new ImageView();
    private final ImageView player2CardImage = new ImageView();

    private final Label cardOutput = new Label("");
    private final Label deckSize = new Label("P1: 26 | P2: 26");

    private final TextArea gameLog = new TextArea();

    public VBox buildUI() {

        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #2c3e50, #4b79a1);");

        // Title
        Label title = new Label("WAR CARD GAME");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: white;");

        // Card Images
        player1CardImage.setFitWidth(140);
        player1CardImage.setPreserveRatio(true);

        player2CardImage.setFitWidth(140);
        player2CardImage.setPreserveRatio(true);

        VBox p1Box = new VBox(10, new Label("Player 1"), player1CardImage);
        p1Box.setAlignment(Pos.CENTER);
        p1Box.getChildren().get(0).setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

        VBox p2Box = new VBox(10, new Label("Player 2"), player2CardImage);
        p2Box.setAlignment(Pos.CENTER);
        p2Box.getChildren().get(0).setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

        HBox cardRow = new HBox(40, p1Box, p2Box);
        cardRow.setAlignment(Pos.CENTER);

        // Buttons
        splitDeckButton.setDisable(false);
        shuffleButton.setDisable(true);

        HBox buttonRow = new HBox(15, drawButton, shuffleButton, splitDeckButton);
        buttonRow.setAlignment(Pos.CENTER);

        drawButton.setStyle("-fx-font-size: 14px;");
        shuffleButton.setStyle("-fx-font-size: 14px;");
        splitDeckButton.setStyle("-fx-font-size: 14px;");

        // Deck sizes
        deckSize.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");

        // Game log box
        gameLog.setEditable(false);
        gameLog.setPrefHeight(150);
        gameLog.setStyle("-fx-font-size: 14px;");

        VBox logBox = new VBox(5, new Label("Game Log:"), gameLog);
        logBox.setPadding(new Insets(10));
        logBox.setStyle("-fx-background-color: rgba(255,255,255,0.8); -fx-background-radius: 10;");

        root.getChildren().addAll(
                title,
                cardRow,
                deckSize,
                buttonRow,
                cardOutput,
                logBox);

        return root;
    }

    // Getters
    public ImageView getPlayer1CardImage() {
        return player1CardImage;
    }

    public ImageView getPlayer2CardImage() {
        return player2CardImage;
    }

    public Button getShuffleButton() {
        return shuffleButton;
    }

    public Button getDrawButton() {
        return drawButton;
    }

    public Button getSplitDeckButton() {
        return splitDeckButton;
    }

    public Label getDeckSize() {
        return deckSize;
    }

    public TextArea getGameLog() {
        return gameLog;
    }
}
