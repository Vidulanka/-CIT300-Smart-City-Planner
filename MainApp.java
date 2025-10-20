package SmartCityRoutePlanner;

import java.util.List;
import java.util.Scanner;

public class MainApp {

	// M4: Integration - Instances of other members' work
	 private static AVLTree avl = new AVLTree();
	 private static GraphCore  graph = new GraphCore ();
	 private static Scanner scanner = new Scanner(System.in);
	 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 System.out.println("--- Smart City Route Planner ---");
		 System.out.println("Loading initial demo data...");

		 // M4: Initial setup/testing to populate graph and tree
		 avl.insert("Hospital"); graph.addLocation("Hospital");
		 avl.insert("Museum"); graph.addLocation("Museum");
		 avl.insert("Park"); graph.addLocation("Park");
		 avl.insert("Stadium"); graph.addLocation("Stadium");
		 graph.addRoad("Hospital", "Park");
		 graph.addRoad("Museum", "Park");
		 graph.addRoad("Stadium", "Hospital");
		 System.out.println("Demo data loaded: 4 locations and 3 roads.");
		 boolean running = true;
		 while (running) {
		 printMenu();
		 int choice = readInt("Choose an option: "); // M4: Input Validation
		 switch (choice) {
		 case 1: addLocation(); break;
		 case 2: removeLocation(); break;
		 case 3: addRoad(); break;
		 case 4: removeRoad(); break;
		 case 5: displayAllConnections(); break;
		 case 6: displayAllLocations(); break;
		 case 7: findShortestRoute(); break;
		 case 8: dfsTraversalDemo(); break;
		 case 9:
			 
		 System.out.println("Exiting. Goodbye!");
		 running = false;
		 break;
		 default: System.out.println("Invalid choice. Please choose a number from the menu.");
		 }
		 }
		 }
		 // M4: Menu Display
		 private static void printMenu() {
		 System.out.println("\n--- Smart City Menu ---");
		 System.out.println("1. Add a new location");
		 System.out.println("2. Remove a location");
		 System.out.println("3. Add a road between locations");
		 System.out.println("4. Remove a road");
		 System.out.println("5. Display all connections (Graph)");
		 System.out.println("6. Display all locations (AVL Tree - Sorted)");
		 System.out.println("7. Find Shortest Route (BFS)");
		 System.out.println("8. Perform Full Traversal (DFS)");
		 System.out.println("9. Exit");
		 }
		 // --- M4: Integration Logic ---
		 private static void addLocation() {
		 String name = readNonEmptyString("Enter location name: ");
		 if (avl.insert(name)) {
		 graph.addLocation(name);
		 System.out.println("SUCCESS: Location '" + name + "' added (synchronized).");
		 } else {
		 System.out.println("ERROR: Location '" + name + "' already exists.");
		 }
		 }
		 private static void removeLocation() {
		 String name = readNonEmptyString("Enter location name to remove: ");
		 if (avl.delete(name)) {
		 graph.removeLocation(name);
		 System.out.println("SUCCESS: Location '" + name + "' removed (synchronized).");
		 } else {
		 System.out.println("ERROR: Location '" + name + "' does not exist.");
		 }
		 }

		 private static void addRoad() {
			 String from = readNonEmptyString("Enter starting location: ");
			 String to = readNonEmptyString("Enter destination location: ");
			 if (!graph.locationExists(from) || !graph.locationExists(to)) 
			 { System.out.println("ERROR:Locations must exist."); return; }
			if (graph.addRoad(from, to)) { System.out.println("SUCCESS: Road added."); } else {
		System.out.println("ERROR: Road already exists."); }
		 }
		 private static void removeRoad() {
		 String from = readNonEmptyString("Enter starting location: ");
		 String to = readNonEmptyString("Enter destination location: ");
		 if (!graph.locationExists(from) || !graph.locationExists(to)) 
		 { System.out.println("ERROR:Locations must exist."); return; }
		 if (graph.removeRoad(from, to)) { System.out.println("SUCCESS: Road removed."); } else {
		System.out.println("ERROR: No road found."); }
		 }

		 // --- M4: Display and Algorithm Execution ---
		 private static void displayAllConnections() { graph.displayConnections(); }
		 private static void displayAllLocations() {
		 List<String> locations = avl.inorder();
		 System.out.println("\n--- All Locations (Sorted by AVL Tree) ---");
		 if (locations.isEmpty()) { System.out.println("No locations have been added yet."); }
		 else { System.out.println(String.join(", ", locations)); }
		 }
		 private static void findShortestRoute() {
		 String src = readNonEmptyString("Enter starting location: ");
		 String dst = readNonEmptyString("Enter destination location: ");
		 if (!graph.locationExists(src) || !graph.locationExists(dst)) { System.out.println("ERROR:Locations must exist."); return; }
		 List<String> path = graph.bfsShortestRoute(src, dst);
		 if (path.isEmpty()) { System.out.println("No route found."); }
		 else { System.out.println("Shortest path (BFS): " + String.join(" -> ", path)); }
		 }
		 private static void dfsTraversalDemo() {
		 String start = readNonEmptyString("Enter starting location for DFS traversal: ");
		 if (!graph.locationExists(start)) { System.out.println("ERROR: Starting location must exist.");
		return; }
		 List<String> order = graph.dfsTraversal(start);
		 if (order.isEmpty()) { System.out.println("No traversal (start missing or isolated)."); }
		 else { System.out.println("DFS traversal order (using stack): " + String.join(" -> ", order)); }
		 }

		 // --- M4: Input Validation Methods (Requirement 6) ---
		 private static int readInt(String prompt) {
		 while (true) {
		 try {
		 System.out.print(prompt);
		 String line = scanner.nextLine();
		 return Integer.parseInt(line.trim());
		 } catch (NumberFormatException e) {
		 System.out.println("Please enter a valid integer.");
		 }
		 }
		 }
		 private static String readNonEmptyString(String prompt) {
		 while (true) {
		 System.out.print(prompt);
		 String s = scanner.nextLine();
		 if (s != null) s = s.trim();
		 if (s == null || s.isEmpty()) {
		 System.out.println("Input cannot be empty.");
		 continue;
		 }
		 return s;
		 }
		 }
		}
