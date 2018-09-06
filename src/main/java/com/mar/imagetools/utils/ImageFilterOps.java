package com.mar.imagetools.utils;

import com.mar.algotools.matrix.Kernel;
import com.mar.algotools.matrix.MatrixUtils;
import com.mar.algotools.signalprocessing.GaussianFactory;
import com.mar.imagetools.objects.Image;
import com.mar.imagetools.objects.ImageProc;

public class ImageFilterOps {

    /**
     * Returns an image blurred with a Gaussian filter of size 2*pK+1 x 2*pK+1
     * and with specified sigma.
     *
     * @param pImageProc
     * @param pK
     * @param pSigma
     * @return
     */
    public static ImageProc applyGaussianFilter(ImageProc pImageProc, int pK, double pSigma) {
        ImageProc blurredImage = new ImageProc(pImageProc.getWidth(), pImageProc.getHeight());
        for (int c = 0; c < Image.NB_COLOR_CHANNEL; ++c) {
            double[][] channel = pImageProc.getChannel(c);
            channel = GaussianFactory.applyGaussianFilter(channel, pK, pSigma);
            blurredImage.setChannel(c, channel);
        }
        return blurredImage;
    }

    /**
     * Returns an image filtered using the specified kernel.
     *
     * @param pImageProc
     * @param pKernel
     * @return
     */
    public static ImageProc applyKernelFilter(ImageProc pImageProc, double[][] pKernel) {
        ImageProc filteredImage = new ImageProc(pImageProc.getWidth(), pImageProc.getHeight());
        for (int c = 0; c < Image.NB_COLOR_CHANNEL; ++c) {
            double[][] channel = pImageProc.getChannel(c);
            channel = MatrixUtils.convolution(channel, pKernel, true);
            filteredImage.setChannel(c, channel);
        }
        return filteredImage;
    }

    /**
     * Returns an image sharpened with unsharp masking.
     *
     * @param pImageProc
     * @param pK
     * @param pSigma
     * @param pCoef
     * @return
     */
    public static ImageProc applyUnsharpMasking(ImageProc pImageProc, int pK, double pSigma, double pCoef) {
        /* Build a blurred image (the unsharp mask). */
        ImageProc blurredImage = applyGaussianFilter(pImageProc, pK, pSigma);

        /* Mask = (image - blurred image) * coef */
        ImageProc mask = new ImageProc(pImageProc);
        mask.subtractImage(blurredImage);
        mask.times(pCoef);
        mask.threshold(0.0, true);

        /* Sharpened image = image + mask */
        ImageProc sharp = new ImageProc(pImageProc);
        sharp.addImage(mask);

        return sharp;
    }

    /**
     * Returns an image filtered using the specified kernel.
     * 
     * @param pImageProc
     * @param pKernel
     * @return
     */
    public static ImageProc applyVaryingKernelFilter(ImageProc pImageProc, Kernel pKernel) {
        ImageProc filteredImage = new ImageProc(pImageProc.getWidth(), pImageProc.getHeight());
        for (int c = 0; c < Image.NB_COLOR_CHANNEL; ++c) {
            double[][] channel = pImageProc.getChannel(c);
            channel = MatrixUtils.convolution(channel, pKernel, true);
            filteredImage.setChannel(c, channel);
        }
        return filteredImage;
    }

}