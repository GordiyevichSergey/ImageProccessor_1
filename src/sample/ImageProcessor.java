package sample;

import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 * Created by gardi on 09.09.2017.
 */
public class ImageProcessor {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private ImageConverter imageConverter;

    public ImageProcessor() {
        imageConverter = new ImageConverter();
    }

    public BufferedImage processImage(BufferedImage in) {
        BufferedImage out;
        Mat mat = imageConverter.bufImg2Mat(in);

        for(int i = 0; i < mat.rows(); i++) {
            for(int j = 0; j < mat.cols(); j++) {
                double[] pixel = mat.get(i, j);
                for(int k = 0; k < pixel.length; k++) {
                    pixel[k] = 255 - pixel[k];
                }
                mat.put(i, j, pixel);
            }
        }

        out = imageConverter.mat2bufImg(mat);

        return out;
    }

    public BufferedImage filterImage(BufferedImage in, int maskSize, boolean type) {
        BufferedImage out;

        BufferedImage bufEnlarged = imageConverter.enlargeBufImg(in, maskSize);
        Mat matEnlarged = imageConverter.bufImg2Mat(bufEnlarged);
        Mat matOut = new Mat(in.getHeight(), in.getWidth(), matEnlarged.type());

        for(int i = 0; i < in.getHeight(); i++) {
            for(int j = 0; j < in.getWidth(); j++) {
                int areaSize = (maskSize + maskSize + 1) * (maskSize + maskSize + 1);
                double[][] pixels = new double[3][areaSize];
                double[] pixel = new double[3];

                for(int i1 = -maskSize; i1 <= maskSize; i1++) {
                    for(int j1 = -maskSize; j1 <= maskSize; j1++) {
                        pixel = matEnlarged.get(i + maskSize + i1, j + maskSize + j1);
                        for(int k = 0; k < 3; k++) {
                            pixels[k][(i1 + maskSize) * (int)Math.sqrt(areaSize) + (j1 + maskSize)] = pixel[k];
                        }
                    }
                }

                if(type) {
                    int mid = (areaSize / 2) + 1;
                    for (int k = 0; k < 3; k++) {
                        Arrays.sort(pixels[k]);
                        pixel[k] = pixels[k][mid];
                    }
                }
                else {
                    double[] sum = new double[3];
                    for(int k = 0; k < areaSize; k++) {
                        for(int k1 = 0; k1 < 3; k1++) {
                            if(pixels[k1][k] != 0) {
                                sum[k1] += 1 / pixels[k1][k];
                            }
                        }
                    }

                    for(int k = 0; k < 3; k++) {
                        pixel[k] = areaSize / sum[k];
                    }
                }

                matOut.put(i, j, pixel);
            }
        }

        out = imageConverter.mat2bufImg(matOut);

        return out;
    }
}