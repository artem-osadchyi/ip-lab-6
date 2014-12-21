import ij.plugin.DICOM;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import managers.Globals;
import utils.ImageHelper;
import event.listeners.MyKeyListener;
import event.listeners.MyMouseMotionListener;
import event.listeners.MyGLEventListener;

public class Main {

    public static void main(String[] args) {
        Globals globals = Globals.getInstance();
        DICOM dicom = ImageHelper.readDicom(Globals.PATH);
        globals.setImage(dicom);
        int width = dicom.getWidth();
        int height = dicom.getHeight();

        launch(width, height);
    }

    public static void launch(int width, int height) {
        Globals globals = Globals.getInstance();
        GLCanvas canvas = globals.getCanvas();

        canvas.setPreferredSize(new Dimension(width, height));
        canvas.addGLEventListener(new MyGLEventListener());
        canvas.addKeyListener(new MyKeyListener());
        canvas.addMouseMotionListener(new MyMouseMotionListener());

        final JFrame frame = globals.getFrame();

        frame.getContentPane().add(canvas, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);

        SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }

}
