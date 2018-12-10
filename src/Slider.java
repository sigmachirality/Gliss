/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

import org.w3c.dom.css.Rect;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * An object in the game. 
 *
 * Game objects exist in the game court. They have a position, velocity, size and bounds. Their
 * velocity controls how they move; their position should always be within their bounds.
 */
public class Slider extends Note{

    long endTime;
    int finalXPos;
    boolean endPlayed = false;

    public Slider(Clip mainTrack, long timePos, int xPos, int width, long endTime, int finalXPos){
        super(mainTrack, timePos, xPos, width);
        this.endTime = endTime;
        this.finalXPos = finalXPos;
    }

    public Shape draw(Graphics g, Audio a) {
        long microSecondPos = mainTrack.getMicrosecondPosition();
        int offset = (int)(timePos - microSecondPos);
        int finalOffset = (int)(endTime - microSecondPos);

        int contextHeight = g.getClipBounds().height;
        double slope = -contextHeight / 1000000.0;
        int[] xPoints = {xPos, xPos + width, finalXPos + width, finalXPos};

        int startYPos = contextHeight + (int)(slope * offset);
        int endYPos = contextHeight + (int)(slope * finalOffset);
        int[] yPoints = {startYPos, startYPos, endYPos, endYPos};

        g.setColor(Color.CYAN);
        g.fillPolygon(xPoints, yPoints, 4);

        try {
            if (startYPos >= contextHeight && (!played)) {
                a.playSound(1);
                played = true;
            }
            if (endYPos >= contextHeight && (!endPlayed)) {
                a.playSound(2);
                endPlayed = true;
            }
        } catch (LineUnavailableException e){
            System.out.println(e);
        }
        return new Rectangle(0,0,0,0);
    }
}