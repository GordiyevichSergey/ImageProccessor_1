package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Controller {
    @FXML
    private Button showNormalImageButton, showProcessedImageButton, showFilteredImageButton;
    @FXML
    private Button processImageButton, filterImageButton;
    @FXML
    private Button showNormalHistogramButton, showProcessedHistogramButton, showFilteredHistogramButton;
    @FXML
    private Button convertToGrayScaleButton;
    @FXML
    private TextField maskSizeTextField;
    @FXML
    private RadioButton mRadioButton;
    @FXML
    private CheckBox grayscaleCheckBox;

    private ImageProcessor imageProcessor;
    private HistDrawer histDrawer;
    private ImageConverter imageConverter;
    private BufferedImage normalImage, processedImage, filteredImage, grayScaleImage;

    public void initialize() {
        imageProcessor = new ImageProcessor();
        histDrawer = new HistDrawer();
        imageConverter = new ImageConverter();
        disable();
    }

    private void disable() {
        showNormalImageButton.setDisable(true);
        processImageButton.setDisable(true);
        filterImageButton.setDisable(true);
        showProcessedImageButton.setDisable(true);
        showFilteredImageButton.setDisable(true);
        showNormalHistogramButton.setDisable(true);
        showProcessedHistogramButton.setDisable(true);
        showFilteredHistogramButton.setDisable(true);
        maskSizeTextField.setDisable(true);
        convertToGrayScaleButton.setDisable(true);
        grayscaleCheckBox.setDisable(true);
    }

    @FXML
    public void onLoadImageButtonClicked() {
        disable();

        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);

        try {
            normalImage = ImageIO.read(file);

            showNormalImageButton.setDisable(false);
            processImageButton.setDisable(false);
            filterImageButton.setDisable(false);
            showNormalHistogramButton.setDisable(false);
            maskSizeTextField.setDisable(false);
            convertToGrayScaleButton.setDisable(false);
        } catch (IOException ex) {
            normalImage = null;
        } catch (IllegalArgumentException ex) {
            normalImage = null;
        }
    }

    @FXML
    public void onProcessImageButtonClicked() {
        if(grayscaleCheckBox.isSelected()) {
            processedImage = imageProcessor.processImage(grayScaleImage);
        }
        else {
            processedImage = imageProcessor.processImage(normalImage);
        }
        showProcessedImageButton.setDisable(false);
        showProcessedHistogramButton.setDisable(false);
    }

    @FXML
    public void onFilterImageButtonClicked() {
        int maskSize;
        if(maskSizeTextField.getText().isEmpty()) {
            maskSize = 1;
        }
        else {
            maskSize = Integer.parseInt(maskSizeTextField.getText());
        }

        boolean type = mRadioButton.isSelected();
        if(grayscaleCheckBox.isSelected()) {
            filteredImage = imageProcessor.filterImage(grayScaleImage, maskSize, type);
        }
        else {
            filteredImage = imageProcessor.filterImage(normalImage, maskSize, type);
        }

        showFilteredImageButton.setDisable(false);
        showFilteredHistogramButton.setDisable(false);
    }

    @FXML
    public void onShowNormalImageClicked() {
        Image image;

        if(grayscaleCheckBox.isSelected()) {
            image = SwingFXUtils.toFXImage(grayScaleImage, null);
        }
        else {
            image = SwingFXUtils.toFXImage(normalImage, null);
        }

        ImageStage stage = new ImageStage(image);
        stage.show();
    }

    @FXML
    public void onShowProcessedImageButtonClicked() {
        Image image = SwingFXUtils.toFXImage(processedImage, null);
        ImageStage stage = new ImageStage(image);
        stage.show();
    }

    @FXML
    public void onShowFilteredImageButtonClicked() {
        Image image = SwingFXUtils.toFXImage(filteredImage, null);
        ImageStage stage = new ImageStage(image);
        stage.show();
    }

    @FXML
    public void onShowNormalHistogramButtonClicked() {
        if(grayscaleCheckBox.isSelected()) {
            histDrawer.drawHistogram(grayScaleImage);
        }
        else {
            histDrawer.drawHistogram(normalImage);
        }
    }

    @FXML
    public void onShowProcessedHistogramButtonClicked() {
        histDrawer.drawHistogram(processedImage);
    }

    @FXML
    public void onShowFilteredHistogramButtonClicked() {
        histDrawer.drawHistogram(filteredImage);
    }

    @FXML
    public void onConvertToGrayScaleButtonClicked() {
        grayScaleImage = imageConverter.buf2grayscale(normalImage);
        grayscaleCheckBox.setDisable(false);
    }
}
