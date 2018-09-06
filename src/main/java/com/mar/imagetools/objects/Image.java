package com.mar.imagetools.objects;

import java.awt.image.BufferedImage;

import com.mar.algotools.mathematics.utils.ArrayConvert;
import com.mar.algotools.mathematics.utils.ArrayUtils;
import com.mar.imagetools.utils.ImageUtils;

/**
 * An image object. The image is represented by an integer 2D array. The RGBA values are stored in the RGBA integer
 * values with, in order from most significant to less significant:<br />
 * <ul>
 * <li>alpha (255 if no alpha channel in the Color).</li>
 * <li>red</li>
 * <li>green</li>
 * <li>blue</li>
 * </ul>
 * @author mrenauld
 */
public class Image {

    public static final int CHANNEL_ALPHA = 3;

    public static final int CHANNEL_RED = 2;

    public static final int CHANNEL_GREEN = 1;

    public static final int CHANNEL_BLUE = 0;

    public static final int NB_COLOR_CHANNEL = 3;

    private int[][] imageData = new int[0][0];

    /**
     * Empty constructor.
     */
    public Image() {

    }

    /**
     * Constructs a new Image corresponding to the specified BufferedImage.
     * @param pImage
     */
    public Image(BufferedImage pImage) {
        imageData = ImageUtils.bufferedImageToSRGBIntArray(pImage);
    }

    /**
     * Copy constructor.
     * @param pImage
     */
    public Image(Image pImage) {
        imageData = ArrayUtils.copy(pImage.imageData);
    }

    /**
     * Constructs an empty white image with specified size.
     * @param pWidth
     * @param pHeight
     */
    public Image(int pWidth, int pHeight) {
        imageData = new int[pHeight][pWidth];
        resetImage();
    }

    /**
     * Constructs a new Image with the specified image data.
     * @param pImageData
     */
    public Image(int[][] pImageData) {
        imageData = pImageData;
    }

    /**
     * Returns this image as a BufferedImage.
     * @return
     */
    public BufferedImage getBufferedImage() {
        return ImageUtils.sRGBIntArrayToBufferedImage(imageData);
    }

    /**
     * Returns the specified channel (R, G, B or A) as an integer array (values between 0 and 255).
     * @param pChannel
     * @return
     */
    public int[][] getChannel(int pChannel) {
        int h = getHeight();
        int w = getWidth();
        int[][] channelData = new int[h][w];
        for (int i = 0; i < h; ++i) {
            for (int j = 0; j < w; ++j) {
                channelData[i][j] = (imageData[i][j] >> (pChannel * 8)) & 0xff;
            }
        }
        return channelData;
    }

    /**
     * Returns the specified channel (R, G, B or A) as a double array (values between 0.0 and 1.0).
     * @param pChannel
     * @return
     */
    public double[][] getChannelDouble(int pChannel) {
        return ArrayConvert.floatToDouble2dArray(getChannelFloat(pChannel));
    }

    /**
     * Returns the specified channel (R, G, B or A) as a float array (values between 0.0f and 1.0f).
     * @param pChannel
     * @return
     */
    public float[][] getChannelFloat(int pChannel) {
        int[][] channel = getChannel(pChannel);
        return ImageUtils.channelIntArrayToFloatArray(channel);
    }

    /**
     * Returns the image height.
     * @return
     */
    public int getHeight() {
        return imageData.length;
    }

    /**
     * Returns the image data (sRGB format).
     * @return
     */
    public int[][] getImageData() {
        return ArrayUtils.copy(imageData);
    }

    /**
     * Returns the image width.
     * @return
     */
    public int getWidth() {
        if (imageData.length > 0) {
            return imageData[0].length;
        }
        else {
            return 0;
        }
    }

    /**
     * Resets the image to a white image.
     */
    public void resetImage() {
        int white = (255 << 24) + (255 << 16) + (255 << 8) + 255;
        resetImage(white);
    }

    /**
     * Resets the image to the specified color. The color must be specified as an integer.
     * @param pColor
     */
    public void resetImage(int pColor) {
        int h = getHeight();
        int w = getWidth();
        for (int i = 0; i < h; ++i) {
            for (int j = 0; j < w; ++j) {
                imageData[i][j] = pColor;
            }
        }
    }

    /**
     * Sets the specified channel of the image to the specified channel data. The data must be a double array with
     * values between 0.0 and 1.0.
     * @param pChannel
     * @param pChannelData
     */
    public void setChannel(int pChannel, double[][] pChannelData) {
        int[][] channelDataInt = ImageUtils
            .floatArrayToChannelIntArray(ArrayConvert.doubleToFloat2dArray(pChannelData));
        setChannel(pChannel, channelDataInt);
    }

    /**
     * Sets the specified channel of the image to the specified channel data. The data must be a float array with values
     * between 0.0f and 1.0f.
     * @param pChannel
     * @param pChannelData
     */
    public void setChannel(int pChannel, float[][] pChannelData) {
        int[][] channelDataInt = ImageUtils.floatArrayToChannelIntArray(pChannelData);
        setChannel(pChannel, channelDataInt);
    }

    /**
     * Sets the specified channel of the image to the specified value (between 0 and 255).
     * @param pChannel
     * @param pChannelValue
     */
    public void setChannel(int pChannel, int pChannelValue) {
        int h = getHeight();
        int w = getWidth();
        int mask = ~(255 << (pChannel * 8));
        for (int i = 0; i < h; ++i) {
            for (int j = 0; j < w; ++j) {
                imageData[i][j] = imageData[i][j] & mask;
                imageData[i][j] += (pChannelValue << (pChannel * 8));
            }
        }
    }

    /**
     * Sets the specified channel of the image to the specified channel data. The data must be an integer array with
     * values between 0 and 255.
     * @param pChannel
     * @param pChannelData
     */
    public void setChannel(int pChannel, int[][] pChannelData) {
        int h = getHeight();
        int w = getWidth();
        int mask = ~(255 << (pChannel * 8));
        for (int i = 0; i < h; ++i) {
            for (int j = 0; j < w; ++j) {
                imageData[i][j] = imageData[i][j] & mask;
                imageData[i][j] += (pChannelData[i][j] << (pChannel * 8));
            }
        }
    }

    /**
     * Sets the image data.
     * @param pImageData
     */
    public void setImageData(int[][] pImageData) {
        imageData = pImageData;
    }
}