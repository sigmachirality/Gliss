import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
import java.nio.Buffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.*;
import java.util.concurrent.ConcurrentHashMap;
import java.io.*;
import javax.sound.sampled.*;
import javax.swing.Timer;
import java.awt.*;
import java.util.Date.*;

public class Level implements Runnable{
    private ConcurrentHashMap<Long, Note> notes = new ConcurrentHashMap<Long, Note>();
    private Keyboard k;
    private Audio a;
    private Clip c;
    private int score = 0;
    private int combo = 0;
    private boolean mapEnded = false;
    private Object[] sortedScores = null;
    private Map<Long, String> scoreMap = null;
    private Deque<BufferedImage> d;
    private GameCourt g;

    //TODO: Construct beatmap from file path text file, parse into NOTES
    public Level(File f, Clip c, Audio a, Keyboard k, Deque<BufferedImage> d, GameCourt g){
        this.k = k;
        this.a = a;
        this.c = c;
        this.d = d;
        this.g = g;
        //Attempt to read from notes file
        try{
            FileReader fr = new FileReader(f);
            BufferedReader br=new BufferedReader(fr);
            String s = br.readLine();
            while(!( s == null || s.isEmpty())){
                String[] str = s.split("\\|");
                if (str.length >= 4){
                    Note n = null;
                    long timePos = Long.parseLong(str[1]);
                    int xPos = Integer.parseInt(str[2]);
                    int width = Integer.parseInt(str[3]);
                    if (str[0].equals("S") && str.length >= 6){
                        long endTime = Long.parseLong(str[4]);
                        int endXPos = Integer.parseInt(str[5]);
                        n = new Slider(c, timePos, xPos, width, endTime, endXPos);
                    } else {
                        n = new Note(c, timePos, xPos, width);
                    }
                    if (n != null) notes.put(timePos, n);
                }
                s = br.readLine();
            }
            br.close();
            fr.close();
        } catch (FileNotFoundException e){
            System.out.print("No file:" + e);
        } catch (IOException e) {
            System.out.print("IOException: " + e);
        }
    }

    private Stream<Note> getNotes(long currentTime){
        return notes.values().stream().filter(n -> (n.getTimePos() - currentTime) <= 1000000);
    }

    private void scoreNote(Note n, Graphics g){
        Area noteShape = new Area(n.draw(g, a));
        if (k.notePressed(noteShape)) {
            int noteScore = n.scoreNote();
            score += noteScore;
        }
    }

    private void drawScores(Graphics g){
        g.drawString("Highscores: ", 0, 100);
        Font myFont = new Font("Sans-Serif", Font.BOLD, 50);
        g.setFont(myFont);
        g.drawString(scoreMap.get(sortedScores[0]) + "---" + sortedScores[0], 100, 200);
        Map<Long, String> m = scoreMap;
        if (sortedScores.length > 1) g.drawString(m.get(sortedScores[1]) + "---" + sortedScores[1], 100,
                300);
        if (sortedScores.length > 2) g.drawString(m.get(sortedScores[2]) + "---" + sortedScores[2], 100,
                400);
        if (sortedScores.length > 3) g.drawString(m.get(sortedScores[3]) + "---" + sortedScores[3], 100,
                500);
        if (sortedScores.length > 4) g.drawString(m.get(sortedScores[4]) + "---" + sortedScores[4], 100,
                600);
    }

    public void run(){
        javax.swing.Timer timer = new Timer(0, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Dimension dim = g.getSize();
                BufferedImage temp = new BufferedImage(dim.width, dim.height, 2);
                Graphics bg = temp.getGraphics();
                bg.setClip(0,0,dim.width,dim.height);
                draw(bg, c.getMicrosecondPosition());
                d.add(temp);
            }
        });
        timer.start();
    }

    public void draw(Graphics g, long timePos){
        Font myFont = new Font("Sans-Serif", Font.BOLD, 100);
        g.setFont(myFont);
        g.setColor(Color.WHITE);
        if (mapEnded) {
            drawScores(g);
        }
        else {
            if (c.getMicrosecondPosition() == c.getMicrosecondLength()) {
                Map<Long, String> m = readWriteScores();
                scoreMap = m;
                Collection<Long> ks = m.keySet();
                Object[] sortedScores = ks.toArray().clone();
                this.sortedScores = sortedScores;
                Arrays.sort(sortedScores, Collections.reverseOrder());
                mapEnded = true;
                g.drawString("Highscores: ", 0, 100);
                myFont = new Font("Sans-Serif", Font.BOLD, 50);
                g.setFont(myFont);
                drawScores(g);
            } else {
                getNotes(timePos).forEach(n -> scoreNote(n, g));
                g.setColor(Color.WHITE);
                String formatted = String.format("%07d", score);
                g.drawString(formatted, 10, 100);
            }
        }
    }

    public Map<Long, String> readWriteScores() {
        try{
            File scoreFile = new File("maps/scores.txt");
            scoreFile.createNewFile();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
            Map<Long, String> scores = new ConcurrentHashMap<Long, String>();
            scores.put(new Long(score), dateFormat.format(new Date()));

            FileReader fr = new FileReader(scoreFile);
            BufferedReader br=new BufferedReader(fr);
            String s = br.readLine();

            while(!( s == null || s.isEmpty())){
                String[] str = s.split("\\|");
                String d = str[0];
                Long score = Long.parseLong(str[1]);
                scores.put(score, d);
                s = br.readLine();
                System.out.println(scores.values().size());
            }
            br.close();
            fr.close();

            FileWriter fw = new FileWriter("maps/scores.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write(dateFormat.format(new Date())+ "|" + score);
            bw.newLine();

            bw.close();
            return scores;
        } catch (FileNotFoundException e){
            System.out.print("No file:" + e);
        } catch (IOException e) {
            System.out.print("IOException: " + e);
        }
        return null;
    }

}
