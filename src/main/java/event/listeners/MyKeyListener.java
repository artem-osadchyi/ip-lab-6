package event.listeners;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import managers.Globals;

public class MyKeyListener extends KeyAdapter {

    @Override
    public void keyPressed(KeyEvent e) {
        Globals globals = Globals.getInstance();

        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            int current = globals.getCurrentIndex();
            int total = globals.getTextures().length;
            current = ++current % total;
            globals.setCurrentIndex(current);

            globals.getFrame().setTitle(globals.getTitles()[current]);
            globals.getCanvas().display();
        }

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
            globals.getFrame().dispatchEvent(new WindowEvent(globals.getFrame(), WindowEvent.WINDOW_CLOSING));

    }

}
