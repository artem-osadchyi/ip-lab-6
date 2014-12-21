package managers;

import com.jogamp.opengl.util.texture.Texture;
import ij.plugin.DICOM;
import pojo.Mouse;

import javax.media.opengl.awt.GLCanvas;
import javax.swing.*;

public class DataManager {

    private DataManager(){}
    private static DataManager instance = null;


    public static final String PATH = "lab6.dcm";
    public static int NEW_MAX_GREYSCALE = 255;

    private GLCanvas canvas = new GLCanvas();
    private final JFrame frame = new JFrame();

    private DICOM image=null;

    private int currentTexturesAndTitle =0;
    private Texture[] textures = new Texture[2];
    private String[] titles = {"Original","Roberts Filter"};

    private Mouse mouse = new Mouse();

    public Mouse getMouse() {
        return mouse;
    }

    public JFrame getFrame() {
        return frame;
    }

    public int getCurrentTexturesAndTitle() {
        return currentTexturesAndTitle;
    }

    public void setCurrentTexturesAndTitle(int currentTexturesAndTitle) {
        this.currentTexturesAndTitle = currentTexturesAndTitle;
    }

    public Texture[] getTextures() {
        return textures;
    }

    public String[] getTitles() {
        return titles;
    }

    public GLCanvas getCanvas() {
        return canvas;
    }

    public DICOM getImage() {
        return image;
    }

    public void setImage(DICOM image) {
        this.image = image;
    }

    public static DataManager getInstance(){
        if(instance == null)
            instance = new DataManager();
        return instance;
    }
}
