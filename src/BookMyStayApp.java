import java.util.*;

/**
 * Book My Stay - Hotel Booking Management System
 *
 * Demonstrates error handling and validation for booking requests.
 * Invalid inputs and inconsistent states are detected and handled.
 *
 * @author Student
 * @version 9.0
 */

// Custom exception for invalid bookings
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Simple Inventory Service to demonstrate validation
class InventoryService {

    private Map<String, Integer> roomAvailability;

    public InventoryService() {
        roomAvailability = new HashMap<>();
        roomAvailability.put("Single", 2);
        roomAvailability.put("Double", 1);
        roomAvailability.put("Suite", 0); // intentionally zero for validation demo
    }

    // Check if room type exists
    public void validateRoomType(String roomType) throws InvalidBookingException {
        if (!roomAvailability.containsKey(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }
    }

    // Check if rooms are available
    public void validateAvailability(String roomType) throws InvalidBookingException {
        if (roomAvailability.get(roomType) <= 0) {
            throw new InvalidBookingException("No rooms available for type: " + roomType);
        }
    }

    // Allocate room (decrement inventory)
    public void allocateRoom(String roomType) throws InvalidBookingException {
        validateRoomType(roomType);
        validateAvailability(roomType);
        int current = roomAvailability.get(roomType);
        roomAvailability.put(roomType, current - 1);
        System.out.println("Room allocated: " + roomType + " | Remaining: " + (current - 1));
    }
}

// Booking Request
class BookingRequest {
    private String guestName;
    private String roomType;

    public BookingRequest(String guestName, String roomType) {
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

// Main Application
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("========================================");
        System.out.println("Book My Stay - Error Handling & Validation");
        System.out.println("Version 9.0");
        System.out.println("========================================");

        InventoryService inventoryService = new InventoryService();

        // Sample booking requests
        List<BookingRequest> requests = Arrays.asList(
                new BookingRequest("Alice", "Single"),
                new BookingRequest("Bob", "Suite"),        // invalid: no rooms available
                new BookingRequest("Charlie", "Penthouse") // invalid: room type doesn't exist
        );

        for (BookingRequest req : requests) {
            try {
                System.out.println("\nProcessing booking for " + req.getGuestName() +
                        " | Room: " + req.getRoomType());
                inventoryService.allocateRoom(req.getRoomType());
                System.out.println("Booking confirmed for " + req.getGuestName());
            } catch (InvalidBookingException e) {
                System.out.println("Booking failed: " + e.getMessage());
            }
        }

        System.out.println("\nAll booking requests processed safely.");
    }
}