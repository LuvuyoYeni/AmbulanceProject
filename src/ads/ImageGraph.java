package ads;


import javafx.scene.shape.VertexFormat;

import java.util.*;

// This will be an Adjacency Map Graph
public class ImageGraph {
    private final Map<PixelVertex, Set<PixelEdge>> adjancyMap;
    private final Set<PixelEdge> edges;
    private final Set<PixelVertex> vertices;
    private final int imageWidth;
    private final int imageHeight;
    private final PixelVertex[][] vertexGrid;

    public ImageGraph(int width, int height) {
        adjancyMap = new HashMap<>();
        edges = new HashSet<>();
        vertices = new HashSet<>();
        imageWidth = width;
        imageHeight = height;
        vertexGrid = new PixelVertex[imageHeight][imageWidth];
    }

    private void addVertex(int r, int c, int pixelValue, boolean isWalkable) {

        if (r < 0 || r >= imageHeight || c < 0 || c >= imageWidth) {
            System.err.println("Tried adding a vertex in an invalid location");
            return;
        }
        PixelVertex newVertex = new PixelVertex(r, c, pixelValue, isWalkable);

        if (vertices.contains(newVertex)) {
            return;
        }

        vertices.add(newVertex);
        adjancyMap.put(newVertex, new HashSet<>());
        vertexGrid[r][c] = newVertex;

        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};

        for (int i = 0; i < 4; i++) {
            int nr = r + dr[i];
            int nc = c + dc[i];

            if (nc < 0 || nc >= imageWidth || nr < 0 || nr >= imageHeight) {
                continue;
            }

            PixelVertex neighbor = vertexGrid[nr][nc];
            if (neighbor != null && neighbor.isWalkable() && newVertex.isWalkable()) {
                int weight = Math.abs(newVertex.getPixelValue() - neighbor.getPixelValue());
                PixelEdge edge1 = new PixelEdge(newVertex, neighbor, weight);
                adjancyMap.get(newVertex).add(edge1);
                edges.add(edge1);

                PixelEdge edge2 = new PixelEdge(neighbor, newVertex, weight);
                adjancyMap.get(neighbor).add(edge2);
                edges.add(edge2);
            }
        }
    }

    public Set<PixelVertex> getVertices() {
        return vertices;
    }

    public Set<PixelEdge> getEdges() {
        return edges;
    }

    public Set<PixelEdge> getEdges(PixelVertex vertex) {
        return adjancyMap.getOrDefault(vertex, new HashSet<>());
    }

    public Set<PixelVertex> getNeighbours(PixelVertex vertex) {
        Set<PixelVertex> neighbours = new HashSet<>();
        if (adjancyMap.containsKey(vertex)) {
            for (PixelEdge edge : adjancyMap.get(vertex)) {
                if (edge.getSource().equals(vertex)) {
                    neighbours.add(edge.getDestination());
                } else if (edge.getDestination().equals(vertex)) {
                    neighbours.add(edge.getSource());
                }
            }
        }
        return neighbours;
    }
}
