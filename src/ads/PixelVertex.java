package ads;

import java.util.HashMap;
import java.util.Map;

public class PixelVertex {
    private int pixelValue;
    private int row, col;
    private boolean isWalkable;

    public PixelVertex(int row, int col, int pixelValue, boolean walkable) {
        this.pixelValue = pixelValue;
        this.row = row;
        this.col = col;
        this.isWalkable = walkable;
    }

    public int getPixelValue() {
        return pixelValue;
    }

    public boolean isWalkable() {
        return isWalkable;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
