import java.util.*;

/**
 * Book My Stay - Hotel Booking Management System
 *
 * Demonstrates safe booking cancellation with inventory rollback.
 * Uses LIFO stack to track room IDs for controlled undo of allocations.
 *
 * Author: Student
 * Version: 10.0
 */

// Reservation class representing a confirmed booking
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void displayReservation() {
        System.out.println("Reservation ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room Type: " + roomType);
    }
}

// Inventory Service managing room availability
class InventoryService {
    private Map<String, Integer> availability = new HashMap<>();

    public InventoryService() {
        availability.put("Single", 2);
        availability.put("Double", 1);
        availability.put("Suite", 1);
    }

    // Allocate room (decrement inventory)
    public boolean allocateRoom(String roomType) {
        if (!availability.containsKey(roomType) || availability.get(roomType) <= 0) {
            return false;
        }
        availability.put(roomType, availability.get(roomType) - 1);
        return true;
    }

    // Rollback allocation (increment inventory)
    public void rollbackRoom(String roomType) {
        if (availability.containsKey(roomType)) {
            availability.put(roomType, availability.get(roomType) + 1);
        } else {
            availability.put(roomType, 1); // safeguard for new type
        }
    }

    // Display current inventory
    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String type : availability.keySet()) {
            System.out.println(type + " → " + availability.get(type) + " available");
        }
    }
}

// Booking History maintaining confirmed reservations
class BookingHistory {
    private Map<String, Reservation> reservations = new HashMap<>();

    public void addReservation(Reservation r) {
        reservations.put(r.getReservationId(), r);
    }

    public Reservation getReservation(String reservationId) {
        return reservations.get(reservationId);
    }

    public boolean removeReservation(String reservationId) {
        return reservations.remove(reservationId) != null;
    }

    public void displayAllReservations() {
        System.out.println("\nCurrent Reservations:");
        for (Reservation r : reservations.values()) {
            r.displayReservation();
        }
    }
}

// Cancellation Service
class CancellationService {
    private InventoryService inventoryService;
    private BookingHistory bookingHistory;
    private Stack<String> rollbackStack = new Stack<>();

    public CancellationService(InventoryService inventoryService, BookingHistory bookingHistory) {
        this.inventoryService = inventoryService;
        this.bookingHistory = bookingHistory;
    }

    // Cancel a booking
    public void cancelReservation(String reservationId) {
        Reservation r = bookingHistory.getReservation(reservationId);
        if (r == null) {
            System.out.println("Cancellation failed: Reservation " + reservationId + " not found.");
            return;
        }

        // Rollback inventory
        inventoryService.rollbackRoom(r.getRoomType());
        rollbackStack.push(r.getRoomType()); // record rollback
        bookingHistory.removeReservation(reservationId);

        System.out.println("Reservation " + reservationId + " cancelled successfully.");
    }

    // Undo last rollback (for demonstration)
    public void undoLastCancellation() {
        if (!rollbackStack.isEmpty()) {
            String roomType = rollbackStack.pop();
            inventoryService.allocateRoom(roomType);
            System.out.println("Undo last cancellation: Restored room type " + roomType);
        } else {
            System.out.println("No cancellations to undo.");
        }
    }
}

// Main Application
public class BookMyStayApp {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("Book My Stay - Booking Cancellation & Rollback");
        System.out.println("Version 10.0");
        System.out.println("========================================");

        InventoryService inventoryService = new InventoryService();
        BookingHistory bookingHistory = new BookingHistory();
        CancellationService cancellationService = new CancellationService(inventoryService, bookingHistory);

        // Sample confirmed bookings
        Reservation r1 = new Reservation("R101", "Alice", "Single");
        Reservation r2 = new Reservation("R102", "Bob", "Suite");
        Reservation r3 = new Reservation("R103", "Charlie", "Double");

        // Add to booking history and allocate inventory
        if (inventoryService.allocateRoom(r1.getRoomType())) bookingHistory.addReservation(r1);
        if (inventoryService.allocateRoom(r2.getRoomType())) bookingHistory.addReservation(r2);
        if (inventoryService.allocateRoom(r3.getRoomType())) bookingHistory.addReservation(r3);

        System.out.println("\nInitial State:");
        bookingHistory.displayAllReservations();
        inventoryService.displayInventory();

        // Perform cancellations
        System.out.println("\nCancelling reservation R102 (Bob)...");
        cancellationService.cancelReservation("R102");

        System.out.println("\nCancelling non-existent reservation R999...");
        cancellationService.cancelReservation("R999");

        System.out.println("\nState after cancellations:");
        bookingHistory.displayAllReservations();
        inventoryService.displayInventory();

        // Undo last cancellation
        System.out.println("\nUndo last cancellation:");
        cancellationService.undoLastCancellation();

        System.out.println("\nFinal State:");
        bookingHistory.displayAllReservations();
        inventoryService.displayInventory();
    }
}