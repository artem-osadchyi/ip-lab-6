package utils;

import ij.plugin.DICOM;
import managers.DataManager;
import pojo.Pixel;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ImageHelper
{
    public static DICOM readDicom(String path) {
        DICOM image = new DICOM();
        image.open(path);
        return image;
    }

    public static int getMax(List<Pixel> imageData){
        return Collections.max(imageData,new Comparator<Pixel>() {
            @Override
            public int compare(Pixel o1, Pixel o2) {
                return Integer.compare(o1.getGrayScale(),o2.getGrayScale());
            }
        }).getGrayScale();
    }

    public static int getMin(List<Pixel> imageData){
        return Collections.min(imageData,new Comparator<Pixel>() {
            @Override
            public int compare(Pixel o1, Pixel o2) {
                return Integer.compare(o1.getGrayScale(),o2.getGrayScale());
            }
        }).getGrayScale();
    }

    public static BufferedImage createImageFromPixelsList(List<Pixel> imageData, int width, int height) {
        BufferedImage result = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);

        for(int i=0;i<height;i++)
            for(int j=0;j<width;j++)
                result.setRGB(i,j, imageData.get(i*width+j).getARGB());

        return result;
    }

    public static List<Pixel> createPixelsListFromImage(DICOM image){
        List<Pixel> imageData = new ArrayList<>(image.getWidth()*image.getHeight());

        for(int i=0;i<image.getHeight();i++)
            for(int j=0;j<image.getWidth();j++)
                imageData.add(new Pixel(image.getPixel(i,j)[0]));

        int minGrayScale = getMin(imageData);

        List<Pixel> resultSet = new ArrayList<>(image.getWidth()*image.getHeight());
        for(Pixel pixel:imageData)
            resultSet.add(new Pixel(pixel.getGrayScale() - minGrayScale));

        return resultSet.isEmpty() ? null : resultSet;
    }

    public static BufferedImage getRobertsFiltrationImage(DICOM original){
        List<Pixel> pixels = createPixelsListFromImage(original);

        for(int i=0;i<pixels.size();i++){
            int[][] pixelSet = getPixelsSetWithMirror(pixels,i, original.getWidth());
            //For Roberts Filter newI= sqrt( P^2 + Q^2);
            //Rx = [0 1; -1 0] Ry = [1 0; 0 -1] => P(x,y) = I(x,y+1) - I(x+1,y) & Q(x,y) = I(x,y) - I(x+1,y+1)
            //newI = sqrt( (I(x,y+1) - I(x+1,y))^2 + (I(x,y) - I(x+1,y+1))^2 );
            int newGrayScale = (int) Math.sqrt(Math.pow((pixelSet[0][1] - pixelSet[1][0]),2) + Math.pow((pixelSet[0][0] - pixelSet[1][1]),2));

            pixels.get(i).setColor(newGrayScale);
        }

        double min = getMin(pixels);
        double max = getMax(pixels);

        pixels.forEach((pixel) -> pixel.setColor((int) ((pixel.getGrayScale() - min) / max * DataManager.NEW_MAX_GREYSCALE)) );

        return createImageFromPixelsList(pixels,original.getWidth(),original.getHeight());
    }

    public static int [][] getPixelsSetWithMirror(List<Pixel> pixels, int currentElementIndex, int imageWidth){
        int x= currentElementIndex / imageWidth;
        int y= currentElementIndex % imageWidth;

        int [][]  pixelSet = new int[2][2];

        if (x == imageWidth - 1 && y == imageWidth - 1) {
            pixelSet[0][0] = pixels.get(x * imageWidth + y).getGrayScale();
            pixelSet[0][1] = pixels.get(x * imageWidth + y - 1).getGrayScale();
            pixelSet[1][0] = pixels.get((x - 1) * imageWidth + y).getGrayScale();
            pixelSet[1][1] = pixels.get((x - 1) * imageWidth + y - 1).getGrayScale();
            return pixelSet;
        }

        if (x == imageWidth - 1) {
            pixelSet[0][0] = pixels.get(x * imageWidth + y).getGrayScale();
            pixelSet[0][1] = pixels.get(x * imageWidth + y + 1).getGrayScale();
            pixelSet[1][0] = pixels.get((x - 1) * imageWidth + y).getGrayScale();
            pixelSet[1][1] = pixels.get((x - 1) * imageWidth + y - 1).getGrayScale();
            return pixelSet;
        }

        if (y == imageWidth - 1) {
            pixelSet[0][0] = pixels.get(x * imageWidth + y).getGrayScale();
            pixelSet[0][1] = pixels.get(x * imageWidth + y - 1).getGrayScale();
            pixelSet[1][0] = pixels.get((x + 1) * imageWidth + y).getGrayScale();
            pixelSet[1][1] = pixels.get((x + 1) * imageWidth + y - 1).getGrayScale();
            return pixelSet;
        }

        pixelSet[0][0] = pixels.get(x * imageWidth + y).getGrayScale();
        pixelSet[0][1] = pixels.get(x * imageWidth + y + 1).getGrayScale();
        pixelSet[1][0] = pixels.get((x + 1) * imageWidth + y).getGrayScale();
        pixelSet[1][1] = pixels.get((x + 1) * imageWidth + y + 1).getGrayScale();
        return pixelSet;
    }

}