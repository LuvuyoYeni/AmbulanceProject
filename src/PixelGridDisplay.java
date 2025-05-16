import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

public class PixelGridDisplay extends Application {

    private Color[][] pixelData;
    private int imageWidth;
    private int imageHeight;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Pixel Grid from Image");

        Button loadButton = new Button("Load Image");
        loadButton.setOnAction(event -> loadImage(primaryStage));

        GridPane gridPane = new GridPane();

        VBox root = new VBox(10, loadButton, gridPane);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadImage(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            try {
                Image image = new Image(selectedFile.toURI().toString());
                imageWidth = (int) image.getWidth();
                imageHeight = (int) image.getHeight();
                pixelData = new Color[imageHeight][imageWidth];
                extractPixelColors(image);
                displayPixelGrid(stage);
            } catch (Exception e) {
                System.err.println("Error loading image: " + e.getMessage());
            }
        }
    }

    private void extractPixelColors(Image image) {
        PixelReader pixelReader = image.getPixelReader();
        if (pixelReader != null) {
            for (int y = 0; y < imageHeight; y++) {
                for (int x = 0; x < imageWidth; x++) {
                    javafx.scene.paint.Color fxColor = pixelReader.getColor(x, y);
                    pixelData[y][x] = fxColor;
                }
            }
        } else {
            System.err.println("Could not get PixelReader for the image.");
        }
    }

    private void displayPixelGrid(Stage primaryStage) {
        GridPane gridPane = new GridPane();
        int cellSize = Math.max(1, Math.min(400 / imageWidth, 400 / imageHeight)); // Adjust cell size to fit window

        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < imageWidth; x++) {
                Rectangle pixelCell = new Rectangle(cellSize, cellSize);
                pixelCell.setFill(pixelData[y][x]);
                gridPane.add(pixelCell, x, y);

                final int pixelX = x;
                final int pixelY = y;
                pixelCell.setOnMouseClicked(event -> {
                    System.out.println("Clicked on pixel at (" + pixelX + ", " + pixelY + ") with color: " + pixelData[pixelY][pixelX]);
                    // You can perform actions based on the clicked pixel here
                });
            }
        }

        VBox root = (VBox) primaryStage.getScene().getRoot();
        root.getChildren().add(gridPane);
        primaryStage.sizeToScene(); // Adjust window size to fit content
    }

    public static void main(String[] args) {
        launch(args);
    }
}