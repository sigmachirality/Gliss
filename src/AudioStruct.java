import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;

public class AudioStruct {

    public AudioFormat format;
    public int size;
    public Info info;
    public byte[] audio;

    public AudioStruct(AudioFormat format, int size, byte[] audio, Info info){
        this.format = format;
        this.size = size;
        this.info = info;
        this.audio = audio;
    }

    public Clip getNewClip() throws LineUnavailableException {
        Clip clip = (Clip) AudioSystem.getLine(info);
        clip.open(format, audio, 0, size);
        return clip;
    }
}
