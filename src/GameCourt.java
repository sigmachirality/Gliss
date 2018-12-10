import java.awt.*;
import javax.swing.*;
import java.io.*;
import javax.sound.sampled.*;
import java.awt.event.*;
import java.awt.image.*;

@SuppressWarnings("serial")
public class GameCourt extends JPanel {

    //TODO: make folder dynamic
    private String mapFolder = "maps/";
    private String assetsFolder = "files/";

    //Local state
    private Audio a;
    private Clip mainClip;
    private volatile long gameTime;

    private Level level;

    //Double buffering

    public GameCourt(Audio a) {
        this.a = a;
        this.level = null;
        try {
            a.addClip(mapFolder + "track.WAV");
            a.addClip(assetsFolder + "sound-start.wav");
            a.addClip(assetsFolder + "sound-end.wav");
        } catch (IOException e){
            System.out.println("IOException: " + e);
        } catch (UnsupportedAudioFileException e) {
            System.out.println("Unsupported Audio File: " + e);
        } catch (Exception e){
            System.out.println(e);
        }
    }

    public void play() {
        // creates border around the court area, and sets play area background to black
        setBackground(Color.BLACK);


        try{
            mainClip = a.playSound(0);
        } catch (Exception e){}

        File f = new File("maps/" + "rawnotes.txt");
        level = new Level(f, mainClip);

        Timer timer = new Timer(0, new ActionListener() {
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

    }

    void tick() {
        gameTime = mainClip.getMicrosecondPosition();
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics bg;
        Dimension dim = getSize();
        BufferedImage renderFrame = new BufferedImage(dim.width, dim.height, 2);
        bg = renderFrame.getGraphics();
        bg.clearRect(0, 0, dim.width, dim.height);
        bg.setColor(Color.BLACK);
        bg.setClip(0,0,dim.width, dim.height);
        super.paintComponent(bg);
        level.draw(bg, mainClip.getMicrosecondPosition());
        g.drawImage(renderFrame,0,0,this);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1280, 720);
    }
}