package core;

import ads.PixelVertex;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Entity {
    int x, y;
    List<PixelVertex> currentPath = new ArrayList<>();
    Set<PixelVertex> lastExploredNodes = new HashSet<>();
    Color color;
}
