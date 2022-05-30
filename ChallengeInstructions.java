// ChallengeInstructions class displays the instructions for the challenge mode

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class ChallengeInstructions {
    int buttonWidth;
    int buttonHeight;
    int buttonY;

    // Helper method to draw a string with word wrapping.
    // Returns the y position of the bottom of the last line.
    private int drawStringWrap(Graphics g, String str, FontMetrics metrics, int initialY) {
        final double lineHeightFactor = 1.2;
        final int marginX = 40;
        String[] words = str.split(" ");
        int lineHeight = (int) (lineHeightFactor * metrics.getHeight());

        String currLine = "";
        int y = initialY;
        for (String word : words) {
            int wordWidth = metrics.stringWidth(word);
            // If adding this word would make the line overflow, draw the current line.
            if (wordWidth + metrics.stringWidth(currLine) > GamePanel.W - 2 * marginX) {
                g.drawString(currLine, marginX, y);
                currLine = "";
                y += lineHeight;
            }
            currLine += word + " ";

        }
        g.drawString(currLine, marginX, y);
        return y + lineHeight;
    }

    public void draw(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, GamePanel.W, GamePanel.H);

        g.setColor(CustomColors.emerald400);
        Font paragraphFont = new Font("Arial", Font.PLAIN, 16);
        FontMetrics metrics = g.getFontMetrics(paragraphFont);
        g.setFont(paragraphFont);
        final int paragraphMargin = 20; // distance between paragraphs
        int y = drawStringWrap(g,
                "In challenge mode, you play against an almost-perfect algorithm that will almost never lose.",
                metrics, 50);
        y = drawStringWrap(g, "The game ends as soon as you lose once.", metrics, y + paragraphMargin);
        y = drawStringWrap(g, "The goal is to last for as much time as you can last without losing.", metrics,
                y + paragraphMargin);

        // Add button
        String buttonText = "Start Game!";
        int buttonTextWidth = metrics.stringWidth(buttonText);
        int padding = 10;
        buttonHeight = metrics.getHeight() + padding * 2;
        buttonY = GamePanel.H / 2 - buttonHeight / 2;
        int buttonTextY = buttonY + buttonHeight / 2 + metrics.getHeight() / 4;
        buttonWidth = buttonTextWidth + 2 * padding;
        Graphics2D graphics2 = (Graphics2D) g;
        int borderRadius = 10; // radius of rounded corners of the button
        RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(GamePanel.W / 2 - buttonWidth / 2, buttonY,
                buttonWidth, buttonHeight, borderRadius, borderRadius);
        graphics2.draw(roundedRectangle);
        g.drawString(buttonText, GamePanel.W / 2 - buttonTextWidth / 2, buttonTextY);
    }

    public boolean isClicked(int x, int y) {
        // x and y are mouse coordinates.
        // returns if mouse coordinates is in the button.
        return Math.abs(x - GamePanel.W / 2) < buttonWidth / 2 && y > buttonY && y < buttonY + buttonHeight;
    }
}
