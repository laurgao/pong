
/* HomeScreen class functions like a main menu.
It displays big text like a title screen as well as a button to start the game.
*/
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.function.BiPredicate;

public class HomeScreen {

    public boolean isVisible;
    public boolean isInstructions;
    private String title;
    private PongBall ball;
    private String buttonText;
    private String subtitle;

    private int buttonY;
    private BiPredicate<Integer, Integer> b2IsPressed;
    ChallengeInstructions instructions = new ChallengeInstructions();

    // constructor creates ball at given location with given dimensions
    public HomeScreen(String text, PongBall ball, String buttonText) {
        isVisible = true;
        isInstructions = false;
        this.title = text;
        this.ball = ball;
        this.buttonText = buttonText;
        subtitle = "";
    }

    public void setText(String titleText, String buttonText, String subtitle) {
        this.title = titleText;
        this.buttonText = buttonText;
        this.subtitle = subtitle;
        isVisible = true;
    }

    public void setText(String titleText, String buttonText) {
        // Overload variant of `setText` that makes subtitle empty.
        setText(titleText, buttonText, "");
    }

    public void hide() {
        isInstructions = false;
        isVisible = false;
    }

    // called frequently from the GamePanel class
    // draws the current home screen content to the screen if it is visible
    public void draw(Graphics g) {
        if (isVisible) {
            if (isInstructions) {
                instructions.draw(g);
            } else {
                final Color bgColor = Color.black;
                g.setColor(bgColor);
                g.fillRect(0, 0, GamePanel.W, GamePanel.H);
                g.setColor(CustomColors.emerald400);

                // Draw title
                boolean subtitleExists = subtitle.length() > 0;
                Font subtitleFont = new Font("TimesRoman", Font.ITALIC, 16);
                FontMetrics subMetrics = g.getFontMetrics(subtitleFont);
                int subtitleHeight = subtitleExists ? subMetrics.getHeight() : 0;

                int numChars = title.length();
                int anticipatedTitleWidth = GamePanel.W / 2;
                int widthPerChar = anticipatedTitleWidth / numChars;
                // Get the FontMetrics so we can determine the width of the text so we can
                // center it on screen.
                Font titleFont = new Font("TimesRoman", Font.PLAIN, (widthPerChar + 20));
                g.setFont(titleFont);
                FontMetrics titleMetrics = g.getFontMetrics(titleFont);
                int titleWidth = titleMetrics.stringWidth(title);

                int titleY = GamePanel.H / 2 - titleMetrics.getHeight() / 2;
                if (subtitleExists)
                    titleY -= (subtitleHeight + titleMetrics.getHeight() / 2);
                g.drawString(title, GamePanel.W / 2 - titleWidth / 2, titleY);

                // Draw subtitle
                int subtitleY = GamePanel.H / 2;
                if (subtitleExists) {
                    g.setFont(subtitleFont);
                    g.setColor(CustomColors.emerald500);
                    int subtitleX = GamePanel.W / 2 - subMetrics.stringWidth(subtitle) / 2;
                    g.drawString(subtitle, subtitleX, subtitleY);
                }

                // pseudobutton
                int buttonHeight = 50;
                int marginTop = 20; // margin between title and button
                int marginTopSubtitle = 10; // margin between subtitle and button
                buttonY = subtitleExists ? subtitleY + subMetrics.getHeight() + marginTopSubtitle
                        : titleY + titleMetrics.getHeight() + marginTop;

                g.setColor(CustomColors.emerald300);
                int buttonFontSize = 16;
                Font buttonFont = new Font("TimesRoman", Font.PLAIN, buttonFontSize);
                int paddingX = 10; // padding between button and text
                g.setFont(buttonFont);

                FontMetrics buttonMetrics = g.getFontMetrics(buttonFont);
                int buttonTextWidth = buttonMetrics.stringWidth(buttonText);
                int buttonTextY = buttonY + buttonHeight / 2 + buttonFontSize / 2 - 4;
                int buttonWidth = buttonTextWidth + 2 * paddingX;

                Graphics2D graphics2 = (Graphics2D) g;
                int borderRadius = 10; // radius of rounded corners of the button
                RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(GamePanel.W / 2 - buttonWidth / 2,
                        buttonY,
                        buttonWidth, buttonHeight, borderRadius, borderRadius);
                graphics2.draw(roundedRectangle);
                g.drawString(this.buttonText, GamePanel.W / 2 - buttonTextWidth / 2, buttonTextY);

                // pseudobutton 2
                String b2Text = "Challenge Mode"; // button 2 text
                int period = 2000; // We want the animation to repeat itself every 2000 milliseconds.
                long t = System.currentTimeMillis() % period / 2; // x goes from 0 to 1000
                int b2FontSize = Math.abs(500 - (int) t) / 100 + 16; // font size of button 2 text
                Font b2Font = new Font("TimesRoman", Font.PLAIN, b2FontSize); // button 2 font
                g.setFont(b2Font);
                FontMetrics b2Metrics = g.getFontMetrics(b2Font); // button 2 text metrics
                int b2Width = b2Metrics.stringWidth(b2Text); // button 2 text width
                int button2Y = buttonY + buttonHeight + 40;
                g.drawString(b2Text, GamePanel.W / 2 - b2Width / 2, button2Y);

                b2IsPressed = (x, y) -> {
                    // arguments are x and y coordinates of the mouse
                    // returns true if the mouse is in the button
                    return Math.abs(x - GamePanel.W / 2) < b2Width / 2 && Math.abs(y - button2Y) < buttonHeight / 2;
                };
            }
        }
    }

    public void mousePressed(MouseEvent e, VoidFunction startNormalMode, VoidFunction startChallenge) {
        if (isVisible) {
            if (isInstructions) {
                if (instructions.isClicked(e.getX(), e.getY())) {
                    // Start challenge mode.
                    hide();
                    startChallenge.run();
                    ball.start();
                }
            } else {
                // check if the mouse is in the button when pressed
                if (e.getX() > GamePanel.W / 2 - 50 && e.getX() < GamePanel.W / 2 + 50
                        && e.getY() > buttonY && e.getY() < buttonY + 50) {
                    hide();
                    startNormalMode.run();
                    ball.start();
                }
                if (b2IsPressed.test(e.getX(), e.getY())) {
                    // Open instructions screen
                    isInstructions = true;
                }
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