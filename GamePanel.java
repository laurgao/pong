package pong;

/* GamePanel class acts as the main "game loop" - continuously runs the game and calls whatever needs to be called

Child of JPanel because JPanel contains methods for drawing to the screen

Implements KeyListener interface to listen for keyboard input

Implements Runnable interface to use "threading" - let the game do two things at once

*/
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable, KeyListener {

    // dimensions of window
    public static final int W = 500; // width of window
    public static final int H = 500; // height of window

    public static final int WINNING_SCORE = 5;

    public Thread gameThread;
    public Image image;
    public Graphics graphics;
    PongBall ball;
    Paddle playerPaddle;
    Paddle computerPaddle;
    int playerScore;
    int computerScore;
    HomeScreen screen;

    boolean isSleeping;

    public GamePanel() {
        // create a ball, set start location to middle of screen
        ball = new PongBall(W / 2, H / 2);
        // create a player-controlled paddle, set start location to middle of screen
        // left side
        playerPaddle = new Paddle(0, H / 2 - Paddle.H / 2);
        // create a computer-controlled paddle, set start location to middle of screen
        // right side
        computerPaddle = new Paddle(W - Paddle.W, H / 2 - Paddle.H / 2);
        computerPaddle.setYVelocity(computerPaddle.SPEED);

        // add the MousePressed method from the MouseAdapter - by doing this we can
        // listen for mouse input. We do this differently from the KeyListener because
        // MouseAdapter has SEVEN mandatory methods - we only need one of them, and we
        // don't want to make 6 empty methods
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                screen.mousePressed(e, () -> {
                    playerScore = 0;
                    computerScore = 0;
                });
            }
        });

        playerScore = 0;
        computerScore = 0;
        screen = new HomeScreen("PONG!", ball, "Start Game");

        this.setPreferredSize(new Dimension(W, H));
        this.setFocusable(true); // make everything in this class appear on the screen
        this.addKeyListener(this); // start listening for keyboard input

        // make this class run at the same time as other classes (without this each
        // class would "pause" while another class runs). By using threading we can
        // remove lag, and also allows us to do features like display timers in real
        // time!
        gameThread = new Thread(this);
        gameThread.start();
    }

    // paint is a method in java.awt library that we are overriding. It is a special
    // method - it is called automatically in the background in order to update what
    // appears in the window. You NEVER call paint() yourself
    public void paint(Graphics g) {
        // we are using "double buffering here" - if we draw images directly onto the
        // screen, it takes time and the human eye can actually notice flashes of lag as
        // each pixel on the screen is drawn one at a time. Instead, we are going to
        // draw images OFF the screen, then simply move the image on screen as needed.
        image = createImage(W, H); // draw off screen
        graphics = image.getGraphics();
        draw(graphics);// update the positions of everything on the screen
        g.drawImage(image, 0, 0, this); // move the image on the screen

    }

    // call the draw methods in each class to update positions as things move
    public void draw(Graphics g) {
        // draw the background
        Color bgColor = CustomColors.emerald50;
        g.setColor(bgColor);
        g.fillRect(0, 0, GamePanel.W, GamePanel.H);

        // draw tracks that the paddles travel on
        g.setColor(CustomColors.emerald100);
        g.fillRect(0, 0, Paddle.W, H);
        g.fillRect(W - Paddle.W, 0, Paddle.W, H);

        // draw the objects
        ball.draw(g);
        playerPaddle.draw(g);
        computerPaddle.draw(g);
        screen.draw(g); // draw the screen last so it is on top of everything else
        drawScore(g);
    }

    public void drawScore(Graphics g) {
        if (screen.isVisible)
            g.setColor(CustomColors.emerald900);
        else
            g.setColor(CustomColors.emerald400);

        int scoreFontSize = 50;
        Font scoreFont = new Font("Arial", Font.ITALIC, scoreFontSize);
        g.setFont(scoreFont);

        // Do some math to center the scores in their respective halves of the screen
        FontMetrics metrics = g.getFontMetrics(scoreFont);
        int marginTop = 80; // distance from top of window
        String pT = Integer.toString(playerScore); // player score text
        int pW = metrics.stringWidth(pT); // player score text width
        int pCx = W / 4; // player score text center x
        int pX = pCx - pW / 2; // player score text left corner x coordinate

        String cT = Integer.toString(computerScore); // computer score text
        int cW = metrics.stringWidth(cT); // computer score text width
        int cCx = 3 * W / 4; // computer score text center x
        int cX = cCx - cW / 2; // computer score text left corner x coordinate

        g.drawString(pT, pX, marginTop);
        g.drawString(cT, cX, marginTop);
    }

    // call the move methods in other classes to update positions
    // this method is constantly called from run(). By doing this, movements appear
    // fluid and natural. If we take this out the movements appear sluggish and
    // laggy
    public void move() {
        ball.move();
        playerPaddle.move();
        computerPaddle.move();
        // move the computer paddle towards the ball if ball is moving towards the
        // computer paddle.
        if (Math.cos(ball.theta) > 0) {
            // Only change direction if ball is sufficiently far away from paddle.

            final int error = 40; // the ball will be this many pixels away from the paddle before it changes
                                  // direction
            // this is so the algorithm will sometimes miss the ball, which gives the
            // player a chance of winning.

            int paddleCy = (computerPaddle.y + Paddle.H / 2); // center y coordinate of paddle
            int ballCy = (ball.y + PongBall.D / 2); // center y coordinate of ball
            if (Math.abs(paddleCy - ballCy) > (Paddle.H / 2 + error)) {
                if (paddleCy < ballCy) {
                    // If paddle is above ball, move paddle down
                    computerPaddle.setYVelocity(computerPaddle.SPEED);
                } else if (computerPaddle.y > ball.y) {
                    computerPaddle.setYVelocity(-computerPaddle.SPEED);
                }
            }
        } else {
            // If ball is moving away from paddle, stop the paddle.
            computerPaddle.setYVelocity(0);
        }

        // Check for collision and update theta accordingly.

        // Does ball hit paddle?
        if ((ball.x + PongBall.D) >= playerPaddle.x
                && ball.x <= playerPaddle.x + Paddle.W
                && ball.y + PongBall.D >= playerPaddle.y && ball.y <= playerPaddle.y + Paddle.H) {
            ball.theta = Math.PI - ball.theta;
            ball.theta += Math.random() * 0.1 - 0.05; // add a little bit of randomness to the angle each time ball
                                                      // bounces.
        }
        // Does hall hit computer paddle?
        else if (ball.x + PongBall.D >= computerPaddle.x
                && ball.x <= computerPaddle.x + Paddle.W
                && ball.y + PongBall.D >= computerPaddle.y
                && ball.y <= computerPaddle.y + Paddle.H) {
            ball.theta = Math.PI - ball.theta;
            ball.theta += Math.random() * 0.1 - 0.05;
        } else if (ball.y <= 0 || ball.y >= GamePanel.H - PongBall.D) {
            // If ball hits top or bottom of screen
            // do a bounce
            ball.theta *= -1;
            ball.theta += Math.random() * 0.1 - 0.05;
        } else if (ball.x <= 0) {
            // If ball hits left or right of screen (and not paddle), the game loses.
            // computer wins
            computerScore++;
            ball.reset();
            if (computerScore >= WINNING_SCORE)
                screen.setText("Computer wins!", "Play again?");
            else {
                // Wait 1 second with the ball in the middle of the screen before the ball
                // starts moving.
                sleep();
                ball.start();
            }
        } else if (ball.x >= GamePanel.W - PongBall.D) {
            playerScore++;
            ball.reset();
            if (playerScore >= WINNING_SCORE)
                screen.setText("Player wins!", "Play again?");
            else {
                sleep();
                ball.start();
            }
        }

        // Normalize theta to be between -pi and pi
        if (ball.theta > Math.PI) {
            ball.theta -= 2 * Math.PI;
        } else if (ball.theta < -Math.PI) {
            ball.theta += 2 * Math.PI;
        }

        // if theta is too close to pi/2 or -pi/2, then the ball will bounce from top
        // wall to bottom wall back and forth for too long, which makes the game slow
        // and boring for the player. thus, we will make it a little
        // farther away from pi/2 and -pi/2.
        double threshold = 0.5;
        if (Math.abs(ball.theta - Math.PI / 2) < threshold) {
            System.out.println("too close to pi/2");
            if (ball.theta < Math.PI / 2)
                ball.theta = Math.PI / 2 - threshold - 0.1;
            else
                ball.theta = Math.PI / 2 + threshold + 0.1;
        } else if (Math.abs(ball.theta + Math.PI / 2) < threshold) {
            System.out.println("too close to -pi/2");
            if (ball.theta < -Math.PI / 2)
                ball.theta = -Math.PI / 2 - threshold - 0.1;
            else
                ball.theta = -Math.PI / 2 + threshold + 0.1;
        }
    }

    public void sleep() {
        // Sleeps for 1 second
        isSleeping = true;
    }

    // run() method is what makes the game continue running without end. It calls
    // other methods to move objects, check for collision, and update the screen
    public void run() {
        // the CPU runs our game code too quickly - we need to slow it down! The
        // following lines of code "force" the computer to get stuck in a loop for short
        // intervals between calling other methods to update the screen.
        long lastTime = System.nanoTime();
        final double amountOfTicks = 60;
        final double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long now;
        long timeWhenSleepingStarted = 0;

        while (true) { // this is the infinite game loop
            now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (!isSleeping) {
                // only move objects around and update screen if enough time has passed
                if (delta >= 1) {
                    move();
                    repaint();
                    delta--;
                }
            } else {
                if (timeWhenSleepingStarted == 0) {
                    timeWhenSleepingStarted = System.nanoTime();
                } else {
                    if (System.nanoTime() - timeWhenSleepingStarted > 1000000000) {
                        delta = 0;
                        isSleeping = false;
                        timeWhenSleepingStarted = 0;
                    }
                }
            }
        }
    }

    // if a key is pressed, we'll send it over to the player paddle object for
    // processing
    public void keyPressed(KeyEvent e) {
        // if is sleeping, then ignore key presses
        if (!isSleeping)
            playerPaddle.keyPressed(e);
    }

    // if a key is released, we'll send it over to the player paddle object for
    // processing
    public void keyReleased(KeyEvent e) {
        playerPaddle.keyReleased(e);
    }

    // left empty because we don't need it; must be here because it is required to
    // be overridded by the KeyListener interface
    public void keyTyped(KeyEvent e) {

    }
}