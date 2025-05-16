import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;

import java.io.File;
import java.awt.image.BufferedImage;

import javafx.embed.swing.SwingFXUtils; // For converting JavaFX Image to BufferedImage

public class ImageLoaderUI extends Application {

    private ImageView imageView;
    //private ImageGraph imageGraph;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Image to Graph");

        imageView = new ImageView();
        imageView.setFitWidth(400);
        imageView.setFitHeight(400);
        imageView.setPreserveRatio(true);

        Button loadButton = new Button("Load Image and Build Graph");
        loadButton.setOnAction(event -> loadImageAndBuildGraph(primaryStage));

        VBox root = new VBox(10);
        root.getChildren().addAll(loadButton, imageView);

        Scene scene = new Scene(root, 420, 450);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadImageAndBuildGraph(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            try {
                Image fxImage = new Image(selectedFile.toURI().toString());
                imageView.setImage(fxImage);

                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(fxImage, null);
                int width = bufferedImage.getWidth();
                int height = bufferedImage.getHeight();

                //imageGraph = new ImageGraph(width, height); // Initialize the graph

                // Extract image data and populate the graph
                for (int r = 0; r < height; r++) {
                    for (int c = 0; c < width; c++) {
                        int rgb = bufferedImage.getRGB(c, r);
                        int red = (rgb >> 16) & 0xFF;
                        int green = (rgb >> 8) & 0xFF;
                        int blue = rgb & 0xFF;

                        System.out.printf("{red: %s green: %s blue: %s}", red, green, blue);

                        // Determine pixel value (e.g., grayscale) and walkability
                        int grayScale = (red + green + blue) / 3;
                        boolean isWalkable = grayScale > 100; // Example: walkable if brighter than 100
                        //System.out.printf("{scale: %d walk: %b}", grayScale, isWalkable);

                        ///imageGraph.addVertex(r, c, grayScale, isWalkable);
                    }
                    System.out.println();
                }

                //System.out.println("Graph built. Number of vertices: " + imageGraph.getVertices().size());
                // You can now perform operations on your imageGraph
                // For example, print the graph:
                // System.out.println(imageGraph);

            } catch (Exception e) {
                System.err.println("Error loading or processing image: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}