package com.mar.imagetools.utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import com.mar.algotools.mathematics.utils.MathOps;

/**
 * Image utils. The RGBA color integer representation works as follows: each
 * integer value contains, from most significant to less significant<br />
 * <ul>
 * <li>alpha (255 if no alpha channel in the Color).</li>
 * <li>red</li>
 * <li>green</li>
 * <li>blue</li>
 * </ul>
 *
 * @author mrenauld
 */
public class ImageUtils {

    /**
     * Returns an RGBA integer array representing the specified BufferedImage.
     *
     * @param pImage
     * @return
     */
    public static int[][] bufferedImageToSRGBIntArray(BufferedImage pImage) {

        byte[] pixels = ((DataBufferByte) pImage.getRaster().getDataBuffer()).getData();
        int width = pImage.getWidth();
        int height = pImage.getHeight();
        boolean hasAlphaChannel = pImage.getAlphaRaster() != null;

        int[][] result = new int[height][width];
        if (hasAlphaChannel) {
            int pixelLength = 4;
            for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += ((pixels[pixel] & 0xff) << 24); // alpha
                argb += (pixels[pixel + 1] & 0xff); // blue
                argb += ((pixels[pixel + 2] & 0xff) << 8); // green
                argb += ((pixels[pixel + 3] & 0xff) << 16); // red
                result[row][col] = argb;
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        } else {
            final int pixelLength = 3;
            for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += -16777216; // alpha = 255
                argb += (pixels[pixel] & 0xff); // blue
                argb += ((pixels[pixel + 1] & 0xff) << 8); // green
                argb += ((pixels[pixel + 2] & 0xff) << 16); // red
                result[row][col] = argb;
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        }

        return result;
    }

    /**
     * Converts an integer array representing an image channel (0 to 255 values)
     * into a float array (0.0f to 1.0f values).
     *
     * @param pIntArray
     * @return
     */
    public static float[][] channelIntArrayToFloatArray(int[][] pIntArray) {
        int x = pIntArray.length;
        int y = pIntArray[0].length;
        float[][] floatArray = new float[x][y];
        for (int i = 0; i < x; ++i) {
            for (int j = 0; j < y; ++j) {
                floatArray[i][j] = channelIntToFloat(pIntArray[i][j]);
            }
        }
        return floatArray;
    }

    /**
     * Converts an integer channel value (between 0 and 255) into a float value
     * between 0.0f and 1.0f.
     *
     * @param pChannelValue
     * @return
     */
    public static float channelIntToFloat(int pChannelValue) {
        return (pChannelValue) / 255.0f;
    }

    /**
     * Returns the RGBA integer corresponding to the specified Color.
     *
     * @param pColor
     * @return
     */
    public static int colorToSRGBInt(Color pColor) {
        int colorInt = 0;
        colorInt += pColor.getAlpha() << 24;
        colorInt += pColor.getRed() << 16;
        colorInt += pColor.getGreen() << 8;
        colorInt += pColor.getBlue();
        return colorInt;
    }

    /**
     * Converts a float array (0.0f to 1.0f values) into an integer array
     * representing an image channel (0 to 255 values).
     *
     * @param pIntArray
     * @return
     */
    public static int[][] floatArrayToChannelIntArray(float[][] pFloatArray) {
        int x = pFloatArray.length;
        int y = pFloatArray[0].length;
        int[][] intArray = new int[x][y];
        for (int i = 0; i < x; ++i) {
            for (int j = 0; j < y; ++j) {
                intArray[i][j] = floatToChannelInt(pFloatArray[i][j]);
            }
        }
        return intArray;
    }

    /**
     * Converts a float value between 0.0f and 1.0f into an integer channel
     * value (between 0 and 255). If the value is outside the 0-255 range, it is
     * clamped to this range.
     *
     * @param pFloat
     * @return
     */
    public static int floatToChannelInt(float pFloat) {
        return MathOps.clamp((int) (pFloat * 255.0f), 0, 255);
    }

    /**
     * Converts RGB values (between 0.0 and 1.0) to a grayscale value (between
     * 0.0 and 1.0) according to the PAL/NTSC conversion.
     *
     * @param pR
     * @param pG
     * @param pB
     * @return
     */
    public static double rgbToGrayscale(double pR, double pG, double pB) {
        return 0.299 * pR + 0.587 * pG + 0.114 * pB;
    }

    /**
     * Converts RGB values (between 0.0f and 1.0f) to a grayscale value (between
     * 0.0f and 1.0f) according to the PAL/NTSC conversion.
     *
     * @param pR
     * @param pG
     * @param pB
     * @return
     */
    public static float rgbToGrayscale(float pR, float pG, float pB) {
        return 0.299f * pR + 0.587f * pG + 0.114f * pB;
    }

    /**
     * Returns a BufferedImage built from the specified integer array.
     *
     * @param pArray
     * @return
     */
    public static BufferedImage sRGBIntArrayToBufferedImage(int[][] pArray) {
        int h = pArray.length;
        int w = pArray[0].length;

        // TODO manage hasAlphaChannel.

        /* Format the data. */
        // byte[] formattedData = new byte[4 * h * w];
        // int cpt = 0;
        // for (int i = 0; i < h; ++i) {
        // for (int j = 0; j < w; ++j) {
        // formattedData[cpt] = (byte) ((pArray[i][j] >> 24) & 0xff);
        // formattedData[cpt + 1] = (byte) (pArray[i][j] & 0xff);
        // formattedData[cpt + 2] = (byte) ((pArray[i][j] >> 8) & 0xff);
        // formattedData[cpt + 3] = (byte) ((pArray[i][j] >> 16) & 0xff);
        // cpt += 4;
        // }
        // }

        byte[] formattedData = new byte[3 * h * w];
        int cpt = 0;
        for (int i = 0; i < h; ++i) {
            for (int j = 0; j < w; ++j) {
                formattedData[cpt] = (byte) (pArray[i][j] & 0xff);
                formattedData[cpt + 1] = (byte) ((pArray[i][j] >> 8) & 0xff);
                formattedData[cpt + 2] = (byte) ((pArray[i][j] >> 16) & 0xff);
                cpt += 3;
            }
        }

        BufferedImage bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
        byte[] imageArray = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();
        System.arraycopy(formattedData, 0, imageArray, 0, formattedData.length);

        return bufferedImage;
    }

    /**
     * Returns the Color corresponding to the specified RGBA integer.
     *
     * @param pColorInt
     * @return
     */
    public static Color sRGBIntToColor(int pColorInt) {
        int alpha = (pColorInt >> 24) & 0xff;
        int red = (pColorInt >> 16) & 0xff;
        int green = (pColorInt >> 8) & 0xff;
        int blue = pColorInt & 0xff;
        Color color = new Color(red, green, blue, alpha);
        return color;
    }

}