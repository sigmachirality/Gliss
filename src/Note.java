/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

import javax.swing.*;
import java.awt.*;

/**
 * An object in the game. 
 *
 * Game objects exist in the game court. They have a position, velocity, size and bounds. Their
 * velocity controls how they move; their position should always be within their bounds.
 */
public class Note extends JLabel{

    private int timePos;
    private int xPos;
    private int width;

    public Note(int timePos, int xPos, int width){
    }

    //TODO: Scale y position dynamically based on where shit be going down boi
    @Override
    public void paintComponent(Graphics g) {
        int contextHeight = g.getClipBounds().height;
        g.setColor(Color.WHITE);
        g.fillRect(xPos, 0, width, (int) (contextHeight * 0.05));
    }
}