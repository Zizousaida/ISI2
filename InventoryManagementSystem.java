import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

class InventoryItem implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String code;
    private int quantity;
    private double price;

    public InventoryItem(String name, String code, int quantity, double price) {
        this.name = name;
        this.code = code;
        this.quantity = quantity;
        this.price = price;
    }

    public String getCode() {
        return code;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Item: " + name + 
               ", Code: " + code + 
               ", Quantity: " + quantity + 
               ", Price: $" + String.format("%.2f", price);
    }
}

public class InventoryManagementSystem {
    private static ArrayList<InventoryItem> inventory = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            try {
                displayMenu();
                int choice = getIntInput("Enter your choice: ");
                
                switch (choice) {
                    case 1:
                        addInventoryItem();
                        break;
                    case 2:
                        deleteInventoryItem();
                        break;
                    case 3:
                        updateItemQuantity();
                        break;
                    case 4:
                        displayAllItems();
                        break;
                    case 5:
                        saveInventoryToFile();
                        break;
                    case 6:
                        loadInventoryFromFile();
                        break;
                    case 7:
                        System.out.println("Exiting the program...");
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
            
            // Add pause before redisplaying menu
            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("\n===== INVENTORY MANAGEMENT SYSTEM =====");
        System.out.println("1. Add a new inventory item");
        System.out.println("2. Delete an inventory item");
        System.out.println("3. Update item quantity");
        System.out.println("4. Display all inventory items");
        System.out.println("5. Save inventory to file");
        System.out.println("6. Load inventory from file");
        System.out.println("7. Exit");
    }

    private static void addInventoryItem() {
        try {
            System.out.println("\n--- Add New Inventory Item ---");
            String name = getStringInput("Enter item name: ");
            
            String code;
            boolean codeExists;
            do {
                code = getStringInput("Enter item code: ");
                codeExists = isCodeExists(code);
                if (codeExists) {
                    System.out.println("An item with this code already exists. Please use a different code.");
                }
            } while (codeExists);
            
            int quantity = getIntInput("Enter quantity: ");
            while (quantity < 0) {
                System.out.println("Quantity cannot be negative.");
                quantity = getIntInput("Enter quantity: ");
            }
            
            double price = getDoubleInput("Enter price: $");
            while (price < 0) {
                System.out.println("Price cannot be negative.");
                price = getDoubleInput("Enter price: $");
            }
            
            inventory.add(new InventoryItem(name, code, quantity, price));
            System.out.println("Inventory item added successfully!");
        } catch (Exception e) {
            System.out.println("Error adding item: " + e.getMessage());
        }
    }

    private static boolean isCodeExists(String code) {
        for (InventoryItem item : inventory) {
            if (item.getCode().equals(code)) {
                return true;
            }
        }
        return false;
    }

    private static void deleteInventoryItem() {
        if (inventory.isEmpty()) {
            System.out.println("No items to delete.");
            return;
        }
        
        System.out.println("\n--- Delete Inventory Item ---");
        String code = getStringInput("Enter item code to delete: ");
        
        boolean found = false;
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).getCode().equals(code)) {
                inventory.remove(i);
                System.out.println("Item deleted successfully!");
                found = true;
                break;
            }
        }
        
        if (!found) {
            System.out.println("No item found with that code.");
        }
    }

    private static void updateItemQuantity() {
        if (inventory.isEmpty()) {
            System.out.println("No items to update.");
            return;
        }
        
        System.out.println("\n--- Update Item Quantity ---");
        String code = getStringInput("Enter item code to update: ");
        
        boolean found = false;
        for (InventoryItem item : inventory) {
            if (item.getCode().equals(code)) {
                System.out.println("Current item: " + item);
                int newQuantity = getIntInput("Enter new quantity: ");
                while (newQuantity < 0) {
                    System.out.println("Quantity cannot be negative.");
                    newQuantity = getIntInput("Enter new quantity: ");
                }
                
                item.setQuantity(newQuantity);
                System.out.println("Quantity updated successfully!");
                found = true;
                break;
            }
        }
        
        if (!found) {
            System.out.println("No item found with that code.");
        }
    }

    private static void displayAllItems() {
        if (inventory.isEmpty()) {
            System.out.println("No items to display.");
            return;
        }
        
        System.out.println("\n--- All Inventory Items ---");
        for (int i = 0; i < inventory.size(); i++) {
            System.out.println((i + 1) + ". " + inventory.get(i));
        }
    }

    private static void saveInventoryToFile() {
        try {
            String filename = getStringInput("Enter filename to save: ");
            if (!filename.endsWith(".dat")) {
                filename += ".dat";
            }
            
            FileOutputStream fileOut = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(inventory);
            out.close();
            fileOut.close();
            System.out.println("Inventory saved successfully to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving to file: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private static void loadInventoryFromFile() {
        try {
            String filename = getStringInput("Enter filename to load: ");
            if (!filename.endsWith(".dat")) {
                filename += ".dat";
            }
            
            FileInputStream fileIn = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            inventory = (ArrayList<InventoryItem>) in.readObject();
            in.close();
            fileIn.close();
            System.out.println("Inventory loaded successfully from " + filename);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + e.getMessage());
        }
    }

    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine();
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid decimal number.");
            }
        }
    }
}
