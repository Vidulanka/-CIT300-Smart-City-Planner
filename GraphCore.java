package Smart_City_Route;

import java.util.*;

public class GraphCore {
	 private Map<Integer, String> locations;
	    private Map<Integer, List<Edge>> adjacencyList;
	    
	    public GraphCore() {
	        this.locations = new HashMap<>();
	        this.adjacencyList = new HashMap<>();
	    }
	    
	    // Edge class to represent roads between locations
	    public static class Edge {
	        public int destination;
	        public int distance;
	        
	        public Edge(int destination, int distance) {
	            this.destination = destination;
	            this.distance = distance;
	        }
	        
	        @Override
	        public String toString() {
	            return "-> " + destination + " (Distance: " + distance + ")";
	        }
	    }
	    
	    // Add a new location to the graph
	    public boolean addLocation(int locationId, String locationName) {
	        if (locations.containsKey(locationId)) {
	            return false;
	        }
	        
	        locations.put(locationId, locationName);
	        adjacencyList.put(locationId, new ArrayList<>());
	        return true;
	    }
	    
	    // Remove a location and all its connections
	    public boolean removeLocation(int locationId) {
	        if (!locations.containsKey(locationId)) {
	            return false;
	        }
	        
	        // Remove all edges pointing to this location
	        for (List<Edge> edges : adjacencyList.values()) {
	            edges.removeIf(edge -> edge.destination == locationId);
	        }
	        
	        // Remove the location and its edges
	        locations.remove(locationId);
	        adjacencyList.remove(locationId);
	        
	        return true;
	    }
	    
	    // Add a bidirectional road between two locations
	    public boolean addRoad(int sourceId, int destId, int distance) {
	        if (!locations.containsKey(sourceId) || !locations.containsKey(destId)) {
	            return false;
	        }
	        
	        // Add edge from source to destination
	        adjacencyList.get(sourceId).add(new Edge(destId, distance));
	        // Add edge from destination to source (bidirectional)
	        adjacencyList.get(destId).add(new Edge(sourceId, distance));
	        
	        return true;
	    }
	    
	    // Remove road between two locations
	    public boolean removeRoad(int sourceId, int destId) {
	        if (!locations.containsKey(sourceId) || !locations.containsKey(destId)) {
	            return false;
	        }
	        
	        boolean removed1 = adjacencyList.get(sourceId).removeIf(edge -> edge.destination == destId);
	        boolean removed2 = adjacencyList.get(destId).removeIf(edge -> edge.destination == sourceId);
	        
	        return removed1 || removed2;
	    }
	    
	    // Display all connections in the city
	    public void displayConnections() {
	        if (locations.isEmpty()) {
	            System.out.println("No locations in the city!");
	            return;
	        }
	        
	        for (Map.Entry<Integer, String> entry : locations.entrySet()) {
	            int locationId = entry.getKey();
	            String locationName = entry.getValue();
	            List<Edge> edges = adjacencyList.get(locationId);
	            
	            System.out.println(locationName + " (ID: " + locationId + ") connects to:");
	            
	            if (edges.isEmpty()) {
	                System.out.println("  No connections");
	            } else {
	                for (Edge edge : edges) {
	                    String destName = locations.get(edge.destination);
	                    System.out.println("  " + destName + " (ID: " + edge.destination + 
	                                     ", Distance: " + edge.distance + ")");
	                }
	            }
	            System.out.println();
	        }
	    }
	    
	    // Getter methods for algorithms
	    public Map<Integer, String> getLocations() {
	        return locations;
	    }
	    
	    public Map<Integer, List<Edge>> getAdjacencyList() {
	        return adjacencyList;
	    }
	    
	    public boolean containsLocation(int locationId) {
	        return locations.containsKey(locationId);
	    }
	    
	    public String getLocationName(int locationId) {
	        return locations.get(locationId);
	    }
	}
