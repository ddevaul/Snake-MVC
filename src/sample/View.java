package sample;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.awt.Point;
import java.util.LinkedList;

public class View {
    int WIDTH;
    int HEIGHT;
    int size;
    double speed; // this is only used for a calculation, I suppose I could do without it
    Scene scene;
    Stage primaryStage;
    StackPane root;
    Canvas c;
    GraphicsContext gc;
    LinkedList<Point> snake;
    Point appleLocation;
    Label scoreLabel;

    // this method, when called by the controller, initializes the GUI. note also that this method is called
    // after many of the instance variables are initialized by the controller
    public void setUp(){
        scoreLabel = new Label();
        root = new StackPane();
        primaryStage = new Stage();
        c = new Canvas(WIDTH, HEIGHT);
        scoreLabel.setText("Score: 0");
        scoreLabel.setPrefHeight(40);
        scoreLabel.setFont(Font.font("Cambria", 32));
        scoreLabel.setTextFill(Color.GHOSTWHITE);
        StackPane.setAlignment(c, Pos.BOTTOM_CENTER);
        StackPane.setAlignment(scoreLabel, Pos.TOP_CENTER);
        root.getChildren().addAll(c, scoreLabel);
        gc = c.getGraphicsContext2D();
        primaryStage.setTitle("Snake");
        scene = new Scene(root, WIDTH, HEIGHT + 40);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    // draws the gc which functions as the background, and then draws the snake, segment by segment, and
    // then the apple
    public void draw(){
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, WIDTH, HEIGHT);
        drawGrid();
        gc.setFill(Color.GREEN);
        for (int i = 0; i < snake.size(); i+=size/speed){
            Point a = snake.get(i);
       //     System.out.println(a);
            gc.fillOval(a.getX(), a.getY(), size, size);
        }
        drawApple(appleLocation);
    }
    // this method, called in the draw method, draws the grid lines
    public void drawGrid(){
        // start with drawing vertical lines
        gc.setStroke(Color.GHOSTWHITE);
        for (int i = 0; i < WIDTH ; i+=size ) {
            gc.strokeLine(i, 0, i, HEIGHT);
        }
        // draw horizontal lines
        for (int i = 0; i < HEIGHT ; i+=size ) {
            gc.strokeLine(0, i, WIDTH, i);
        }
    }
    public void drawApple(Point p){
        gc.setFill(Color.RED);
        gc.fillOval(p.getX(), p.getY(), size, size);
    }
    // draws the start screen which is shown when the application is first run, the label receives the Focus
    // so, the event handler doesn't have to be later removed from the root
    public void startScreen(Controller vc){
        root.setFocusTraversable(true);
        root.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        Label b = new Label("Press Any Key to Start");
        b.setTextFill(Color.GHOSTWHITE);
        EventHandler<KeyEvent> keyEventEventHandler = new EventHandler<>() {
            public void handle(KeyEvent keyEvent) {
                vc.setStarted(true);
                root.setBackground(new Background(new BackgroundFill(Color.DARKBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                b.removeEventHandler(KeyEvent.KEY_TYPED, this);
                reset();
            }
        };
        b.setOnKeyPressed(keyEventEventHandler);
        root.getChildren().add(b);
        b.requestFocus();
        scoreLabel.setText("");
    }
    // adds the button at the end when the player dies. when the R key is pressed the game is reset
    public void addRestartButton(Controller vc){
        root.setFocusTraversable(true);
        Button replay = new Button("Press 'R' to Replay");
        root.getChildren().add(replay);
        EventHandler<KeyEvent> keyEventEventHandler = new EventHandler<>() {
            public void handle(KeyEvent keyEvent) {
                KeyCode code =  keyEvent.getCode();
                if (code == KeyCode.R){
                    vc.resetAll();
                }
            }
        };
        replay.setOnKeyPressed(keyEventEventHandler);
        replay.requestFocus();
    }
    // this removes the last node which was added, which is either the reset button or the start button, and then
    // it makes sure the root has the Focus
    public void reset(){
        int last = root.getChildren().size();
        root.getChildren().remove(last -1);
        root.requestFocus();
        root.setFocusTraversable(false);
    }
    // various getters and setters. Most are setters with the exception of the getter for the scoreLabel
    // which is updated directly in the ViewController
    public void setSnake(LinkedList<Point> s) {
        this.snake = s;
    }
    public void setSize(int size) {
        this.size = size;
    }
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setWIDTH(int WIDTH) {
        this.WIDTH = WIDTH;
    }

    public void setHEIGHT(int HEIGHT) {
        this.HEIGHT = HEIGHT;
    }
    public void setAppleLocation(Point appleLocation) {
        this.appleLocation = appleLocation;
    }

    public Scene getScene() {
        return scene;
    }
    public Label getScoreLabel() {
        return scoreLabel;
    }
}

