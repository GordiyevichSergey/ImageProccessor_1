package sample;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.opencv.core.Mat;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gardi on 09.09.2017.
 */
public class HistDrawer {
    private static final int PIXELS = 256;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;

    private List<CategoryChart> categoryCharts;
    private ImageConverter imageConverter;

    public HistDrawer() {
        categoryCharts = new ArrayList<>();
        imageConverter = new ImageConverter();
    }

    public void drawHistogram(BufferedImage img) {
        Mat mImg = imageConverter.bufImg2Mat(img);

        int[] x = getHistX();
        int[][] y = getHistY(mImg);

        for(int i = 0; i < 4; i++) {
            String title = null;
            switch(i) {
                case 0:
                    title = "R";
                    break;
                case 1:
                    title = "G";
                    break;
                case 2:
                    title = "B";
                    break;
                case 3:
                    title = "RGB";
                default:
                    break;
            }

            CategoryChart chart = new CategoryChartBuilder().width(WIDTH).height(HEIGHT).title(title).
                    xAxisTitle("").yAxisTitle("").build();
            chart.addSeries(title, x, y[i]);
            categoryCharts.add(chart);
        }

        new SwingWrapper<>(categoryCharts).displayChartMatrix();
    }

    private int[] getHistX() {
        int[] x = new int[PIXELS];

        for(int i = 0; i < x.length; i++) {
            x[i] = i;
        }

        return x;
    }

    private int[][] getHistY(Mat img) {
        int[][] out = new int[4][PIXELS];

        for(int i = 0; i < img.rows(); i++) {
            for(int j = 0; j < img.cols(); j++) {
                double[] pixel = img.get(i, j);
                out[0][(int) pixel[0]]++;
                out[1][(int) pixel[1]]++;
                out[2][(int) pixel[2]]++;
                out[3][(int) pixel[0]]++;
                out[3][(int) pixel[1]]++;
                out[3][(int) pixel[2]]++;
            }
        }

        return out;
    }
}