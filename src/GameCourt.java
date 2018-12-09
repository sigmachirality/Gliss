import java.awt.*;
import javax.swing.*;
import java.io.*;
import javax.sound.sampled.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class GameCourt extends JPanel {

    //TODO: make folder dynamic
    private String mapFolder = "maps/";
    private String assetsFolder = "files/";

    //Local state
    private Audio a;
    private Clip mainClip;
    private volatile long gameTime;

    private Note n;

    public GameCourt(Audio a) {
        this.a = a;
        try {
            a.addClip(mapFolder + "track.WAV");
            a.addClip(assetsFolder + "sound-start.wav");
            a.addClip(assetsFolder + "sound-end.wav");
        } catch (IOException e){
            System.out.println("IOException: " + e);
        } catch (UnsupportedAudioFileException e) {
            System.out.println("Unsupported Audio File: " + e);
        }
    }

    public void play() {
        // creates border around the court area, and sets play area background to black
        setBackground(Color.BLACK);

        try{
            mainClip = a.playSound(0);
        } catch (LineUnavailableException e) {
            System.out.println("Line unavailable: " + e);
        }

        Timer timer = new Timer(10, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        timer.start();

        // Enable keyboard focus on the court area.
        // When this component has the keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        // Key listener
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

            public void keyReleased(KeyEvent e) {
                try{
                    a.playSound(2);
                    System.out.println(mainClip.getMicrosecondPosition());
                } catch (LineUnavailableException ex){
                    System.out.println("Line unavailable: " + ex);
                }
            }
        });

        n = new Note(mainClip, 800, 0, 100);
    }

    void tick() {
        gameTime = mainClip.getMicrosecondPosition();
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        n.draw(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1280, 720);
    }
}