import java.util.*;

/**
 * Book My Stay - Hotel Booking Management System
 *
 * Demonstrates reservation confirmation and room allocation
 * while preventing double booking.
 *
 * @author Student
 * @version 6.0
 */

// Reservation class representing booking request
class Reservation {

    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}


// Inventory Service (manages room availability)
class InventoryService {

    private Map<String, Integer> inventory = new HashMap<>();

    public void addRoomType(String type, int count) {
        inventory.put(type, count);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public void decrementRoom(String type) {
        int count = inventory.get(type);
        inventory.put(type, count - 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + " : " + inventory.get(type));
        }
    }
}


// Booking Service (processes queue and allocates rooms)
class BookingService {

    private Queue<Reservation> requestQueue;
    private InventoryService inventoryService;

    // Set to enforce unique room IDs
    private Set<String> allocatedRoomIds = new HashSet<>();

    // Map room types to assigned room IDs
    private Map<String, Set<String>> roomAllocationMap = new HashMap<>();

    private int roomCounter = 1;

    public BookingService(Queue<Reservation> requestQueue, InventoryService inventoryService) {
        this.requestQueue = requestQueue;
        this.inventoryService = inventoryService;
    }

    // Generate unique room ID
    private String generateRoomId(String roomType) {
        return roomType.substring(0, 2).toUpperCase() + roomCounter++;
    }

    // Process booking requests
    public void processBookings() {

        while (!requestQueue.isEmpty()) {

            Reservation reservation = requestQueue.poll();
            String roomType = reservation.getRoomType();

            System.out.println("\nProcessing booking for: " + reservation.getGuestName());

            if (inventoryService.getAvailability(roomType) > 0) {

                String roomId = generateRoomId(roomType);

                // Ensure uniqueness using Set
                if (!allocatedRoomIds.contains(roomId)) {

                    allocatedRoomIds.add(roomId);

                    roomAllocationMap
                            .computeIfAbsent(roomType, k -> new HashSet<>())
                            .add(roomId);

                    // Update inventory immediately
                    inventoryService.decrementRoom(roomType);

                    System.out.println("Reservation Confirmed!");
                    System.out.println("Guest: " + reservation.getGuestName());
                    System.out.println("Room Type: " + roomType);
                    System.out.println("Assigned Room ID: " + roomId);
                }

            } else {
                System.out.println("Booking Failed: No rooms available for " + roomType);
            }
        }
    }

    // Display final room allocations
    public void displayAllocations() {

        System.out.println("\nFinal Room Allocations:");
        for (String type : roomAllocationMap.keySet()) {
            System.out.println(type + " → " + roomAllocationMap.get(type));
        }
    }
}


// Main Application
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("Book My Stay - Room Allocation System");
        System.out.println("Version 6.0");
        System.out.println("======================================");

        // Initialize inventory
        InventoryService inventory = new InventoryService();
        inventory.addRoomType("Single", 2);
        inventory.addRoomType("Double", 1);
        inventory.addRoomType("Suite", 1);

        // Booking request queue
        Queue<Reservation> requestQueue = new LinkedList<>();

        requestQueue.add(new Reservation("Alice", "Single"));
        requestQueue.add(new Reservation("Bob", "Suite"));
        requestQueue.add(new Reservation("Charlie", "Single"));
        requestQueue.add(new Reservation("David", "Double"));
        requestQueue.add(new Reservation("Eva", "Suite")); // should fail

        // Process bookings
        BookingService bookingService = new BookingService(requestQueue, inventory);
        bookingService.processBookings();

        // Display final allocation
        bookingService.displayAllocations();

        // Display updated inventory
        inventory.displayInventory();
    }
}