package ads;


public class PixelEdge {
    private PixelVertex source, destination;
    private int weight;

    public PixelEdge(PixelVertex s, PixelVertex d, int w) {
        source = s;
        destination = d;
        weight = w;
    }

    public PixelVertex getSource() {
        return source;
    }

    public PixelVertex getDestination() {
        return destination;
    }

    public int getWeight() {
        return weight;
    }
}
