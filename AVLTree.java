package Smart_City_Route;

public class AVLTree {
private Node root;
    
    private class Node {
        int locationId;
        String locationName;
        Node left, right;
        int height;
        
        Node(int locationId, String locationName) {
            this.locationId = locationId;
            this.locationName = locationName;
            this.height = 1;
        }
    }
    
    // Insert a new location into the AVL tree
    public void insert(int locationId, String locationName) {
        root = insertRecursive(root, locationId, locationName);
    }
    
    private Node insertRecursive(Node node, int locationId, String locationName) {
        if (node == null) {
            return new Node(locationId, locationName);
        }
        
        if (locationId < node.locationId) {
            node.left = insertRecursive(node.left, locationId, locationName);
        } else if (locationId > node.locationId) {
            node.right = insertRecursive(node.right, locationId, locationName);
        } else {
            // Duplicate ID - update name
            node.locationName = locationName;
            return node;
        }
        
        // Update height and balance factor
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        
        // Balance the tree
        return balanceNode(node, locationId);
    }
    
    // Delete a location from the AVL tree
    public void delete(int locationId) {
        root = deleteRecursive(root, locationId);
    }
    
    private Node deleteRecursive(Node node, int locationId) {
        if (node == null) {
            return null;
        }
        
        if (locationId < node.locationId) {
            node.left = deleteRecursive(node.left, locationId);
        } else if (locationId > node.locationId) {
            node.right = deleteRecursive(node.right, locationId);
        } else {
            // Node to be deleted found
            
            // Node with only one child or no child
            if (node.left == null || node.right == null) {
                Node temp = (node.left != null) ? node.left : node.right;
                
                // No child case
                if (temp == null) {
                    node = null;
                } else {
                    // One child case
                    node = temp;
                }
            } else {
                // Node with two children
                Node temp = findMinNode(node.right);
                node.locationId = temp.locationId;
                node.locationName = temp.locationName;
                node.right = deleteRecursive(node.right, temp.locationId);
            }
        }
        
        if (node == null) {
            return null;
        }
        
        // Update height and balance factor
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        
        // Balance the tree
        return balanceNode(node, locationId);
    }
    
    // Balance the AVL tree node
    private Node balanceNode(Node node, int locationId) {
        int balance = getBalance(node);
        
        // Left Left Case
        if (balance > 1 && locationId < node.left.locationId) {
            return rotateRight(node);
        }
        
        // Right Right Case
        if (balance < -1 && locationId > node.right.locationId) {
            return rotateLeft(node);
        }
        
        // Left Right Case
        if (balance > 1 && locationId > node.left.locationId) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        
        // Right Left Case
        if (balance < -1 && locationId < node.right.locationId) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }
        
        return node;
    }
    
    // Right rotation
    private Node rotateRight(Node y) {
        Node x = y.left;
        Node T2 = x.right;
        
        x.right = y;
        y.left = T2;
        
        y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;
        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;
        
        return x;
    }
    
    // Left rotation
    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node T2 = y.left;
        
        y.left = x;
        x.right = T2;
        
        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;
        y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;
        
        return y;
    }
    
    // Utility methods
    private int getHeight(Node node) {
        return (node == null) ? 0 : node.height;
    }
    
    private int getBalance(Node node) {
        return (node == null) ? 0 : getHeight(node.left) - getHeight(node.right);
    }
    
    private Node findMinNode(Node node) {
        Node current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }
    
    // Display all locations using in-order traversal
    public void displayInOrder() {
        if (root == null) {
            System.out.println("No locations in the tree!");
            return;
        }
        
        System.out.println("Locations (Sorted by ID):");
        displayInOrderRecursive(root);
    }
    
    private void displayInOrderRecursive(Node node) {
        if (node != null) {
            displayInOrderRecursive(node.left);
            System.out.println("ID: " + node.locationId + " - " + node.locationName);
            displayInOrderRecursive(node.right);
        }
    }
    
    // Search for a location by ID
    public String search(int locationId) {
        Node result = searchRecursive(root, locationId);
        return (result != null) ? result.locationName : null;
    }
    
    private Node searchRecursive(Node node, int locationId) {
        if (node == null || node.locationId == locationId) {
            return node;
        }
        
        if (locationId < node.locationId) {
            return searchRecursive(node.left, locationId);
        } else {
            return searchRecursive(node.right, locationId);
        }
    }
}
