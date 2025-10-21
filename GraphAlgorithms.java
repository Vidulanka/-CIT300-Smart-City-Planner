package Smart_City_Route;

import java.util.*;

public class GraphAlgorithms {
	// BFS Traversal using Queue
    public static void bfsTraversal(GraphCore graph, int startId) {
        if (!graph.containsLocation(startId)) {
            System.out.println("Start location not found!");
            return;
        }
        
        Set<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();
        
        System.out.println("BFS Traversal starting from " + graph.getLocationName(startId) + ":");
        
        queue.offer(startId);
        visited.add(startId);
        
        while (!queue.isEmpty()) {
            int current = queue.poll();
            System.out.print(graph.getLocationName(current) + " ");
            
            List<GraphCore.Edge> neighbors = graph.getAdjacencyList().get(current);
            for (GraphCore.Edge edge : neighbors) {
                if (!visited.contains(edge.destination)) {
                    visited.add(edge.destination);
                    queue.offer(edge.destination);
                }
            }
        }
        System.out.println();
    }
    
    // DFS Traversal using Stack
    public static void dfsTraversal(GraphCore graph, int startId) {
        if (!graph.containsLocation(startId)) {
            System.out.println("Start location not found!");
            return;
        }
        
        Set<Integer> visited = new HashSet<>();
        Stack<Integer> stack = new Stack<>();
        
        System.out.println("DFS Traversal starting from " + graph.getLocationName(startId) + ":");
        
        stack.push(startId);
        
        while (!stack.isEmpty()) {
            int current = stack.pop();
            
            if (!visited.contains(current)) {
                visited.add(current);
                System.out.print(graph.getLocationName(current) + " ");
                
                List<GraphCore.Edge> neighbors = graph.getAdjacencyList().get(current);
                for (GraphCore.Edge edge : neighbors) {
                    if (!visited.contains(edge.destination)) {
                        stack.push(edge.destination);
                    }
                }
            }
        }
        System.out.println();
    }
    
    // Dijkstra's Algorithm for shortest path
    public static void findShortestPath(GraphCore graph, int startId, int endId) {
        if (!graph.containsLocation(startId) || !graph.containsLocation(endId)) {
            System.out.println("One or both locations not found!");
            return;
        }
        
        if (startId == endId) {
            System.out.println("Start and end locations are the same!");
            return;
        }
        
        Map<Integer, Integer> distances = new HashMap<>();
        Map<Integer, Integer> previous = new HashMap<>();
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(node -> node.distance));
        
        // Initialize distances
        for (int locationId : graph.getLocations().keySet()) {
            if (locationId == startId) {
                distances.put(locationId, 0);
            } else {
                distances.put(locationId, Integer.MAX_VALUE);
            }
            previous.put(locationId, -1);
        }
        
        priorityQueue.offer(new Node(startId, 0));
        
        while (!priorityQueue.isEmpty()) {
            Node current = priorityQueue.poll();
            int currentId = current.id;
            
            if (currentId == endId) {
                break;
            }
            
            if (current.distance > distances.get(currentId)) {
                continue;
            }
            
            List<GraphCore.Edge> neighbors = graph.getAdjacencyList().get(currentId);
            for (GraphCore.Edge edge : neighbors) {
                int newDistance = distances.get(currentId) + edge.distance;
                
                if (newDistance < distances.get(edge.destination)) {
                    distances.put(edge.destination, newDistance);
                    previous.put(edge.destination, currentId);
                    priorityQueue.offer(new Node(edge.destination, newDistance));
                }
            }
        }
        
        // Reconstruct and display path
        if (distances.get(endId) == Integer.MAX_VALUE) {
            System.out.println("No path exists between " + graph.getLocationName(startId) + 
                             " and " + graph.getLocationName(endId));
        } else {
            System.out.println("Shortest path from " + graph.getLocationName(startId) + 
                             " to " + graph.getLocationName(endId) + ":");
            System.out.println("Total distance: " + distances.get(endId));
            
            List<Integer> path = new ArrayList<>();
            int current = endId;
            
            while (current != -1) {
                path.add(current);
                current = previous.get(current);
            }
            
            Collections.reverse(path);
            
            for (int i = 0; i < path.size(); i++) {
                if (i > 0) System.out.print(" -> ");
                System.out.print(graph.getLocationName(path.get(i)) + " (" + path.get(i) + ")");
            }
            System.out.println();
        }
    }
    
    // Helper class for priority queue in Dijkstra's algorithm
    private static class Node {
        int id;
        int distance;
        
        Node(int id, int distance) {
            this.id = id;
            this.distance = distance;
        }
    }
}
