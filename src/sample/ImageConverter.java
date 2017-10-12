package sample;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.awt.image.BufferedImage;

/**
 * Created by gardi on 09.09.2017.
 */
public class ImageConverter {

    public Mat bufImg2Mat(BufferedImage in) {
        Mat out;
        int r, g, b;
        byte[] data;
        int[] dataBuf = in.getRGB(0, 0, in.getWidth(), in.getHeight(), null, 0, in.getWidth());

        if(in.getType() == BufferedImage.TYPE_3BYTE_BGR) {
            out = new Mat(in.getHeight(), in.getWidth(), CvType.CV_8UC3);
            data = new byte[in.getHeight() * in.getWidth() * (int)out.elemSize()];
            for(int i = 0; i < dataBuf.length; i++) {
                data[i * 3] = (byte) ((dataBuf[i] >> 16) & 0xff);
                data[i * 3 + 1] = (byte) ((dataBuf[i] >> 8) & 0xff);
                data[i * 3 + 2] = (byte) (dataBuf[i] & 0xff);
            }
        }
        else {
            out = new Mat(in.getHeight(), in.getWidth(), CvType.CV_8UC1);
            data = new byte[in.getHeight() * in.getWidth() * (int)out.elemSize()];
            for(int i = 0; i < dataBuf.length; i++) {
                r = (byte) ((dataBuf[i] >> 16) & 0xff);
                g = (byte) ((dataBuf[i] >> 8) & 0xff);
                b = (byte) (dataBuf[i] & 0xff);
                data[i] = (byte) (0.21 * r + 0.71 * g + 0.07 * b);
            }
        }

        out.put(0, 0, data);

        return out;
    }

    public BufferedImage mat2bufImg(Mat in) {
        BufferedImage out;
        int type;

        byte[] data = new byte[in.rows() * in.cols() * (int)in.elemSize()];
        in.get(0, 0 ,data);

        type = (in.channels() == 1) ? BufferedImage.TYPE_BYTE_GRAY : BufferedImage.TYPE_3BYTE_BGR;
        out = new BufferedImage(in.cols(), in.rows(), type);
        out.getRaster().setDataElements(0, 0 , in.cols(), in.rows(), data);

        return out;
    }

    public BufferedImage enlargeBufImg(BufferedImage in, int maskSize) {
        int rgb;
        int width = in.getWidth();
        int height = in.getHeight();
        BufferedImage out = new BufferedImage(width + maskSize * 2, height + maskSize * 2, in.getType());

        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                rgb = in.getRGB(i, j);
                out.setRGB(i + maskSize, j + maskSize, rgb);
            }
        }

        for(int i = 0; i < maskSize; i++) {
            for(int j = 0; j < width; j++) {
                rgb = in.getRGB(j, 0);
                out.setRGB(j + maskSize, i, rgb);
                rgb = in.getRGB(j, height - 1);
                out.setRGB(j + maskSize, i + height + maskSize, rgb);
            }
        }

        for(int i = 0; i < maskSize; i++) {
            for(int j = 0; j < height; j++) {
                rgb = in.getRGB(0, j);
                out.setRGB(i, j + maskSize, rgb);
                rgb = in.getRGB(width - 1, j);
                out.setRGB(i + width + maskSize, j + maskSize, rgb);
            }
        }

        for(int i = 0; i < maskSize; i++) {
            for(int j = 0; j < maskSize; j++) {
                rgb = in.getRGB(0, 0);
                out.setRGB(0 + j, 0 + i, rgb);
                rgb = in.getRGB(width - 1, 0);
                out.setRGB(width + j + maskSize, 0 + i, rgb);
                rgb = in.getRGB(0, height - 1);
                out.setRGB(0 + j, height + i + maskSize, rgb);
                rgb = in.getRGB(width - 1, height - 1);
                out.setRGB(width + j + maskSize, height + i + maskSize, rgb);
            }
        }

        return out;
    }

    public BufferedImage buf2grayscale(BufferedImage in) {
        BufferedImage out;
        Mat mIn = bufImg2Mat(in);

        for(int i = 0; i < mIn.rows(); i++) {
            for(int j = 0; j < mIn.cols(); j++) {
                double[] pixel = mIn.get(i, j);
                double[] nPixel = new double[3];
                for(int k = 0; k < 3; k++) {
                    nPixel[k] = pixel[0] * 0.3 + pixel[1] * 0.59 + pixel[2] * 0.11;
                }
                mIn.put(i, j, nPixel);
            }
        }

        out = mat2bufImg(mIn);

        return out;
    }
}