package sample;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.awt.*;
import java.util.LinkedList;

public class Controller {
    Model model;
    View view;
    AnimationTimer at;
    double speed;
    double newXVel;
    double newYVel;
    double xVel;
    double yVel;
    int WIDTH;
    int HEIGHT;
    int size;
    boolean keySaved;
    boolean dead;
    boolean started;
    boolean startScreenSet;

    // the constructor takes in data from the model, and initializes parameters in the View
    public Controller(View a, Model b) {
        this.model = b;
        this.size = b.getBodySegSize();
        this.WIDTH = b.getWIDTH();
        this.HEIGHT = b.getHEIGHT();
        this.speed = b.getSpeed();
        this.view = a;
        this.xVel = speed;
        a.setSize(b.getBodySegSize());
        a.setSpeed(b.getSpeed());
        a.setHEIGHT(400);
        a.setWIDTH(600);
        a.setUp();
        a.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent e) {
                KeyCode code;
                if (!keySaved) {
                    code = e.getCode();
                    switch (code) {
                        case UP:
                            if (yVel == 0) {
                                newYVel = -speed;
                                newXVel = 0;
                                keySaved = true;
                            }
                            break;
                        case DOWN:
                            if (yVel == 0) {
                                newYVel = speed;
                                newXVel = 0;
                                keySaved = true;
                            }
                            break;
                        case LEFT:
                            if (xVel == 0) {
                                newYVel = 0;
                                newXVel = -speed;
                                keySaved = true;
                            }
                            break;
                        case RIGHT:
                            if (xVel == 0) {
                                newYVel = 0;
                                newXVel = speed;
                                keySaved = true;
                            }
                            break;
                    }
                }
            }
        });
        generateApple();
    }
    // changes the snake's position, and deals with conditions such as the snake dying or eating an apple
    public void move() {
        // will only move the snake if the startScreen() method has been called AND the EventHandler
        // on the start screen has set started = true;
        // the startScreenSet boolean keeps this snippet of code from running multiple times and from
        // repeatedly calling the startScreen() method which would add a lot of labels to the root
        if(!started && !startScreenSet){
            view.startScreen(this);
            startScreenSet = true;
        } else if (started){
            LinkedList<Point> snake = model.getSnake();
            assert snake.peekFirst() != null;
            double snakeX = snake.peekFirst().getX();
            assert snake.peekFirst() != null;
            double snakeY = snake.peekFirst().getY();
            int snakeListLength = model.getSnakeListLength();
            Point aLocation = model.getAppleLocation();
            // would be a beast move to have this be multi-threaded so that the apple stays on screen
            // a little bit longer
            // increases the snake's length if it gets an apple, and generates a new apple
            if (Math.abs(snakeX - aLocation.getX()) < size && Math.abs(snakeY - aLocation.getY()) < size) {
                model.setSnakeListLength(snakeListLength + (int) (size / speed));
                generateApple();
                model.setScore(model.getScore() + 1);
            }
            // this if statement works by saying, if a key has been pressed and the velocity has not yet changed
            // (meaning keySaved = true)
            // and the snake is only barely past the beginning of the box, then change the snake's position to
            // be perfectly within the box and then change the velocities
            // by checking if the remained is less than 4, I am trying to create a margin for error by updating the
            // velocities if the snake head is in the first four pixels of the box, and putting the snake
            // head back inside the grid
            if (snakeX % size <= 4 && snakeY % size <= 4 && keySaved) {
                if (Math.abs(newYVel) > 0) {
                    snakeX = snakeX - snakeX % size;
                } else if (Math.abs(newXVel) > 0) {
                    snakeY = snakeY - snakeY % size;
                }
                xVel = newXVel;
                yVel = newYVel;
                keySaved = false;
            }
            // these are where the snake head would be if the position was updated, this allows to check
            // to see if that move will kill the snake
            double tempX = snakeX + xVel;
            double tempY = snakeY + yVel;
            // this checks to see if the point in the future will overlap with any parts of the snake's
            // body, and if so, it kills the snake
            for (Point i : snake) {
                if (Math.abs(tempX - i.getX()) < speed && Math.abs(tempY - i.getY()) < speed) {
                    dead = true;
                    break;
                }
            }
            // if the snake hits a barrier, the snake is dead, otherwise, update the x and y coordinates
            if (snakeY <= HEIGHT - size && snakeY >= 0) {
                snakeY += yVel;
            } else {
                dead = true;
            }
            if (snakeX <= WIDTH - size && snakeX >= 0) {
                snakeX += xVel;
            } else {
                dead = true;
            }
            // add this new point to the snake LinkedList
            Point p = new Point((int) snakeX, (int) snakeY);
            snake.push(p);
            // build up until the size of the snake, thus if the size of the snake is increased, it stores
            // more points in the body to accommodate. specifically, it stores one more Point each
            // time the move() method is called in the Animation Timer in the main method. if the snake is already
            // the correct length, then it removes the last point to keep it the correct length
            if (snake.size() >= snakeListLength) {
                snake.removeLast();
            }
            // update the model and the view
            model.setSnake(snake);
            view.setSnake(snake);
            // if the snake is dead then stop the animation and give the option to the player to restart, otherwise
            // update the score and draw the updated position of the snake and draw the apple, updated or not
            if (dead) {
                at.stop();
                view.addRestartButton(this);
            } else {
                view.getScoreLabel().setText("Score: " + model.getScore());
                view.draw();
            }
        }
    }

    // this will generate random coordinates for an apple, then it will tell the model and
    // the view the new coordinates, has to make sure the apple is not where the snake is!
    public void generateApple() {
        LinkedList<Point> snake = model.getSnake();
        double snakeX = snake.peekFirst().getX();
        double snakeY = snake.peekFirst().getY();
        while (true) {
            int xCo = (int) (Math.random() * WIDTH);
            int yCo = (int) (Math.random() * HEIGHT);
            xCo = xCo - (xCo % size);
            yCo = yCo - (yCo % size);
            Point p = new Point(xCo, yCo);
            if (xCo != snakeX && yCo != snakeY && !snake.contains(p)) {
                view.setAppleLocation(p);
                model.setAppleLocation(p);
                break;
            }
        }
    }
    // next two methods are just simple setters used above
    public void setStarted(boolean started) {
        this.started = started;
    }

    public void setAt(AnimationTimer at) {
        this.at = at;
    }
    // resets the controller
    public void reset() {
        xVel = speed;
        yVel = 0;
        generateApple();
    }
    // resets the controller, view, and model and starts the animation timer again
    public void resetAll() {
        dead = false;
        view.reset();
        this.reset();
        model.reset();
        at.start();
    }
}
