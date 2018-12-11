import java.awt.*;
import javax.swing.*;
import java.io.*;
import javax.sound.sampled.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.Collection;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

@SuppressWarnings("serial")
public class GameCourt extends JPanel {

    //TODO: make folder dynamic
    private String mapFolder = "files/maps/";
    private String assetsFolder = "files/";

    //Local state
    private Audio a;
    private Clip mainClip;
    private Level level;
    private Keyboard keyboard;
    private Deque<BufferedImage> d = new ConcurrentLinkedDeque<BufferedImage>();

    private volatile BufferedImage image;

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
        keyboard = new Keyboard(d, this);
    }

    public void play() {
        // creates border around the court area, and sets play area background to black
        setBackground(Color.BLACK);
        try{
            mainClip = a.playSound(0);
        } catch (Exception e){}

        File f = new File(mapFolder + "notes.txt");
        level = new Level(f, mainClip, a, keyboard, d, this);
        level.run();

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
        addKeyListener(keyboard);
        keyboard.run();

    }

    void tick() {
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        //Double buffering
        Graphics bg;
        Dimension dim = getSize();
        image = new BufferedImage(dim.width, dim.height, 2);

        //Setup image
        bg = image.getGraphics();
        bg.clearRect(0, 0, dim.width, dim.height);
        bg.setColor(Color.BLACK);
        bg.setClip(0,0,dim.width, dim.height);

        //Draw components
        super.paintComponent(bg);
        //level.draw(bg, mainClip.getMicrosecondPosition());
        //keyboard.draw(bg);
        d.stream().forEach(image -> bg.drawImage(image,0,0,this));
        d.clear();

        //Render image
        g.drawImage(image,0,0,this);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1280, 720);
    }
}