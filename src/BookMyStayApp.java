import java.util.*;

/**
 * Book My Stay - Hotel Booking Management System
 *
 * Demonstrates room search functionality using read-only access
 * to centralized inventory and room domain objects.
 *
 * @author Student
 * @version 4.0
 */

// Domain Model: Room
class Room {

    private String type;
    private double price;
    private String amenities;

    public Room(String type, double price, String amenities) {
        this.type = type;
        this.price = price;
        this.amenities = amenities;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public String getAmenities() {
        return amenities;
    }

    public void displayDetails(int availableCount) {
        System.out.println("Room Type: " + type);
        System.out.println("Price: ₹" + price);
        System.out.println("Amenities: " + amenities);
        System.out.println("Available Rooms: " + availableCount);
        System.out.println("--------------------------------");
    }
}


// Inventory Service (State Holder)
class RoomInventory {

    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
    }

    public void addRoom(String roomType, int count) {
        inventory.put(roomType, count);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public Set<String> getRoomTypes() {
        return inventory.keySet();
    }
}


// Search Service (Read-Only)
class SearchService {

    public void searchAvailableRooms(RoomInventory inventory, Map<String, Room> roomCatalog) {

        System.out.println("\nAvailable Rooms:");
        System.out.println("========================");

        for (String type : inventory.getRoomTypes()) {

            int available = inventory.getAvailability(type);

            // Validation: show only rooms with availability > 0
            if (available > 0) {

                Room room = roomCatalog.get(type);

                if (room != null) { // defensive programming
                    room.displayDetails(available);
                }
            }
        }
    }
}


// Main Application
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println("Book My Stay - Room Search");
        System.out.println("Version 4.0");
        System.out.println("=================================");

        // Initialize room catalog (domain objects)
        Map<String, Room> roomCatalog = new HashMap<>();

        roomCatalog.put("Single", new Room("Single", 2000, "WiFi, TV"));
        roomCatalog.put("Double", new Room("Double", 3500, "WiFi, TV, AC"));
        roomCatalog.put("Suite", new Room("Suite", 6000, "WiFi, TV, AC, Jacuzzi"));

        // Initialize centralized inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoom("Single", 5);
        inventory.addRoom("Double", 3);
        inventory.addRoom("Suite", 0); // No availability

        // Guest performs room search
        SearchService searchService = new SearchService();
        searchService.searchAvailableRooms(inventory, roomCatalog);
    }
}