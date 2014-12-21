package event.listeners;

import managers.DataManager;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;

public class KeyEventListener implements KeyListener {


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            int currentIndex = DataManager.getInstance().getCurrentTexturesAndTitle();
            int elementsLength = DataManager.getInstance().getTextures().length;
            currentIndex = ++currentIndex % elementsLength;
            DataManager.getInstance().setCurrentTexturesAndTitle(currentIndex);

            DataManager.getInstance().getFrame().setTitle(DataManager.getInstance().getTitles()[currentIndex]);
            DataManager.getInstance().getCanvas().display();
        }

        if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
            DataManager.getInstance().getFrame().dispatchEvent(new WindowEvent(DataManager.getInstance().getFrame(),WindowEvent.WINDOW_CLOSING));
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
