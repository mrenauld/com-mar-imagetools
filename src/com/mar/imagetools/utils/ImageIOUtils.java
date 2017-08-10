package com.mar.imagetools.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.mar.framework.core.logging.Logger;

public class ImageIOUtils {

    /** JPG format name. */
    public static final String FORMAT_JPG = "jpg";

    /** PNG format name. */
    public static final String FORMAT_PNG = "png";

    /**
     * Reads an image file and returns the corresponding BufferedImage.
     * @param pPath
     * @return
     */
    public static BufferedImage readImage(String pPath) {
        File imageFile = new File(pPath);
        BufferedImage image = null;
        try {
            image = ImageIO.read(imageFile);
        }
        catch (IOException e) {
            Logger.logError(ImageIOUtils.class, "IOException [" + e.toString() + "]");
        }
        return image;
    }

    /**
     * Writes the specified BufferedImage to the specified path using the specified format.
     * @param pImage
     * @param pPath
     * @param pFormat
     */
    public static void writeImage(BufferedImage pImage, String pPath, String pFormat) {
        File imageFile = new File(pPath);
        try {
            ImageIO.write(pImage, pFormat, imageFile);
        }
        catch (IOException e) {
            Logger.logError(ImageIOUtils.class, "IOException [" + e.toString() + "]");
        }
    }

}
