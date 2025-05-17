package ui;

import ads.ImageGraph;
import ads.PixelVertex;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Set;

public class MapPane extends HBox {
    private ImageGraph graph;
    private int map_width;
    private int map_height;
    private int cell_size;
    private Color[][] pixelData;
    private final Stage stage;

    public MapPane(Stage stage) {
        super(10);
        this.stage = stage;
        makeUI();
    }

    public int getMap_width() {
        return map_width;
    }

    public int getMap_height() {
        return map_height;
    }

    public int getCell_size() {
        return cell_size;
    }

    public ImageGraph getGraph() {
        return graph;
    }

    private void drawUI() {

    }

    private void extractImage(Image image) {
        PixelReader pixelReader = image.getPixelReader();
        map_width = (int) image.getWidth();
        map_height = (int) image.getHeight();

        System.out.printf("Width : %d Height: %d\n", map_width, map_height);
        if (map_height > 400 || map_width > 400) {
            System.err.println("Image is too large");
            throw new IllegalArgumentException("Image too large, limit is 400x400");
        }
        pixelData = new Color[map_height][map_width];
        graph = new ImageGraph(map_width, map_height);

        for (int y = 0; y < map_height; y++) {
            for (int x = 0; x < map_width; x++) {
                Color fxColour = pixelReader.getColor(x, y);
                pixelData[y][x] = fxColour;

                // create a new vertex based on the pixel
                double greyscale = (0.299 * fxColour.getRed() + 0.587 * fxColour.getGreen() + 0.114 * fxColour.getBlue());
                int pixelValue = (int) greyscale * 255;

                boolean isWalkable = greyscale > 0.5;
                graph.addVertex(y, x, pixelValue, isWalkable);
            }
        }

        System.out.println("Graph created with " + graph.getVertices().size() + " vertices");
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
                extractImage(image);
            } catch (Exception e) {
                System.err.println("Error loading image: " + e.getMessage());
            }
        }
    }

    private void makeUI() {
        VBox imageBox = new VBox(10);
        VBox controlBox = new VBox(10);
        ObservableList<String> options = FXCollections.observableArrayList("Yellow", "Red", "Black");
        ComboBox<String> severityDropdown = new ComboBox<>(options);

        Button imageBtn = new Button("Load Image");

        controlBox.getChildren().add(imageBtn);
        imageBtn.setOnMouseClicked(e -> {
            try {
                loadImage(stage);
                severityDropdown.setValue("Choose Severity");

                controlBox.getChildren().remove(imageBtn);
                imageBtn.setText("Load New Image");
                controlBox.getChildren().addAll(severityDropdown, imageBtn);

                GridPane grid = displayPixelGrid(stage);
                imageBox.getChildren().add(grid);

                stage.sizeToScene();
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }

        });

        this.getChildren().addAll(imageBox, controlBox);
    }


    private GridPane displayPixelGrid(Stage stage) {
        GridPane grid = new GridPane();
        int cellSize = Math.max(1, Math.min(400 / map_width, 400 / map_height)); // Adjust cell size to fit window

        Set<PixelVertex> vertices = graph.getVertices();

        for (PixelVertex pixel : vertices) {
            Rectangle pixelCell = new Rectangle(cellSize, cellSize);
            int col = pixel.getCol();
            int row = pixel.getRow();
            pixelCell.setFill(pixelData[row][col]);
            grid.add(pixelCell, col, row);

            pixelCell.setOnMouseClicked(e -> {
                System.out.printf("Clicked at {%d, %d} \n", row, col);
            });
        }

//        for (int y = 0; y < map_height; y++) {
//            for (int x = 0; x < map_width; x++) {
//                Rectangle pixelCell = new Rectangle(cellSize, cellSize);
//                pixelCell.setFill(pixelData[y][x]);
//                grid.add(pixelCell, x, y);
//
//                final int pixelX = x;
//                final int pixelY = y;
//                pixelCell.setOnMouseClicked(event -> {
//                    System.out.println("Clicked on cell");
//                });
//            }
//        }
        return grid;
    }

}
