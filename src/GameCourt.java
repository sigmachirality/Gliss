/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

import java.awt.*;
import javax.swing.*;
import java.io.*;
import javax.sound.sampled.*;
import java.awt.event.*;

/**
 * GameCourt
 * 
 * This class holds the primary game logic for how different objects interact with one another.
 * Using concurrency it checks the keymap stored TODO: somewhere else I guess lmao
 */
@SuppressWarnings("serial")
public class GameCourt extends JPanel {

    //TODO: make folder dynamic
    String mapFolder = "maps/";
    String assetsFolder = "files/";

    //Local state
    AudioManager a;
    Clip mainClip;


    public GameCourt(AudioManager a) {
        this.a = a;
        try {
            a.addClip(mapFolder + "track.WAV");
            a.addClip(assetsFolder + "sound-start.wav");
            a.addClip(assetsFolder + "sound-end.wav");
        } catch (IOException e){
            System.out.println("IOException: " + e);
        } catch (UnsupportedAudioFileException e){
            System.out.println("Unsupported Audio File: " + e);
        }
    }

    public void play() {
        // creates border around the court area, and sets play area background to black
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setBackground(Color.BLACK);

        try{
            mainClip = a.playSound(0);
        } catch (LineUnavailableException e){
            System.out.println("Line unavailable: " + e);
        }

        //TODO: Reimplement timer
        // The timer is an object which triggers an action periodically with the given INTERVAL. We
        // register an ActionListener with this timer, whose actionPerformed() method is called each
        // time the timer triggers. We define a helper method called tick() that actually does
        // everything that should be done in a single timestep.
        /*Timer timer = new Timer(INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });*
        timer.start(); // MAKE SURE TO START THE TIMER!
        */
        // Enable keyboard focus on the court area.
        // When this component has the keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        // This key listener allows the square to move as long as an arrow key is pressed, by
        // changing the square's velocity accordingly. (The tick method below actually moves the
        // square.)
        addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    try{
                        a.playSound(1);
                        System.out.println(mainClip.getMicrosecondPosition());
                    } catch (LineUnavailableException ex){
                        System.out.println("Line unavailable: " + ex);
                    }
                }
            }

            /*public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    endSound.start();
                }
            }*/
        });
    }


    /**
     * This method is called every time the timer defined in the constructor triggers.
     */
    //void tick() {
        /*
        }
        if (playing) {
            // advance the square and snitch in their current direction.
            square.move();
            snitch.move();

            // make the snitch bounce off walls...
            snitch.bounce(snitch.hitWall());
            // ...and the mushroom
            snitch.bounce(snitch.hitObj(poison));

            // check for the game end conditions
            if (square.intersects(poison)) {
                playing = false;
                status.setText("You lose!");
            } else if (square.intersects(snitch)) {
                playing = false;
                status.setText("You win!");
            }

            // update the display
            repaint();
        }
        */
    //}

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1280, 720);
    }
}