/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

// imports necessary libraries for Java swing
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.sound.sampled.*;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {
    public void run() {
        // NOTE : recall that the 'final' keyword notes immutability even for local variables.
        // Be sure to change "TOP LEVEL FRAME" to the name of your game
        final JFrame frame = new JFrame("Gliss");
        frame.setLocation(0, 0);

        Audio a = new Audio();

        // Main playing area
        final GameCourt court = new GameCourt(a);
        frame.add(court, BorderLayout.CENTER);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        //Setup sound and level
        Clip mainClip;
        try{
            //mainClip = a.addClip("maps/" + "track.WAV");
        }
        catch (Exception e){
            System.out.println(e);
        }



        // Start game
        court.play();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }

}