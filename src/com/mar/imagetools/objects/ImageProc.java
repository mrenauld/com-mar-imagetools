package com.mar.imagetools.objects;

import com.mar.algotools.mathematics.utils.ArrayUtils;

/**
 * Represents an RGB image (without alpha channel). The data is stored as double arrays, in order to have more precision
 * than in {@link Image}.
 * @author mrenauld
 */
public class ImageProc {

    /** Image data: nb color channels x height x width. */
    private double[][][] channelData;

    /**
     * Constructs a new ImageProc from the specified {@link Image}.
     * @param pImage
     */
    public ImageProc(Image pImage) {
        channelData = new double[Image.NB_COLOR_CHANNEL][][];
        for (int c = 0; c < Image.NB_COLOR_CHANNEL; ++c) {
            channelData[c] = pImage.getChannelDouble(c);
        }
    }

    /**
     * Copy constructor.
     * @param pImage
     */
    public ImageProc(ImageProc pImage) {
        channelData = new double[Image.NB_COLOR_CHANNEL][][];
        for (int c = 0; c < Image.NB_COLOR_CHANNEL; ++c) {
            channelData[c] = ArrayUtils.copy(pImage.channelData[c]);
        }
    }

    /**
     * Constructs an empty ImageProc with specified width and height.
     * @param pWidth
     * @param pHeight
     */
    public ImageProc(int pWidth, int pHeight) {
        channelData = new double[Image.NB_COLOR_CHANNEL][][];
        for (int c = 0; c < Image.NB_COLOR_CHANNEL; ++c) {
            channelData[c] = new double[pHeight][pWidth];
        }
    }

    /**
     * Adds the specified offset to all channel data.
     * @param pOffset
     */
    public void add(double pOffset) {
        for (int c = 0; c < Image.NB_COLOR_CHANNEL; ++c) {
            add(c, pOffset);
        }
    }

    /**
     * Adds the specified offset array to all channel data (the array must have the same size as the image).
     * @param pOffsetArray
     */
    public void add(double[][] pOffsetArray) {
        for (int c = 0; c < Image.NB_COLOR_CHANNEL; ++c) {
            add(c, pOffsetArray);
        }
    }

    /**
     * Adds the specified offset to the specified channel data.
     * @param pChannelId
     * @param pOffset
     */
    public void add(int pChannelId, double pOffset) {
        for (int i = 0; i < getHeight(); ++i) {
            for (int j = 0; j < getWidth(); ++j) {
                channelData[pChannelId][i][j] += pOffset;
            }
        }
    }

    /**
     * Adds the specified offset array to the specified channel data (the array must have the same size as the image).
     * @param pChannelId
     * @param pOffsetArray
     */
    public void add(int pChannelId, double[][] pOffsetArray) {
        for (int i = 0; i < getHeight(); ++i) {
            for (int j = 0; j < getWidth(); ++j) {
                channelData[pChannelId][i][j] += pOffsetArray[i][j];
            }
        }
    }

    /**
     * Adds the specified ImageProc to this ImageProc.
     * @param pImage
     */
    public void addImage(ImageProc pImage) {
        for (int c = 0; c < Image.NB_COLOR_CHANNEL; ++c) {
            for (int i = 0; i < getHeight(); ++i) {
                for (int j = 0; j < getWidth(); ++j) {
                    channelData[c][i][j] += pImage.channelData[c][i][j];
                }
            }
        }
    }

    /**
     * Returns a new {@link Image} built from this ImageProc. All values below 0.0 or above 1.0 will be rounded to 0.0
     * and 1.0.
     * @return
     */
    public Image convertToImage() {
        int h = getHeight();
        int w = getWidth();
        Image image = new Image(w, h);
        for (int c = 0; c < Image.NB_COLOR_CHANNEL; ++c) {
            image.setChannel(c, channelData[c]);
        }
        return image;
    }

    /**
     * Returns the data for the specified channel.
     * @param pChannelId
     * @return
     */
    public double[][] getChannel(int pChannelId) {
        return ArrayUtils.copy(channelData[pChannelId]);
    }

    /**
     * Returns the image height.
     * @return
     */
    public int getHeight() {
        return channelData[0].length;
    }

    /**
     * Returns the image width.
     * @return
     */
    public int getWidth() {
        if (channelData[0].length > 0) {
            return channelData[0][0].length;
        }
        else {
            return 0;
        }
    }

    /**
     * Inverts the data: for each channel, for each pixel the new values is 1.0 minus the old one.
     */
    public void invert() {
        for (int c = 0; c < Image.NB_COLOR_CHANNEL; ++c) {
            for (int i = 0; i < getHeight(); ++i) {
                for (int j = 0; j < getWidth(); ++j) {
                    channelData[c][i][j] = 1.0 - channelData[c][i][j];
                }
            }
        }
    }

    /**
     * Normalizes the data so that all data sits between 0.0 and 1.0.
     */
    public void normalize() {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for (int c = 0; c < Image.NB_COLOR_CHANNEL; ++c) {
            for (int i = 0; i < getHeight(); ++i) {
                for (int j = 0; j < getWidth(); ++j) {
                    if (channelData[c][i][j] < min) {
                        min = channelData[c][i][j];
                    }
                    if (channelData[c][i][j] > max) {
                        max = channelData[c][i][j];
                    }
                }
            }
        }

        add(min);
        times(1.0 / max);
    }

    /**
     * Normalizes the data so that all data for the specified channel index sits between 0.0 and 1.0.
     * @param pChannelId
     */
    public void normalize(int pChannelId) {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for (int i = 0; i < getHeight(); ++i) {
            for (int j = 0; j < getWidth(); ++j) {
                if (channelData[pChannelId][i][j] < min) {
                    min = channelData[pChannelId][i][j];
                }
                if (channelData[pChannelId][i][j] > max) {
                    max = channelData[pChannelId][i][j];
                }
            }
        }

        add(pChannelId, min);
        times(pChannelId, 1.0 / max);
    }

    /**
     * Subtracts the specified ImageProc from this ImageProc.
     * @param pImage
     */
    public void subtractImage(ImageProc pImage) {
        for (int c = 0; c < Image.NB_COLOR_CHANNEL; ++c) {
            for (int i = 0; i < getHeight(); ++i) {
                for (int j = 0; j < getWidth(); ++j) {
                    channelData[c][i][j] -= pImage.channelData[c][i][j];
                }
            }
        }
    }

    /**
     * Sets the data for the specified channel index.
     * @param pChannelId
     * @param pChannelData
     */
    public void setChannel(int pChannelId, double[][] pChannelData) {
        channelData[pChannelId] = ArrayUtils.copy(pChannelData);
    }

    /**
     * Keeps only data above or below a specified threshold. The remaining data is set to 0.0.
     * @param pThreshold
     * @param pKeepUpper
     */
    public void threshold(double pThreshold, boolean pKeepUpper) {
        for (int c = 0; c < Image.NB_COLOR_CHANNEL; ++c) {
            for (int i = 0; i < getHeight(); ++i) {
                for (int j = 0; j < getWidth(); ++j) {
                    double v = Math.abs(channelData[c][i][j]);
                    if ((pKeepUpper && v < pThreshold) || (!pKeepUpper && v > pThreshold)) {
                        channelData[c][i][j] = 0.0;
                    }
                }
            }
        }
    }

    /**
     * Multiplies the data by the specified coefficient.
     * @param pCoef
     */
    public void times(double pCoef) {
        for (int c = 0; c < Image.NB_COLOR_CHANNEL; ++c) {
            times(c, pCoef);
        }
    }

    /**
     * Multiplies the data by the specified coefficient array (the array must have the same size as the image).
     * @param pCoefArray
     */
    public void times(double[][] pCoefArray) {
        for (int c = 0; c < Image.NB_COLOR_CHANNEL; ++c) {
            times(c, pCoefArray);
        }
    }

    /**
     * Multiplies the specified channel data by the specified coefficient.
     * @param pChannelId
     * @param pCoef
     */
    public void times(int pChannelId, double pCoef) {
        for (int i = 0; i < getHeight(); ++i) {
            for (int j = 0; j < getWidth(); ++j) {
                channelData[pChannelId][i][j] *= pCoef;
            }
        }
    }

    /**
     * Multiplies the specified channel data by the specified coefficient array (the array must have the same size as
     * the image).
     * @param pChannelId
     * @param pCoefArray
     */
    public void times(int pChannelId, double[][] pCoefArray) {
        for (int i = 0; i < getHeight(); ++i) {
            for (int j = 0; j < getWidth(); ++j) {
                channelData[pChannelId][i][j] *= pCoefArray[i][j];
            }
        }
    }

}