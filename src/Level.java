import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.io.*;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class Level {
    ConcurrentHashMap<Long, Note> notes =
            new ConcurrentHashMap<Long, Note>();
    Keyboard k;
    Audio a;
    int score = 0;

    //TODO: Construct beatmap from file path text file, parse into NOTES
    public Level(File f, Clip c, Audio a, Keyboard k){
        this.k = k;
        this.a = a;
        try{
            FileReader fr = new FileReader(f);
            BufferedReader br=new BufferedReader(fr);
            String s = br.readLine();
            while(!( s == null || s.isEmpty())){
                String[] str = s.split(",");
                long timePos = Long.parseLong(str[1]);
                int xPos = Integer.parseInt(str[2]);
                int width = Integer.parseInt(str[3]);
                Note n = null;
                if (str[0].equals("S")){
                    long endTime = Long.parseLong(str[4]);
                    int endXPos = Integer.parseInt(str[5]);
                    n = new Slider(c, timePos, xPos, width, endTime, endXPos);
                } else {
                    n = new Note(c, timePos, xPos, width);
                }
                if (n != null) notes.put(timePos, n);
                s = br.readLine();
            }
            br.close();
            fr.close();
        } catch (FileNotFoundException e){
            System.out.print("No file:" + e);
        } catch (IOException e){
            System.out.print("IOException: " + e);
        }
    }

    public Collection<Note> getNotes(long currentTime){
        long startTime = currentTime - 600000;
        long endTime = currentTime + 600000;
        if (startTime < 0) startTime = 0;
        LinkedList<Note> tempNotes = new LinkedList<Note>();
        for (long l = startTime; l <= endTime; l++){
            if (notes.contains(l)) {
                tempNotes.add(notes.get(l));
                System.out.print("NICE");
            }
        }
        return tempNotes;
    }

    public void draw(Graphics g, long timePos){

        for (Note n: notes.values()) {
            Area noteShape = new Area(n.draw(g, a));
            if (k.notePressed(noteShape)){
                score += 10;
            }
        }

        Font myFont = new Font("Sans-Serif", Font.BOLD, 100);
        g.setFont(myFont);
        g.setColor(Color.WHITE);
        String formatted = String.format("%07d", score);
        g.drawString(formatted,10,100);
    }

}
