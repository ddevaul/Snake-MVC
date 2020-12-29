package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        // initializes each element of the MVC
        Model model = new Model();
        View view = new View();
        Controller controller = new Controller(view, model);
        AnimationTimer at = new AnimationTimer() {
            public void handle(long l) {
                // draw whats on screen and handle data in MVC
                controller.move();
            }
        };
        controller.setAt(at);
        at.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
