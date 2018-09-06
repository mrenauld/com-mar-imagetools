package com.mar.imagetools.utils;

import com.mar.imagetools.objects.Image;
import com.mar.imagetools.objects.ImageProc;

/**
 * Operations on {@link ImageProc} objects. Contrarily to {@link Image}, {@link ImageProc} encodes the pixel values in
 * double values, so this format is more adequate for precise computations on pixel values.
 * @author mrenauld
 */
public class ImageProcOps {

    /**
     * Blends two images. The proportion of image 1 (0.0 to 1.0) is specified in the pProportionImage1 double array.
     * @param pImage1
     * @param pImage2
     * @param pProportionImage1
     * @return
     */
    public static ImageProc blend(ImageProc pImage1, ImageProc pImage2, double[][] pProportionImage1) {
        /* Build proportions for image 2. */
        int h = pImage1.getHeight();
        int w = pImage1.getWidth();
        double[][] proportionImage2 = new double[h][w];
        for (int i = 0; i < h; ++i) {
            for (int j = 0; j < w; ++j) {
                proportionImage2[i][j] = 1.0 - pProportionImage1[i][j];
            }
        }

        /* Blend. */
        ImageProc blended = new ImageProc(pImage1);
        blended.times(pProportionImage1);
        ImageProc image2Temp = new ImageProc(pImage2);
        image2Temp.times(proportionImage2);
        blended.addImage(image2Temp);

        return blended;
    }

    /**
     * Converts an image to black and white. The image is first converted to grayscale, then a threshold is applied: all
     * pixels above the threshold are set to white, and those below to black.
     * @param pImage1
     * @param pThreshold
     * @return
     */
    public static ImageProc toBlackAndWhite(ImageProc pImage1, double pThreshold) {
        ImageProc out = toGrayscale(pImage1);
        /* Process only one channel. The image is in grayscale so we know all color channels contain the same values. */
        double[][] channelData = out.getChannel(Image.CHANNEL_RED);
        for (int i = 0; i < channelData.length; ++i) {
            for (int j = 0; j < channelData[i].length; ++j) {
                if (channelData[i][j] > pThreshold) {
                    channelData[i][j] = 1.0;
                }
                else {
                    channelData[i][j] = 0.0;
                }
            }
        }
        /* Set the same value for all color channels. */
        for (int c = 0; c < Image.NB_COLOR_CHANNEL; ++c) {
            out.setChannel(c, channelData);
        }
        return out;
    }

    /**
     * Converts an image into a grayscale image.
     * @param pImage
     * @return
     */
    public static ImageProc toGrayscale(ImageProc pImage) {
        int h = pImage.getHeight();
        int w = pImage.getWidth();
        ImageProc out = new ImageProc(w, h);
        double[][] channelR = pImage.getChannel(Image.CHANNEL_RED);
        double[][] channelG = pImage.getChannel(Image.CHANNEL_GREEN);
        double[][] channelB = pImage.getChannel(Image.CHANNEL_BLUE);
        double[][] channelGray = new double[h][w];
        for (int i = 0; i < h; ++i) {
            for (int j = 0; j < w; ++j) {
                channelGray[i][j] = ImageUtils.rgbToGrayscale(channelR[i][j], channelG[i][j], channelB[i][j]);
            }
        }
        /* Set the same value for all color channels. */
        for (int c = 0; c < Image.NB_COLOR_CHANNEL; ++c) {
            out.setChannel(c, channelGray);
        }
        return out;
    }

}