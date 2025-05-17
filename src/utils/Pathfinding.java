package utils;

import ads.ImageGraph;
import ads.PixelVertex;

import java.util.*;

public class Pathfinding {
    public record PathResult(List<PixelVertex> path, Set<PixelVertex> explored) {
    }
/*
    public static PathResult findPathWithTracking(EnemyBehavior behavior, ImageGraph graph,
                                                  PixelVertex start, PixelVertex goal) {
        if (behavior instanceof AggressiveBehavior || behavior instanceof HunterBehavior) {
            return astarWithTracking(graph, start, goal);
        } else {
            return bfsWithTracking(graph, start, goal);
        }
    }*/

    public static PathResult bfsWithTracking(ImageGraph graph, PixelVertex start, PixelVertex goal) {
        if (graph == null || start == null || goal == null) {
            return new PathResult(Collections.emptyList(), Collections.emptySet());
        }

        Queue<PixelVertex> queue = new LinkedList<>();
        Map<PixelVertex, PixelVertex> cameFrom = new HashMap<>();
        Set<PixelVertex> explored = new HashSet<>();

        queue.add(start);
        explored.add(start);
        cameFrom.put(start, null);

        while (!queue.isEmpty()) {
            PixelVertex current = queue.poll();

            if (current.equals(goal)) {
                break;
            }

            for (PixelVertex neighbor : graph.getNeighbours(current)) {
                if (!explored.contains(neighbor)) {
                    explored.add(neighbor);
                    cameFrom.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }

        return new PathResult(reconstructPath(cameFrom, start, goal), explored);
    }

    public static PathResult astarWithTracking(ImageGraph graph, PixelVertex start, PixelVertex goal) {
        if (graph == null || start == null || goal == null) {
            return new PathResult(Collections.emptyList(), Collections.emptySet());
        }

        PriorityQueue<PixelVertex> queue = new PriorityQueue<>(
                Comparator.comparingInt(node -> node.getDistance() + heuristic(node, goal))
        );

        Map<PixelVertex, Integer> gScore = new HashMap<>();
        Map<PixelVertex, PixelVertex> cameFrom = new HashMap<>();
        Set<PixelVertex> explored = new HashSet<>();

        for (PixelVertex node : graph.getVertices()) {
            gScore.put(node, Integer.MAX_VALUE);
            node.setDistance(Integer.MAX_VALUE);
        }

        gScore.put(start, 0);
        start.setDistance(heuristic(start, goal));
        queue.add(start);
        cameFrom.put(start, null);

        while (!queue.isEmpty()) {
            PixelVertex current = queue.poll();
            explored.add(current);

            if (current.equals(goal)) {
                break;
            }

            for (PixelVertex neighbor : graph.getNeighbours(current)) {
                int tentativeG = gScore.get(current) + 1;
                if (tentativeG < gScore.get(neighbor)) {
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tentativeG);
                    neighbor.setDistance(tentativeG + heuristic(neighbor, goal));

                    // Update priority queue
                    queue.remove(neighbor);
                    queue.add(neighbor);
                }
            }
        }

        return new PathResult(reconstructPath(cameFrom, start, goal), explored);
    }

    public static List<PixelVertex> bfs(ImageGraph graph, PixelVertex start, PixelVertex goal) {
        return bfsWithTracking(graph, start, goal).path;
    }

    public static List<PixelVertex> dijkstra(ImageGraph graph, PixelVertex start, PixelVertex goal) {
        if (graph == null || start == null || goal == null) {
            return Collections.emptyList();
        }

        PriorityQueue<PixelVertex> queue = new PriorityQueue<>(
                Comparator.comparingInt(PixelVertex::getDistance)
        );

        Map<PixelVertex, Integer> distances = new HashMap<>();
        Map<PixelVertex, PixelVertex> cameFrom = new HashMap<>();

        for (PixelVertex node : graph.getVertices()) {
            distances.put(node, Integer.MAX_VALUE);
            node.setDistance(Integer.MAX_VALUE);
        }

        start.setDistance(0);
        distances.put(start, 0);
        queue.add(start);
        cameFrom.put(start, null);

        while (!queue.isEmpty()) {
            PixelVertex current = queue.poll();

            if (current.equals(goal)) {
                break;
            }

            for (PixelVertex neighbor : graph.getNeighbours(current)) {
                int newDist = distances.get(current) + 1;
                if (newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    neighbor.setDistance(newDist);
                    cameFrom.put(neighbor, current);

                    queue.remove(neighbor);
                    queue.add(neighbor);
                }
            }
        }

        return reconstructPath(cameFrom, start, goal);
    }

    public static List<PixelVertex> astar(ImageGraph graph, PixelVertex start, PixelVertex goal) {
        return astarWithTracking(graph, start, goal).path;
    }

    private static int heuristic(PixelVertex a, PixelVertex b) {
        // Manhattan distance
        return Math.abs(a.getRow() - b.getRow()) + Math.abs(a.getCol() - b.getCol());
    }

    private static List<PixelVertex> reconstructPath(Map<PixelVertex, PixelVertex> cameFrom,
                                                     PixelVertex start, PixelVertex goal) {
        List<PixelVertex> path = new ArrayList<>();
        if (!cameFrom.containsKey(goal)) return path;

        PixelVertex current = goal;
        while (current != null && !current.equals(start)) {
            path.add(current);
            current = cameFrom.get(current);
        }
        Collections.reverse(path);

        return path;
    }

    // Behavior-specific implementations
//    public static class BehaviorMethods {
//
//        public static PathResult cautiousPath(ImageGraph graph, PixelVertex start, PixelVertex target) {
//            // Create modified graph with wall penalties
//            ImageGraph modifiedGraph = new ImageGraph(graph.getRows(), graph.getCols());
//
//            // Copy walls and add danger zones
//            for (PixelVertex node : graph.getAllNodes()) {
//                if (graph.isWall(node)) {
//                    modifiedGraph.setWall(modifiedGraph.getNode(node.row, node.col), true);
//                    // Mark adjacent nodes as dangerous
//                    for (PixelVertex neighbor : graph.getNeighbors(node)) {
//                        PixelVertex modNode = modifiedGraph.getNode(neighbor.row, neighbor.col);
//                        modNode.distance += 5; // Penalty
//                    }
//                }
//            }
//
//            return astarWithTracking(modifiedGraph, start, target);
//        }
//
//        public static PathResult hunterPath(ImageGraph graph, PixelVertex start, PixelVertex target,
//                                            int lastPlayerX, int lastPlayerY) {
//            // Predict movement
//            int predictedX = target.col + (target.col - lastPlayerX);
//            int predictedY = target.row + (target.row - lastPlayerY);
//
//            PixelVertex predictedTarget = graph.getNode(predictedY, predictedX);
//            if (predictedTarget != null && !graph.isWall(predictedTarget)) {
//                return astarWithTracking(graph, start, predictedTarget);
//            }
//            return astarWithTracking(graph, start, target);
//        }
//    }
}