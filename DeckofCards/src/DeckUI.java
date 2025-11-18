import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DeckUI extends Application {

    @Override
    public void start(Stage stage) {
        DeckView view = new DeckView();

        new DeckController(view);

        // Builds the layout, will needs to change the numbers later
        Scene scene = new Scene(view.buildUI(), 600, 400);
        
        stage.setScene(scene);

        stage.setTitle("Deck of Cards");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
