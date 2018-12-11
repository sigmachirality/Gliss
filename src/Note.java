/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

import javax.sound.sampled.LineUnavailableException;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import javax.sound.sampled.Clip;

public class Note{

    protected long timePos;
    protected int xPos;
    protected int width;
    protected Clip mainTrack;
    protected boolean played = false;
    protected boolean scored = false;

    public Note(Clip mainTrack, long timePos, int xPos, int width){
        this.mainTrack = mainTrack;
        this.timePos = timePos;
        this.xPos = xPos;
        this.width = width;
    }

    public int scoreNote(){
        scored = true;
        return 10;
    }

    public Shape draw(Graphics g, Audio a) {
        int contextHeight = g.getClipBounds().height;
        g.setColor(Color.WHITE);
        if (scored) g.setColor(Color.GREEN);
        int yPos = getYPos(g);

        Rectangle2D rect = new Rectangle(0,0,0,0);
        if (yPos >= 0) {
            rect = new Rectangle(xPos, yPos, width, (int) Math.round(contextHeight * .025));
            g.fillRect(xPos, yPos, width, (int) Math.round(contextHeight * .025));
        }

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

    private int getYPos(Graphics g){
        int offset = (int)(timePos - mainTrack.getMicrosecondPosition());
        int contextHeight = g.getClipBounds().height;
        double slope = -contextHeight / 1000000.0;
        int yPos = contextHeight + (int)(slope * offset);
        return yPos;
    }

    public int getXPos(){
        return xPos;
    }

    public boolean noteMissed(Graphics g){
        return (getYPos(g) >= 1280) && (getYPos(g) <= 1400) && !scored;
    }


    public long getTimePos (){
        return timePos;
    }
}