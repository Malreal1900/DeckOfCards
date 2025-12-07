// From BirsaLR 11/16
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class SplitDeckDialog {

    private final Dialog<int[]> dialog;
    private final TextField player1Field;

    public SplitDeckDialog() {
        dialog = new Dialog<>();
        dialog.setTitle("Split Deck");

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        player1Field = new TextField();
        player1Field.setPromptText("Enter cards for Player 1 (1–51)");

        VBox box = new VBox(10, new Label("How many cards should Player 1 get? (1–51)"), player1Field);
        box.setPadding(new Insets(15));

        dialog.getDialogPane().setContent(box);

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                try {
                    int p1 = Integer.parseInt(player1Field.getText());

                    // Prevents 0-card or 52-card splits
                    if (p1 > 0 && p1 < 52) {
                        return new int[]{ p1, 52 - p1 };
                    }

                } catch (Exception ignore) { }
            }
            return null;
        });
    }

    public int[] showAndWait() {
        return dialog.showAndWait().orElse(null);
    }
}