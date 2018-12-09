/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

import javax.swing.*;
import java.awt.*;
import javax.sound.sampled.Clip;

/**
 * An object in the game. 
 *
 * Game objects exist in the game court. They have a position, velocity, size and bounds. Their
 * velocity controls how they move; their position should always be within their bounds.
 */
public class Note{

    private long timePos;
    private int xPos;
    private int width;
    private Clip mainTrack;

    public Note(Clip mainTrack, long timePos, int xPos, int width){
        this.mainTrack = mainTrack;
        this.timePos = timePos;
        this.xPos = xPos;
        this.width = width;
    }

    public int scoreNote(){
        long offset = Math.abs(mainTrack.getMicrosecondPosition() - timePos);
        if (offset <= 31.5) return 300;
        if (offset <= 75.5) return 100;
        if (offset <= 119.5) return 50;
        return 0;
    }

    protected boolean checkKeyPress(){
        return false;
    }

    //TODO: Scale y position dynamically based on where shit be going down boi
    public void draw(Graphics g) {
        int offset = (int) Math.abs(mainTrack.getMicrosecondPosition() - timePos);
        int contextHeight = g.getClipBounds().height;
        g.setColor(Color.WHITE);
        g.fillRect(xPos, offset/600, width, (int) Math.round(contextHeight*.025));
    }
}