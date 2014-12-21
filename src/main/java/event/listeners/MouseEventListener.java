package event.listeners;

import managers.DataManager;

import javax.media.opengl.awt.GLCanvas;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

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
