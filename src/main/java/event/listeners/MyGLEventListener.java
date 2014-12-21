package event.listeners;

import ij.plugin.DICOM;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.nio.FloatBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;

import managers.Globals;
import pojo.Mouse;
import pojo.Pixel;
import utils.ImageHelper;

import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

public class MyGLEventListener implements GLEventListener {

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL2 gl2 = glAutoDrawable.getGL().getGL2();
        gl2.glEnable(GL.GL_TEXTURE_2D);

        GLProfile profile = glAutoDrawable.getGLProfile();

        Globals globals = Globals.getInstance();
        Texture[] textures = globals.getTextures();

        DICOM dicom = globals.getImage();
        BufferedImage original = dicom.getBufferedImage();
        BufferedImage filtered = ImageHelper.getRobertsFiltrationImage(dicom);

        textures[0] = AWTTextureIO.newTexture(profile, original, false);
        textures[1] = AWTTextureIO.newTexture(profile, filtered, false);

        for (Texture texture : textures)
            texture.bind(gl2);
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

        Globals globals = Globals.getInstance();
        int current = globals.getCurrentIndex();
        Texture currentTexture = globals.getTextures()[current];
        currentTexture.bind(gl2);
        BufferedImage image = globals.getImage().getBufferedImage();
        /*
         * bf(0;0) -> gl(0;1) bf(1;0) -> gl(1;1)
         * 
         * 
         * bf(0;1) -> gl(0;0) bf(1;1) -> gl(1;0)
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

        // --------------Pixel color in text renderer----------------
        gl2.glReadBuffer(GL.GL_FRONT);
        FloatBuffer pixelColorBuffer = FloatBuffer.allocate(4);
        Mouse mouse = globals.getMouse();
        gl2.glReadPixels(mouse.x, mouse.y, 1, 1, GL.GL_RGBA, GL.GL_FLOAT, pixelColorBuffer);

        Pixel hoveredPixel = new Pixel((int) (pixelColorBuffer.array()[0] * 255));

        TextRenderer renderer = new TextRenderer(new Font("Arial", Font.BOLD, 14));
        renderer.setColor(1.0f, 0f, 0f, 1.0f);

        renderer.beginRendering(globals.getCanvas().getWidth(), globals.getCanvas().getHeight());
        renderer.draw(mouse.toString() + hoveredPixel.toString(), 1, 1);
        renderer.endRendering();

        renderer.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {
    }

}
