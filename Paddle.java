package pong;

/* Paddle class defines behaviours for both the computer- and player-controlled paddles.  

child of Rectangle because that makes it easy to draw and check for collision
*/
import java.awt.*;
import java.awt.event.*;

public class Paddle extends Rectangle {

    public int yVelocity;
    public final int SPEED = 5; // movement speed of ball
    public static final int W = 15; // width of paddle
    public static final int H = 75; // height of paddle

    // constructor creates ball at given location with given dimensions
    public Paddle(int x, int y) {
        super(x, y, W, H);
    }

    // called from GamePanel when any keyboard input is detected
    // updates the direction of the ball based on user input
    // if the keyboard input isn't any of the options (d, a, w, s), then nothing
    // happens
    public void keyPressed(KeyEvent e) {

        if (e.getKeyChar() == 'w') {
            setYVelocity(SPEED * -1);
            move();
        }

        if (e.getKeyChar() == 's') {
            setYVelocity(SPEED);
            move();
        }
    }

    // called from GamePanel when any key is released (no longer being pressed down)
    // Makes the ball stop moving in that direction
    public void keyReleased(KeyEvent e) {

        if (e.getKeyChar() == 'w') {
            setYVelocity(0);
            move();
        }

        if (e.getKeyChar() == 's') {
            setYVelocity(0);
            move();
        }
    }

    // called whenever the paddle changes in the y-direction (up/down)
    public void setYVelocity(int yVelocity) {
        this.yVelocity = yVelocity;
    }

    // called frequently from both Paddle class and GamePanel class
    // updates the current location of the ball
    public void move() {
        y += yVelocity;
        // if the paddle is going off the screen, then move it back to the edge
        if (y < 0) {
            y = 0;
        } else if (y > GamePanel.H - Paddle.H) {
            y = GamePanel.H - Paddle.H;
        }

    }

    // called frequently from the GamePanel class
    // draws the current location of the ball to the screen
    public void draw(Graphics g) {
        Color emerald600 = new Color(5, 150, 105);
        g.setColor(emerald600);
        g.fillRect(x, y, width, height);
    }

}