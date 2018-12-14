import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class Audio {
    private Vector<AudioStruct> clips;

    public Audio() {
        clips = new Vector<AudioStruct>();
    }

    public int addClip(File f) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        AudioInputStream stream = AudioSystem.getAudioInputStream(f);
        AudioFormat af = stream.getFormat();
        int size = (int) (af.getFrameSize() * stream.getFrameLength());
        byte[] audio = new byte[size];
        DataLine.Info info = new DataLine.Info(Clip.class, af, size);
        stream.read(audio, 0, size);
        clips.add(new AudioStruct(af, size, audio, info));
        return clips.size() - 1;
    }

    public int addClip(String s) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        File f = new File(s);
        return addClip(f);
    }

    public void playSound(int x) throws LineUnavailableException {
        getClip(x).start();
    }

    public Clip getClip(int x) throws LineUnavailableException{
        if (x > (clips.size() - 1)) {
            System.out.println("playSound: sample nr[" + x + "] is not available");
            return null;
        } else {
            return clips.elementAt(x).getNewClip();
        }
    }
}

