import java.awt.geom.Area;
import java.util.stream.*;
import java.util.concurrent.ConcurrentHashMap;
import java.io.*;
import javax.sound.sampled.*;
import java.awt.*;

public class Level {
    private ConcurrentHashMap<Long, Note> notes = new ConcurrentHashMap<Long, Note>();
    private Keyboard k;
    private Audio a;
    private int score = 0;
    private int combo = 0;
    private boolean lastPress = true;

    //TODO: Construct beatmap from file path text file, parse into NOTES
    public Level(File f, Clip c, Audio a, Keyboard k){
        this.k = k;
        this.a = a;
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

    public void draw(Graphics g, long timePos){
        Font myFont = new Font("Sans-Serif", Font.BOLD, 100);
        g.setFont(myFont);
        getNotes(timePos).forEach(n -> scoreNote(n, g));
        g.setColor(Color.WHITE);
        String formatted = String.format("%07d", score);
        g.drawString(formatted,10,100);
    }

}
