package utils;

import java.awt.image.BufferedImage;

public class ImageProcessor {
    public static void ExtractImage(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                int rgb = image.getRGB(r, c);
                int red = rgb << 16;
                int blue = rgb << 8;
                System.out.println(red + " " + blue + " " + rgb);
            }
        }
    }
}
