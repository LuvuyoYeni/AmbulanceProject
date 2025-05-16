import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;

import java.io.File;

public class InteractiveImageLoaderUI extends Application {

    private Canvas canvas;
    private Image loadedImage;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Interactive Image");

        canvas = new Canvas(400, 400); // Initial canvas size

        Button loadButton = new Button("Load Image");
        loadButton.setOnAction(event -> loadImage(primaryStage));

        VBox root = new VBox(10);
        root.getChildren().addAll(loadButton, canvas);

        Scene scene = new Scene(root, 420, 450);
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
                loadedImage = new Image(selectedFile.toURI().toString());
                redrawCanvas();
            } catch (Exception e) {
                System.err.println("Error loading image: " + e.getMessage());
            }
        }
    }

    private void redrawCanvas() {
        if (loadedImage != null) {
            double width = loadedImage.getWidth();
            double height = loadedImage.getHeight();

            // Resize canvas to image dimensions or a fixed size, maintaining aspect ratio
            double canvasWidth = 400;
            double canvasHeight = 400;
            double aspectRatio = width / height;

            if (width > canvasWidth || height > canvasHeight) {
                if (aspectRatio > 1) {
                    canvas.setWidth(canvasWidth);
                    canvas.setHeight(canvasWidth / aspectRatio);
                } else {
                    canvas.setHeight(canvasHeight);
                    canvas.setWidth(canvasHeight * aspectRatio);
                }
            } else {
                canvas.setWidth(width);
                canvas.setHeight(height);
            }

            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // Clear previous drawing

            // Disable image smoothing/interpolation
            gc.setImageSmoothing(false);

            gc.drawImage(loadedImage, 0, 0, canvas.getWidth(), canvas.getHeight());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}