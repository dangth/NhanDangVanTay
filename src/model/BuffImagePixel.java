/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

/**
 *
 * @author PhanHai
 * @since 05/05/2012 @category Convert data between BufferedImage and Pixel
 * array
 */
public class BuffImagePixel {

    private BufferedImage buffImage;

    public BuffImagePixel(BufferedImage buffImage) {
        this.buffImage = buffImage;
    }

    /**
     * Convert BufferImage to Pixel Array
     *
     * @return Pixel array
     */
    public Pixel[][] getData() {
        Raster raster = buffImage.getRaster();
        int width = raster.getWidth();
        int height = raster.getHeight();
        Pixel[][] data = new Pixel[height][width];
        float[] rgb = new float[3];
        // Read data
        for (int row = 0; row < height; ++row) {
            for (int col = 0; col < width; ++col) {
                rgb = raster.getPixel(col, row, rgb);
                data[row][col] = new Pixel(rgb[0], rgb[1], rgb[2]);
            }
        }
        return data;
    }

    /**
     * Convert Pixel array to Buffered Image
     *
     * @param data
     */
    public void setData(Pixel[][] data) {
        float[] rgb = new float[3];     // a temporary array to hold r,g,b values
        WritableRaster wr = buffImage.getRaster();
        int width = wr.getWidth();
        int height = wr.getHeight();
        //Process exception
        if (data.length != wr.getHeight()) {
            throw new IllegalArgumentException("Array size does not match");
        } else if (data[0].length != wr.getWidth()) {
            throw new IllegalArgumentException("Array size does not match");
        }
        // Write data
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                rgb[0] = data[row][col].getRed();
                rgb[1] = data[row][col].getGreen();
                rgb[2] = data[row][col].getBlue();
                wr.setPixel(col, row, rgb);
            }
        }
    }

    /**
     * Set Buffer Image
     *
     * @param buffImage
     */
    public void setBuffImage(BufferedImage buffImage) {
        this.buffImage = buffImage;
    }

    /**
     * Get Buffered Image
     *
     * @return Buffered Image
     */
    public BufferedImage getBuffImage() {
        return buffImage;
    }
}
