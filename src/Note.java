/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import javax.sound.sampled.Clip;

/**
 * An object in the game. 
 *
 * Game objects exist in the game court. They have a position, velocity, size and bounds. Their
 * velocity controls how they move; their position should always be within their bounds.
 */
public class Note{

    protected long timePos;
    protected int xPos;
    protected int width;
    protected Clip mainTrack;
    protected boolean played = false;
    private boolean scored = false;

    public Note(Clip mainTrack, long timePos, int xPos, int width){
        this.mainTrack = mainTrack;
        this.timePos = timePos;
        this.xPos = xPos;
        this.width = width;
    }

    public int scoreNote(){
        long offset = Math.abs(mainTrack.getMicrosecondPosition() - timePos);
        if (offset <= 31500) return 300;
        if (offset <= 75500) return 100;
        if (offset <= 119500) return 50;
        scored = true;
        return 0;
    }

    //TODO: Scale y position dynamically based on where shit be going down boi
    public Shape draw(Graphics g, Audio a) {
        int offset = (int)(timePos - mainTrack.getMicrosecondPosition());
        int contextHeight = g.getClipBounds().height;
        g.setColor(Color.WHITE);
        double slope = -contextHeight / 1000000.0;
        int yPos = contextHeight + (int)(slope * offset);
        Rectangle2D rect = new Rectangle(xPos, yPos, width, (int) Math.round(contextHeight*.025));
        g.fillRect(xPos, yPos, width, (int) Math.round(contextHeight*.025));

        try {
            if (yPos >= contextHeight && (!played)) {
                a.playSound(1);
                played = true;
            }
        } catch (LineUnavailableException e){
            System.out.println(e);
        }

        return rect;
    }

    public String toString(){
        return "" + timePos;
    }
}