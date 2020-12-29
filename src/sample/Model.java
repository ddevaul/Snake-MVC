package sample;

import java.awt.*;
import java.util.LinkedList;

public class Model {
    // note to self: add a difficulty setting, and add another button to reset that allows you to reselect the
    // difficulty settings. maybe change height and stuff back to global variables
    Point appleLocation;
    int score;
    //  the snake works by stacking the head's position on the snake, and then passing it down
    public LinkedList<Point> snake = new LinkedList<>();
    private int speed = 3;
    public int numBodyParts = 3;
    private final int bodySegSize = 25;
    // the snake will have Points stored speed distance apart, but if you drew only those Points they
    // would be overlapping. instead you need to save enough Points so that you can skip drawing some
    // in the view.draw() method.
    // the reason the list size needs to be calculated in this way can be best understood from an example:
    // if you want 3 body segments for a snake of 10 pixels long, and the snake is moving at 2 pixels per of the controller.move()
    // method, then you need to draw every 5th Point to have a snake that doesn't overlap. Therefore, you would
    // need 11 total points for the snakes body, drawing at the 1st point, the 6th point, and the 11th point
    public int snakeListLength = bodySegSize/speed*(numBodyParts) + 1;

    public Model() {
        // fills out the beginning snake
        for (int i = 0; i < snakeListLength ;i++ ) {
            int startingX = 75;
            int startingY = 200;
            snake.add(new Point(startingX -i*speed, startingY));
        }
    }
    public double getSpeed() {
        return speed;
    }
    // to be used for difficulty settings
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    public LinkedList<Point> getSnake() {
        return snake;
    }
    public void setSnake(LinkedList<Point> snake) {
        this.snake = snake;
    }
    public int getBodySegSize() {
        return bodySegSize;
    }
    // this is slightly redundant, but I'm debating making WIDTH and HEIGHT instance variables just
    // because they will be easier to find
    public int getWIDTH() {
        int WIDTH = 600;
        return WIDTH;
    }
    public int getHEIGHT() {
        int HEIGHT = 400;
        return HEIGHT;
    }
    public void setAppleLocation(Point appleLocation) {
        this.appleLocation = appleLocation;
    }
    public Point getAppleLocation() {
        return appleLocation;
    }
    public int getSnakeListLength(){
        return snakeListLength;
    }
    public void setSnakeListLength(int x){
        this.snakeListLength = x;
    }
    // resets the model by  redoing the actions done in the constructor
    public void reset(){
        snake.clear();
        score = 0;
        this.numBodyParts = 3;
        this.snakeListLength = bodySegSize/speed*numBodyParts;
        for (int i = 0; i < snakeListLength ;i++ ) {
            int startingX = 75;
            int startingY = 200;
            snake.add(new Point(startingX - i * speed, startingY));
        }
    }
    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
}
