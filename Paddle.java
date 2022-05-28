
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

    public Paddle(int x, int y) {
        super(x, y, W, H);
    }

    // called from GamePanel when any keyboard input is detected
    // updates the direction of the paddle based on user input
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_UP) {
            setYVelocity(SPEED * -1);
            move();
        }

        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            setYVelocity(SPEED);
            move();
        }
    }

    // called from GamePanel when any key is released
    // Makes the paddle stop moving in that direction
    public void keyReleased(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_UP) {
            setYVelocity(0);
            move();
        }

        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            setYVelocity(0);
            move();
        }
    }

    // called whenever the paddle changes in the y-direction (up/down)
    public void setYVelocity(int yVelocity) {
        this.yVelocity = yVelocity;
    }

    // called frequently from both Paddle class and GamePanel class
    // updates the current location of the paddle
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
    // draws the current location of the paddle to the screen
    public void draw(Graphics g) {
        g.setColor(CustomColors.emerald600);
        g.fillRect(x, y, width, height);
    }

}