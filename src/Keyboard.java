import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
import java.awt.geom.Area;

class Keyboard extends KeyAdapter implements Runnable {

    private Area activeArea = new Area();
    private Collection<BufferedImage> b;
    private GameCourt g;

    private final Set<Integer> pressed = new HashSet<Integer>();

    public Keyboard(Collection<BufferedImage> b, GameCourt g){
        activeArea = new Area();
        this.b = b;
        this.g = g;
    }

    @Override
    public synchronized void keyPressed(KeyEvent e) {
        pressed.add(e.getExtendedKeyCode());
    }

    @Override
    public synchronized void keyReleased(KeyEvent e) {
        pressed.remove(e.getKeyCode());
        /*try{
            a.playSound(2);
        } catch (LineUnavailableException ex){
            System.out.println("Line unavailable: " + ex);
        }*/
    }

    public void draw(Graphics g) {
        Color c=new Color(1f,1f,0f,.3f );
        g.setColor(c);
        int indicatorHeight = (int) (0.05 * g.getClipBounds().height);
        int indicatorPos = (int) (0.95 * g.getClipBounds().height);
        int guardPos = (int) (0.85 * g.getClipBounds().height);
        int guardHeight = indicatorHeight * 3;
        Area tempArea = new Area();
        if (pressed.contains(KeyEvent.VK_CAPS_LOCK)){
            tempArea.add(new Area(new Rectangle(0,indicatorPos,124,guardHeight)));
            g.fillRect(0,indicatorPos,124,indicatorHeight);
        }
        if (pressed.contains(KeyEvent.VK_A)){
            tempArea.add(new Area(new Rectangle(123,indicatorPos,83,guardHeight)));
            g.fillRect(123,indicatorPos,83,indicatorHeight);
        }
        if (pressed.contains(KeyEvent.VK_S)){
            tempArea.add(new Area(new Rectangle(206,indicatorPos,83,guardHeight)));
            g.fillRect(206,indicatorPos,83,indicatorHeight);
        }
        if (pressed.contains(KeyEvent.VK_D)){
            tempArea.add(new Area(new Rectangle(289,indicatorPos,83,guardHeight)));
            g.fillRect(289,indicatorPos,83,indicatorHeight);
        }
        if (pressed.contains(KeyEvent.VK_F)){
            tempArea.add(new Area(new Rectangle(372,indicatorPos,83,guardHeight)));
            g.fillRect(372,indicatorPos,83,indicatorHeight);
        }
        if (pressed.contains(KeyEvent.VK_G)){
            tempArea.add(new Area(new Rectangle(455,indicatorPos,83,guardHeight)));
            g.fillRect(455,indicatorPos,83,indicatorHeight);
        }
        if (pressed.contains(KeyEvent.VK_H)){
            tempArea.add(new Area(new Rectangle(538,indicatorPos,83,guardHeight)));
            g.fillRect(538,indicatorPos,83,indicatorHeight);
        }
        if (pressed.contains(KeyEvent.VK_J)){
            tempArea.add(new Area(new Rectangle(621,indicatorPos,83,guardHeight)));
            g.fillRect(621,indicatorPos,83,indicatorHeight);
        }
        if (pressed.contains(KeyEvent.VK_K)){
            tempArea.add(new Area(new Rectangle(704,indicatorPos,83,guardHeight)));
            g.fillRect(704,indicatorPos,83,indicatorHeight);
        }
        if (pressed.contains(KeyEvent.VK_L)){
            tempArea.add(new Area(new Rectangle(787,indicatorPos,83,guardHeight)));
            g.fillRect(787,indicatorPos,83,indicatorHeight);
        }
        if (pressed.contains(KeyEvent.VK_COLON)){
            tempArea.add(new Area(new Rectangle(870,indicatorPos,83,guardHeight)));
            g.fillRect(870,indicatorPos,83,indicatorHeight);
        }
        if (pressed.contains(KeyEvent.VK_QUOTE)){
            tempArea.add(new Area(new Rectangle(953,indicatorPos,83,guardHeight)));
            g.fillRect(953,indicatorPos,83,indicatorHeight);
        }
        if (pressed.contains(KeyEvent.VK_ENTER)){
            tempArea.add(new Area(new Rectangle(1036,indicatorPos,244,guardHeight)));
            g.fillRect(1036,indicatorPos,244,indicatorHeight);
        }
        g.drawRect(0, indicatorPos,1280,indicatorHeight);
        g.setColor(Color.magenta);
        g.drawRect(0, guardPos, 1280,guardHeight);

        activeArea = tempArea;
    }

    public boolean notePressed(Area n){
        Area temp = (Area) n.clone();
        n.subtract(activeArea);
        return !(n.equals(temp));
    }

    @Override
    public void run(){
        Timer timer = new Timer(0, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Dimension d = g.getSize();
                BufferedImage temp = new BufferedImage(d.width, d.height, 2);
                Graphics bg = temp.getGraphics();
                bg.setClip(0,0,d.width,d.height);
                draw(bg);
                b.add(temp);
            }
        });
        timer.start();
    }

}