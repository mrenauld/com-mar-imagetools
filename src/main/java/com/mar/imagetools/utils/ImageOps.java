package com.mar.imagetools.utils;

import java.awt.Color;

import com.mar.imagetools.objects.Image;
import com.mar.imagetools.objects.ImageProc;

/**
 * Operations on {@link Image} objects. Contrarily to {@link ImageProc}, {@link Image} manages the alpha channel. On the
 * other hand, data in {@link Image} is encoded as Integer values in the 0-255 range, so this format is less adequate
 * for precise computations on pixel values.
 * @author mrenauld
 */
public class ImageOps {

    /**
     * Inverts the colors of the specified image.
     * @param pImage
     * @return
     */
    public static Image invertColors(Image pImage) {
        Image out = new Image(pImage);

        for (int c = 0; c < Image.NB_COLOR_CHANNEL; ++c) {
            int[][] channelData = out.getChannel(c);
            for (int i = 0; i < channelData.length; ++i) {
                for (int j = 0; j < channelData[i].length; ++j) {
                    channelData[i][j] = 255 - channelData[i][j];
                }
            }
            out.setChannel(c, channelData);
        }

        return out;
    }

    /**
     * Sets the specified color (within a certain threshold) transparent. Each pixel is set to transparent individually
     * if its distance to the specified color is lower than the specified threshold:<br/>
     * Distance = sqrt((color_red - pixel_red)^2 + (color_green - pixel_green)^2 + (color_blue - pixel_blue)^2).<br/>
     * Pixel is set to transparent if distance < threshold. The color values are in the scale 0-255, so the distance is
     * between 0 and 255.
     * @param pImage
     * @param pColor
     * @param pThreshold
     */
    public static void setColorTransparent(Image pImage, Color pColor, float pThreshold) {
        float[][] channelRed = pImage.getChannelFloat(Image.CHANNEL_RED);
        float[][] channelGreen = pImage.getChannelFloat(Image.CHANNEL_GREEN);
        float[][] channelBlue = pImage.getChannelFloat(Image.CHANNEL_BLUE);
        float[][] channelAlpha = pImage.getChannelFloat(Image.CHANNEL_ALPHA);

        float cR = ImageUtils.channelIntToFloat(pColor.getRed());
        float cG = ImageUtils.channelIntToFloat(pColor.getGreen());
        float cB = ImageUtils.channelIntToFloat(pColor.getBlue());

        int h = channelRed.length;
        int w = channelRed[0].length;
        for (int i = 0; i < h; ++i) {
            for (int j = 0; j < w; ++j) {
                /* Compute the distance with the specified color. */
                double dist = Math.pow(cR - channelRed[i][j], 2.0);
                dist += Math.pow(cG - channelGreen[i][j], 2.0);
                dist += Math.pow(cB - channelBlue[i][j], 2.0);
                dist = Math.sqrt(dist);

                /* If the distance is lower or equal to the threshold, set the pixel transparent. */
                if (dist <= pThreshold) {
                    channelAlpha[i][j] = 0.0f;
                }
            }
        }

        pImage.setChannel(Image.CHANNEL_ALPHA, channelAlpha);
    }

    /**
     * Converts an image into a grayscale image.
     * @param pImage
     * @return
     */
    public static Image toGrayscale(Image pImage) {
        int h = pImage.getHeight();
        int w = pImage.getWidth();
        Image grayscaleImage = new Image(w, h);
        float[][] channelR = pImage.getChannelFloat(Image.CHANNEL_RED);
        float[][] channelG = pImage.getChannelFloat(Image.CHANNEL_GREEN);
        float[][] channelB = pImage.getChannelFloat(Image.CHANNEL_BLUE);
        float[][] channelGray = new float[h][w];
        for (int i = 0; i < h; ++i) {
            for (int j = 0; j < w; ++j) {
                channelGray[i][j] = ImageUtils.rgbToGrayscale(channelR[i][j], channelG[i][j], channelB[i][j]);
            }
        }
        for (int c = 0; c < Image.NB_COLOR_CHANNEL; ++c) {
            grayscaleImage.setChannel(c, channelGray);
        }
        return grayscaleImage;
    }
}
