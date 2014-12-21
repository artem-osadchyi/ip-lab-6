package org.insane.ip.l6;

import java.awt.image.BufferedImage;

import ij.ImagePlus;
import ij.plugin.DICOM;

public class MyImage {

    public static MyImage load(String path) {
        DICOM image = new DICOM();

        image.open(path);

        return create(image);
    }

    public static MyImage create(ImagePlus image) {
        int height = image.getHeight();
        int width = image.getWidth();

        int[][] pixels = new int[width][height];

        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                pixels[x][y] = image.getPixel(x, y)[0];

        return new MyImage(pixels, width, height);
    }

    private final int[][] pixels;
    private final int width;
    private final int height;

    protected MyImage(int[][] pixels, int width, int height) {
        this.pixels = pixels;
        this.width = width;
        this.height = height;
    }

    public int getPixel(int x, int y) {
        int ix = x < 0 ? 0 : x >= width ? width - 1 : x;
        int iy = y < 0 ? 0 : y >= height ? height - 1 : y;

        return pixels[ix][iy];
    }

    public int[][] getRegion(int x0, int y0, int x1, int y1) {
        int width = x1 - x0 + 1;
        int height = y1 - y0 + 1;

        int[][] result = new int[width][height];

        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                result[x][y] = getPixel(x0 + x, y0 + y);

        return result;
    }

    public int[][] getNeighbourhood(int x, int y, int size) {
        int x0 = x - size;
        int y0 = y - size;
        int x1 = x + size;
        int y1 = y + size;

        return getRegion(x0, y0, x1, y1);
    }

    public MyImage filter(Filter filter) {
        int[][] result = new int[width][height];

        int size = filter.getSize();

        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                result[x][y] = filter.apply(getNeighbourhood(x, y, size / 2));

        return new MyImage(result, width, height);
    }

    public BufferedImage toBufferedImage() {
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_GRAY);

        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                result.setRGB(x, y, pixels[x][y]);

        return result;
    }

}
