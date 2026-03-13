import java.util.*;
import java.util.concurrent.*;

/**
 * Book My Stay - Concurrent Booking Simulation
 *
 * Demonstrates thread-safe handling of multiple booking requests.
 * Synchronization ensures consistent inventory and prevents double allocation.
 *
 * Author: Student
 * Version: 11.0
 */

// Reservation class
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() { return reservationId; }
    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }

    public void displayReservation() {
        System.out.println("Reservation ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room Type: " + roomType);
    }
}

// Thread-safe Inventory Service
class InventoryService {
    private final Map<String, Integer> availability = new HashMap<>();

    public InventoryService() {
        availability.put("Single", 2);
        availability.put("Double", 1);
        availability.put("Suite", 1);
    }

    // Synchronized allocation
    public synchronized boolean allocateRoom(String roomType) {
        int available = availability.getOrDefault(roomType, 0);
        if (available > 0) {
            availability.put(roomType, available - 1);
            return true;
        }
        return false;
    }

    // Display inventory (synchronized to prevent interleaving)
    public synchronized void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String type : availability.keySet()) {
            System.out.println(type + " → " + availability.get(type) + " available");
        }
    }
}

// Booking History (thread-safe)
class BookingHistory {
    private final List<Reservation> confirmedReservations = Collections.synchronizedList(new ArrayList<>());

    public void addReservation(Reservation r) {
        confirmedReservations.add(r);
    }

    public void displayAllReservations() {
        System.out.println("\nConfirmed Reservations:");
        synchronized(confirmedReservations) {
            for (Reservation r : confirmedReservations) {
                r.displayReservation();
            }
        }
    }
}

// Booking processor runnable
class BookingTask implements Runnable {
    private final Reservation reservation;
    private final InventoryService inventoryService;
    private final BookingHistory bookingHistory;

    public BookingTask(Reservation reservation, InventoryService inventoryService, BookingHistory bookingHistory) {
        this.reservation = reservation;
        this.inventoryService = inventoryService;
        this.bookingHistory = bookingHistory;
    }

    @Override
    public void run() {
        // Attempt allocation
        boolean allocated = inventoryService.allocateRoom(reservation.getRoomType());
        if (allocated) {
            bookingHistory.addReservation(reservation);
            System.out.println("Booking successful: " + reservation.getReservationId() +
                    " for " + reservation.getGuestName());
        } else {
            System.out.println("Booking failed (no availability): " + reservation.getReservationId() +
                    " for " + reservation.getGuestName());
        }
    }
}

// Main Application
public class BookMyStayApp {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("======================================");
        System.out.println("Book My Stay - Concurrent Booking Simulation");
        System.out.println("Version 11.0");
        System.out.println("======================================");

        InventoryService inventoryService = new InventoryService();
        BookingHistory bookingHistory = new BookingHistory();

        // Sample booking requests (multiple guests submitting concurrently)
        List<Reservation> requests = Arrays.asList(
                new Reservation("R201", "Alice", "Single"),
                new Reservation("R202", "Bob", "Single"),
                new Reservation("R203", "Charlie", "Double"),
                new Reservation("R204", "David", "Suite"),
                new Reservation("R205", "Eve", "Single") // should fail if inventory exhausted
        );

        // Executor for concurrent booking
        ExecutorService executor = Executors.newFixedThreadPool(3);

        for (Reservation r : requests) {
            executor.submit(new BookingTask(r, inventoryService, bookingHistory));
        }

        // Shutdown executor and wait for tasks to complete
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        // Display final state
        bookingHistory.displayAllReservations();
        inventoryService.displayInventory();
    }
}