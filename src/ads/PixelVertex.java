package ads;

public class PixelVertex {
    private final int pixelValue;
    private final int row;
    private final int col;
    private final boolean isWalkable;
    private int distance = Integer.MAX_VALUE;

    public PixelVertex(int row, int col, int pixelValue, boolean walkable) {
        this.pixelValue = pixelValue;
        this.row = row;
        this.col = col;
        this.isWalkable = walkable;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
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
