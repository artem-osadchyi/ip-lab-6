package event.listeners;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.media.opengl.awt.GLCanvas;

import pojo.Mouse;
import managers.Globals;

public class MyMouseMotionListener extends MouseAdapter {

    @Override
    public void mouseMoved(MouseEvent e) {
        Globals globals = Globals.getInstance();
        GLCanvas canvas = globals.getCanvas();
        Mouse mouse = globals.getMouse();

        mouse.setPosition(e.getX(), canvas.getWidth() - 1 - e.getY());

        canvas.display();
    }

}
