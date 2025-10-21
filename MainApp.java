package Smart_City_Route;

import java.util.*;

public class MainApp { 
    private GraphCore cityGraph;
    private AVLTree locationTree;
    private Scanner scanner;
    
    // ANSI color codes for better UI
    private static final String RESET = "\u001B[0m";
    private static final String BLUE = "\u001B[34m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String CYAN = "\u001B[36m";
    private static final String BOLD = "\u001B[1m";
    
    public MainApp() {
        this.cityGraph = new GraphCore();
        this.locationTree = new AVLTree();
        this.scanner = new Scanner(System.in);
        loadSampleData(); // Load sample data for better demonstration
    }
    
    public void start() {
        clearScreen();
        printWelcomeBanner();
        
        while (true) {
            displayMenu();
            int choice = getValidatedInput(1, 11);
            clearScreen();
            processChoice(choice);
            
            if (choice != 11) {
                pauseForUser();
            }
        }
    }
    
    private void printWelcomeBanner() {
        System.out.println(CYAN + "╔════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                            ║");
        System.out.println("║        " + BOLD + "SMART CITY ROUTE PLANNER" + RESET + CYAN + "                       ║");
        System.out.println("║                                                            ║");
        System.out.println("║        Navigate Your City with Confidence!                 ║");
        System.out.println("║                                                            ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝" + RESET);
        System.out.println();
    }
    
    private void displayMenu() {
        System.out.println(BLUE + "┌─────────────────────────────────────────────────────────┐" + RESET);
        System.out.println(BLUE + "│" + BOLD + "              MAIN MENU" + RESET + BLUE + "                                  │" + RESET);
        System.out.println(BLUE + "├─────────────────────────────────────────────────────────┤" + RESET);
        System.out.println(BLUE + "│" + RESET + " " + GREEN + "📍 Location Management:" + RESET);
        System.out.println(BLUE + "│" + RESET + "   1. Add a new location");
        System.out.println(BLUE + "│" + RESET + "   2. Remove a location");
        System.out.println(BLUE + "│" + RESET + "   3. Search for a location");
        System.out.println(BLUE + "│" + RESET);
        System.out.println(BLUE + "│" + RESET + " " + GREEN + "🛣️  Road Management:" + RESET);
        System.out.println(BLUE + "│" + RESET + "   4. Add a road between locations");
        System.out.println(BLUE + "│" + RESET + "   5. Remove a road");
        System.out.println(BLUE + "│" + RESET);
        System.out.println(BLUE + "│" + RESET + " " + GREEN + "📊 View Information:" + RESET);
        System.out.println(BLUE + "│" + RESET + "   6. Display all connections");
        System.out.println(BLUE + "│" + RESET + "   7. Display all locations (sorted)");
        System.out.println(BLUE + "│" + RESET);
        System.out.println(BLUE + "│" + RESET + " " + GREEN + "🗺️  Navigation & Analysis:" + RESET);
        System.out.println(BLUE + "│" + RESET + "   8. Find shortest path");
        System.out.println(BLUE + "│" + RESET + "   9. BFS Traversal");
        System.out.println(BLUE + "│" + RESET + "   10. DFS Traversal");
        System.out.println(BLUE + "│" + RESET);
        System.out.println(BLUE + "│" + RESET + "   11. " + RED + "Exit" + RESET);
        System.out.println(BLUE + "└─────────────────────────────────────────────────────────┘" + RESET);
        System.out.print(YELLOW + "\n💡 Enter your choice (1-11): " + RESET);
    }
    
    private int getValidatedInput(int min, int max) {
        while (true) {
            try {
                int input = scanner.nextInt();
                scanner.nextLine();
                if (input >= min && input <= max) {
                    return input;
                } else {
                    System.out.print(RED + "❌ Please enter a number between " + min + " and " + max + ": " + RESET);
                }
            } catch (InputMismatchException e) {
                System.out.print(RED + "❌ Invalid input. Please enter a number: " + RESET);
                scanner.nextLine();
            }
        }
    }
    
    private void processChoice(int choice) {
        switch (choice) {
            case 1: addLocation(); break;
            case 2: removeLocation(); break;
            case 3: searchLocation(); break;
            case 4: addRoad(); break;
            case 5: removeRoad(); break;
            case 6: displayAllConnections(); break;
            case 7: displayAllLocations(); break;
            case 8: findShortestPath(); break;
            case 9: bfsTraversal(); break;
            case 10: dfsTraversal(); break;
            case 11: exitApplication(); break;
            default: System.out.println(RED + "❌ Invalid choice!" + RESET);
        }
    }
    
    private void addLocation() {
        printSectionHeader("📍 ADD NEW LOCATION");
        
        System.out.print("Enter location ID (number): ");
        int locationId = getValidatedInput(0, Integer.MAX_VALUE);
        
        System.out.print("Enter location name: ");
        String locationName = scanner.nextLine().trim();
        
        if (locationName.isEmpty()) {
            System.out.println(RED + "\n❌ Error: Location name cannot be empty!" + RESET);
            return;
        }
        
        if (cityGraph.addLocation(locationId, locationName)) {
            locationTree.insert(locationId, locationName);
            System.out.println(GREEN + "\n✅ Success! Location '" + locationName + "' (ID: " + locationId + ") added!" + RESET);
        } else {
            System.out.println(RED + "\n❌ Error: Location with ID " + locationId + " already exists!" + RESET);
        }
    }
    
    private void removeLocation() {
        printSectionHeader("🗑️  REMOVE LOCATION");
        
        if (cityGraph.getLocations().isEmpty()) {
            System.out.println(YELLOW + "⚠️  No locations available to remove!" + RESET);
            return;
        }
        
        System.out.println("Available locations:");
        displayLocationList();
        
        System.out.print("\nEnter location ID to remove: ");
        int locationId = getValidatedInput(0, Integer.MAX_VALUE);
        
        String locationName = cityGraph.getLocationName(locationId);
        
        if (cityGraph.removeLocation(locationId)) {
            locationTree.delete(locationId);
            System.out.println(GREEN + "\n✅ Location '" + locationName + "' (ID: " + locationId + ") removed successfully!" + RESET);
        } else {
            System.out.println(RED + "\n❌ Location with ID " + locationId + " not found!" + RESET);
        }
    }
    
    private void searchLocation() {
        printSectionHeader("🔍 SEARCH LOCATION");
        
        System.out.print("Enter location ID to search: ");
        int locationId = getValidatedInput(0, Integer.MAX_VALUE);
        
        String locationName = locationTree.search(locationId);
        
        if (locationName != null) {
            System.out.println(GREEN + "\n✅ Found: " + locationName + " (ID: " + locationId + ")" + RESET);
        } else {
            System.out.println(RED + "\n❌ Location with ID " + locationId + " not found!" + RESET);
        }
    }
    
    private void addRoad() {
        printSectionHeader("🛣️  ADD ROAD");
        
        if (cityGraph.getLocations().size() < 2) {
            System.out.println(YELLOW + "⚠️  Need at least 2 locations to create a road!" + RESET);
            return;
        }
        
        System.out.println("Available locations:");
        displayLocationList();
        
        System.out.print("\nEnter source location ID: ");
        int sourceId = getValidatedInput(0, Integer.MAX_VALUE);
        
        System.out.print("Enter destination location ID: ");
        int destId = getValidatedInput(0, Integer.MAX_VALUE);
        
        if (sourceId == destId) {
            System.out.println(RED + "\n❌ Source and destination cannot be the same!" + RESET);
            return;
        }
        
        System.out.print("Enter road distance (km): ");
        int distance = getValidatedInput(1, Integer.MAX_VALUE);
        
        if (cityGraph.addRoad(sourceId, destId, distance)) {
            String sourceName = cityGraph.getLocationName(sourceId);
            String destName = cityGraph.getLocationName(destId);
            System.out.println(GREEN + "\n✅ Road created: " + sourceName + " ↔ " + destName + " (" + distance + " km)" + RESET);
        } else {
            System.out.println(RED + "\n❌ Failed to add road. Check if both locations exist!" + RESET);
        }
    }
    
    private void removeRoad() {
        printSectionHeader("🚧 REMOVE ROAD");
        
        System.out.print("Enter source location ID: ");
        int sourceId = getValidatedInput(0, Integer.MAX_VALUE);
        
        System.out.print("Enter destination location ID: ");
        int destId = getValidatedInput(0, Integer.MAX_VALUE);
        
        if (cityGraph.removeRoad(sourceId, destId)) {
            String sourceName = cityGraph.getLocationName(sourceId);
            String destName = cityGraph.getLocationName(destId);
            System.out.println(GREEN + "\n✅ Road removed between " + sourceName + " and " + destName + RESET);
        } else {
            System.out.println(RED + "\n❌ Road not found between the specified locations!" + RESET);
        }
    }
    
    private void displayAllConnections() {
        printSectionHeader("📊 ALL CITY CONNECTIONS");
        cityGraph.displayConnections();
    }
    
    private void displayAllLocations() {
        printSectionHeader("📍 ALL LOCATIONS (SORTED BY ID)");
        locationTree.displayInOrder();
    }
    
    private void findShortestPath() {
        printSectionHeader("🗺️  FIND SHORTEST PATH");
        
        if (cityGraph.getLocations().size() < 2) {
            System.out.println(YELLOW + "⚠️  Need at least 2 locations to find a path!" + RESET);
            return;
        }
        
        System.out.println("Available locations:");
        displayLocationList();
        
        System.out.print("\nEnter start location ID: ");
        int startId = getValidatedInput(0, Integer.MAX_VALUE);
        
        System.out.print("Enter destination location ID: ");
        int endId = getValidatedInput(0, Integer.MAX_VALUE);
        
        System.out.println();
        GraphAlgorithms.findShortestPath(cityGraph, startId, endId);
    }
    
    private void bfsTraversal() {
        printSectionHeader("🔄 BFS TRAVERSAL");
        
        System.out.println("Available locations:");
        displayLocationList();
        
        System.out.print("\nEnter starting location ID: ");
        int startId = getValidatedInput(0, Integer.MAX_VALUE);
        
        System.out.println();
        GraphAlgorithms.bfsTraversal(cityGraph, startId);
    }
    
    private void dfsTraversal() {
        printSectionHeader("🔄 DFS TRAVERSAL");
        
        System.out.println("Available locations:");
        displayLocationList();
        
        System.out.print("\nEnter starting location ID: ");
        int startId = getValidatedInput(0, Integer.MAX_VALUE);
        
        System.out.println();
        GraphAlgorithms.dfsTraversal(cityGraph, startId);
    }
    
    private void displayLocationList() {
        Map<Integer, String> locations = cityGraph.getLocations();
        if (locations.isEmpty()) {
            System.out.println(YELLOW + "  No locations available" + RESET);
            return;
        }
        
        List<Map.Entry<Integer, String>> sortedLocations = new ArrayList<>(locations.entrySet());
        sortedLocations.sort(Map.Entry.comparingByKey());
        
        for (Map.Entry<Integer, String> entry : sortedLocations) {
            System.out.println("  • " + entry.getValue() + " (ID: " + entry.getKey() + ")");
        }
    }
    
    private void printSectionHeader(String title) {
        System.out.println(CYAN + "═══════════════════════════════════════════════════════════" + RESET);
        System.out.println(CYAN + "  " + BOLD + title + RESET);
        System.out.println(CYAN + "═══════════════════════════════════════════════════════════" + RESET);
        System.out.println();
    }
    
    private void pauseForUser() {
        System.out.println(YELLOW + "\n⏸️  Press Enter to continue..." + RESET);
        scanner.nextLine();
        clearScreen();
    }
    
    private void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // If clearing fails, just print new lines
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
    
    private void loadSampleData() {
        // Add sample locations
        cityGraph.addLocation(1, "City Hall");
        locationTree.insert(1, "City Hall");
        
        cityGraph.addLocation(2, "Central Park");
        locationTree.insert(2, "Central Park");
        
        cityGraph.addLocation(3, "Shopping Mall");
        locationTree.insert(3, "Shopping Mall");
        
        cityGraph.addLocation(4, "Airport");
        locationTree.insert(4, "Airport");
        
        cityGraph.addLocation(5, "Train Station");
        locationTree.insert(5, "Train Station");
        
        // Add sample roads
        cityGraph.addRoad(1, 2, 5);
        cityGraph.addRoad(1, 3, 10);
        cityGraph.addRoad(2, 3, 3);
        cityGraph.addRoad(2, 4, 15);
        cityGraph.addRoad(3, 5, 8);
        cityGraph.addRoad(4, 5, 12);
    }
    
    private void exitApplication() {
        System.out.println(CYAN + "╔════════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(CYAN + "║                                                            ║" + RESET);
        System.out.println(CYAN + "║    " + GREEN + "Thank you for using Smart City Route Planner!" + CYAN + "      ║" + RESET);
        System.out.println(CYAN + "║                                                            ║" + RESET);
        System.out.println(CYAN + "║    " + YELLOW + "Safe travels! 🚗" + CYAN + "                                     ║" + RESET);
        System.out.println(CYAN + "║                                                            ║" + RESET);
        System.out.println(CYAN + "╚════════════════════════════════════════════════════════════╝" + RESET);
        scanner.close();
        System.exit(0);
    }
    
    public static void main(String[] args) {
        MainApp app = new MainApp();
        app.start();
    }
}
