import java.io.*;
import java.util.*;
import javax.sound.sampled.*;

public class AudioManager {

    //Vectors to store audio data
    private Vector afs;
    private Vector sizes;
    private Vector infos;
    private Vector audios;
    private int num=0;

    public AudioManager()
    {
        afs=new Vector();
        sizes=new Vector();
        infos=new Vector();
        audios=new Vector();
    }

    public void addClip(String s) throws IOException, UnsupportedAudioFileException
    {
        File trackFile = new File(s);
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(trackFile);
        AudioFormat af = audioInputStream.getFormat();
        int size = (int) (af.getFrameSize() * audioInputStream.getFrameLength());
        byte[] audio = new byte[size];
        DataLine.Info info = new DataLine.Info(Clip.class, af, size);
        audioInputStream.read(audio, 0, size);

        afs.add(af);
        sizes.add(size);
        infos.add(info);
        audios.add(audio);

        num++;
    }

    public Clip playSound(int x) throws LineUnavailableException
    {
        if(x>num)
        {
            System.out.println("playSound: sample nr["+x+"] is not available");
            return null;
        }
        else
        {
            Clip clip = (Clip) AudioSystem.getLine((DataLine.Info)infos.elementAt(x));
            clip.open((AudioFormat)afs.elementAt(x), (byte[])audios.elementAt(x), 0, ((int) sizes.elementAt(x)));
            clip.start();
            return clip;
        }
    }
}

