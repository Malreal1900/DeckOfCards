
// From BirsaLR 11/16
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DeckUI extends Application {

    @Override
    public void start(Stage stage) {
        DeckView view = new DeckView();

        // new DeckController(view);
        new WarController(view);

        // Builds the layout, will needs to change the numbers later
        Scene scene = new Scene(view.buildUI(), 800, 600);

        stage.setScene(scene);

        stage.setTitle("Card Game");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}