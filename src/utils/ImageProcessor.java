package utils;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

public class ImageProcessor {

    private Color[][] pixelData;

    public static void ExtractImage(Image image) throws Exception {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        if (width > 350 || height > 350) {
            throw new Exception("Image too large");
        }


        PixelReader pixelReader = image.getPixelReader();
    }
}
