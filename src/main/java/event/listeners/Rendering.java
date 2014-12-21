package event.listeners;

import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;
import managers.DataManager;
import pojo.Mouse;
import pojo.Pixel;
import utils.ImageHelper;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.FloatBuffer;

public class Rendering implements GLEventListener {

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL2 gl2 = glAutoDrawable.getGL().getGL2();
        gl2.glEnable(GL.GL_TEXTURE_2D);

        //initialize all textures
        Texture[] textures = DataManager.getInstance().getTextures();

        //initialize original image:
        textures[0] = AWTTextureIO.newTexture(glAutoDrawable.getGLProfile(), DataManager.getInstance().getImage().getBufferedImage(), false);

        //initialize filtered image using Robert's filter
        textures[1] = AWTTextureIO.newTexture(glAutoDrawable.getGLProfile(),
                        ImageHelper.getRobertsFiltrationImage(DataManager.getInstance().getImage()),
                        false);

        for(int i=0;i<textures.length;i++)
            textures[i].bind(gl2);
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {
        GL2 gl2 = glAutoDrawable.getGL().getGL2();
        gl2.glOrtho(0, width, 0, height, -1, 1);
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GL2 gl2 = glAutoDrawable.getGL().getGL2();
        gl2.glClear(GL.GL_COLOR_BUFFER_BIT);

        int currentTextureIndex = DataManager.getInstance().getCurrentTexturesAndTitle();
        Texture currentTexture = DataManager.getInstance().getTextures()[currentTextureIndex];
        currentTexture.bind(gl2);
        BufferedImage image = DataManager.getInstance().getImage().getBufferedImage();
        /*
        bf(0;0) -> gl(0;1)              bf(1;0) -> gl(1;1)


        bf(0;1) -> gl(0;0)              bf(1;1) -> gl(1;0)
         */
        gl2.glBegin(GL2.GL_POLYGON);
        gl2.glTexCoord2i(0, 1);
        gl2.glVertex2i(0, 0);
        gl2.glTexCoord2i(0, 0);
        gl2.glVertex2i(0, image.getHeight());
        gl2.glTexCoord2i(1, 0);
        gl2.glVertex2i(image.getWidth(), image.getHeight());
        gl2.glTexCoord2i(1, 1);
        gl2.glVertex2i(image.getWidth(), 0);
        gl2.glEnd();

        //--------------Pixel color in text renderer----------------
        gl2.glReadBuffer(GL.GL_FRONT);
        FloatBuffer pixelColorBuffer = FloatBuffer.allocate(4);
        Mouse mouse = DataManager.getInstance().getMouse();
        gl2.glReadPixels(mouse.x, mouse.y, 1, 1, GL.GL_RGBA, GL.GL_FLOAT, pixelColorBuffer);

        Pixel hoveredPixel = new Pixel((int) (pixelColorBuffer.array()[0]*255));

        TextRenderer renderer = new TextRenderer(new Font("Arial", Font.BOLD, 14));
        renderer.setColor(1.0f, 0f, 0f, 1.0f);

        renderer.beginRendering(DataManager.getInstance().getCanvas().getWidth(), DataManager.getInstance().getCanvas().getHeight());
        renderer.draw(mouse.toString() + hoveredPixel.toString(), 1, 1);
        renderer.endRendering();

        renderer.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {
    }
}
