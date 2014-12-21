package pojo;

import java.awt.Color;

public class Pixel {
    public int color;

    public Pixel(int grayScale) {
        color = grayScale;
    }

    @Override
    public String toString() {
        return " GrayScale (" + color + ") ";
    }

    public void setColor(int grayScale) {
        color = grayScale;
    }

    public int getGrayScale() {
        return color;
    }

    public int getARGB() {
        return new Color(color, color, color).getRGB();
    }
}
