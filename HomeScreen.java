package pong;

/* Displays big text like a title screen as well as a button to start the game.
*/
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class HomeScreen {

    boolean isVisible;
    String text;
    PongBall ball;
    String buttonText;
    int buttonY;

    // constructor creates ball at given location with given dimensions
    public HomeScreen(String text, PongBall ball, String buttonText) {
        isVisible = true;
        this.text = text;
        this.ball = ball;
        this.buttonText = buttonText;
    }

    public void setText(String text, String buttonText) {
        this.text = text;
        this.buttonText = buttonText;
        isVisible = true;
    }

    public void hide() {
        isVisible = false;
    }

    // called frequently from the GamePanel class
    // draws the current location of the ball to the screen
    public void draw(Graphics g) {
        if (isVisible) {
            final Color bgColor = Color.black;
            g.setColor(bgColor);
            g.fillRect(0, 0, GamePanel.W, GamePanel.H);
            g.setColor(CustomColors.emerald400);
            int numChars = text.length();
            int anticipatedTitleWidth = GamePanel.W / 2;
            int widthPerChar = anticipatedTitleWidth / numChars;
            // Get the FontMetrics so we can determine the width of the text so we can
            // center it on screen.
            Font font = new Font("TimesRoman", Font.PLAIN, (widthPerChar + 15));
            g.setFont(font);
            FontMetrics metrics = g.getFontMetrics(font);
            int textWidth = metrics.stringWidth(text);

            g.drawString(text, GamePanel.W / 2 - textWidth / 2, GamePanel.H / 2 - widthPerChar / 2);

            // pseudobutton
            g.setColor(CustomColors.emerald600);
            int buttonHeight = 50;
            buttonY = GamePanel.H / 2 + widthPerChar / 2;

            Graphics2D graphics2 = (Graphics2D) g;
            // g.fillRect(GamePanel.W / 2 - 50, buttonY, 100, buttonHeight);
            int borderRadius = 10; // radius of rounded corners of the button
            RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(GamePanel.W / 2 - 50, buttonY, 100,
                    buttonHeight, borderRadius, borderRadius);
            graphics2.draw(roundedRectangle);

            g.setColor(CustomColors.emerald300);
            int buttonFontSize = 16;
            g.setFont(new Font("TimesRoman", Font.PLAIN, 16));
            int buttonTextY = buttonY + buttonHeight / 2 + buttonFontSize / 2 - 4;
            g.drawString(this.buttonText, GamePanel.W / 2 - 40, buttonTextY);
        }
    }

    public void mousePressed(MouseEvent e, VoidFunction resetScores) {
        if (isVisible) {
            // check if the mouse is in the button when pressed
            if (e.getX() > GamePanel.W / 2 - 50 && e.getX() < GamePanel.W / 2 + 50
                    && e.getY() > buttonY && e.getY() < buttonY + 50) {
                resetScores.run();
                ball.start();
                hide();
            }
        }
    }

}

@FunctionalInterface
interface VoidFunction {
    // Dummy interface for typing of functions with no inputs and no outputs,
    // so we can pass callback functions as arguments to methods.
    void run();
}