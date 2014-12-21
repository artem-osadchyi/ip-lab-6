package event.listeners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.media.opengl.awt.GLCanvas;

import managers.DataManager;

public class MouseEventListener implements MouseMotionListener {

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        GLCanvas canvas = DataManager.getInstance().getCanvas();
        DataManager.getInstance().getMouse().setPosition(e.getX(), canvas.getWidth() - 1 - e.getY());
        canvas.display();
    }
}
