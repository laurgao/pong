/* PongBall class defines behaviours for the pong ball  

child of Rectangle because that makes it easy to draw and check for collision
*/
import java.awt.*;

public class PongBall extends Rectangle {

    public final int SPEED = 5; // movement speed of ball
    public static final int D = 20; // diameter of ball

    public double theta = 0; // when velocity in polar coordinates, the angle of velocity in radians.
    // note that this is measured in the principle angle convention (from the
    // positive x-axis counterclockwise)

    // We have xDouble and yDouble variables because we need to keep track of
    // fractional movements,
    // because sometimes velocity defined by `cos(theta) * speed` or `sin(theta) *
    // speed` will be less than 1.
    private double xDouble;
    private double yDouble;

    boolean isMoving;

    // constructor creates ball at given location with given dimensions
    public PongBall(int x, int y) {
        super(x, y, D, D);
        // Generate a random angle from pi/2 to 3pi/2 (so the ball always starts by
        // going towards the player)
        theta = Math.random() * Math.PI + Math.PI / 2;
        xDouble = (double) x;
        yDouble = (double) y;
        isMoving = false;
    }

    public void reset() {
        // reset the ball to the center of the screen
        isMoving = false;
        x = GamePanel.W / 2;
        y = GamePanel.W / 2;
        xDouble = (double) x;
        yDouble = (double) y;
        theta = Math.random() * Math.PI + Math.PI / 2;
    }

    public void start() {
        isMoving = true;
    }

    // called frequently from both PlayerBall class and GamePanel class
    // updates the current location of the ball
    public void move() {
        if (isMoving) {
            // update the location of the ball based on its current velocity
            xDouble += (Math.cos(theta) * SPEED);
            yDouble += (Math.sin(theta) * SPEED);
            x = (int) xDouble;
            y = (int) yDouble;
        }
    }

    // called frequently from the GamePanel class
    // draws the current location of the ball to the screen
    public void draw(Graphics g) {
        Color emerald300 = new Color(110, 231, 183);
        g.setColor(emerald300);
        g.fillOval(x, y, D, D);
        g.drawString("" + theta, 100, 100);
    }

}