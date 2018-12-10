import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.io.*;
import javax.sound.sampled.*;
import java.awt.*;
import java.util.LinkedList;

public class Level {
    ConcurrentHashMap<Long, Note> notes =
            new ConcurrentHashMap<Long, Note>();

    //TODO: Construct beatmap from file path text file, parse into NOTES
    public Level(File f, Clip c){
        try{
            FileReader fr = new FileReader(f);
            BufferedReader br=new BufferedReader(fr);
            String s = br.readLine();
            while(!( s == null || s.isEmpty())){
                String[] str = s.split(",");
                long timePos = Long.parseLong(str[0])*1000;
                int xPos = Integer.parseInt(str[1]);
                Note n = new Note(c, timePos, xPos, 100);
                notes.put(timePos, n);
                s = br.readLine();
            }
            br.close();
            fr.close();
        } catch (FileNotFoundException e){
            System.out.print("FUCK");
        } catch (IOException e){
            System.out.print("NYET");
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
            n.draw(g);
        }
    }
}
