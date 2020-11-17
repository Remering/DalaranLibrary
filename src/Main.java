import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.MainView;

import java.util.concurrent.Semaphore;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(new MainView());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Dalaran Library");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
