import java.io.*;
import java.util.*;

/**
 * Book My Stay - Data Persistence & System Recovery
 *
 * Demonstrates saving and loading system state (inventory & booking history)
 * to and from a file, enabling recovery after application restart.
 *
 * Author: Student
 * Version: 12.0
 */

// Reservation class must implement Serializable
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;
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

// Inventory Service (Serializable)
class InventoryService implements Serializable {
    private static final long serialVersionUID = 1L;
    private Map<String, Integer> availability = new HashMap<>();

    public InventoryService() {
        availability.put("Single", 2);
        availability.put("Double", 1);
        availability.put("Suite", 1);
    }

    public boolean allocateRoom(String roomType) {
        int available = availability.getOrDefault(roomType, 0);
        if (available > 0) {
            availability.put(roomType, available - 1);
            return true;
        }
        return false;
    }

    public void releaseRoom(String roomType) {
        availability.put(roomType, availability.getOrDefault(roomType, 0) + 1);
    }

    public void displayInventory() {
        System.out.println("\nInventory:");
        for (String type : availability.keySet()) {
            System.out.println(type + " → " + availability.get(type) + " available");
        }
    }

    public Map<String, Integer> getAvailability() {
        return availability;
    }

    public void setAvailability(Map<String, Integer> availability) {
        this.availability = availability;
    }
}

// Booking History (Serializable)
class BookingHistory implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Reservation> confirmedReservations = new ArrayList<>();

    public void addReservation(Reservation r) {
        confirmedReservations.add(r);
    }

    public void displayAllReservations() {
        System.out.println("\nConfirmed Reservations:");
        for (Reservation r : confirmedReservations) {
            r.displayReservation();
        }
    }

    public List<Reservation> getConfirmedReservations() {
        return confirmedReservations;
    }

    public void setConfirmedReservations(List<Reservation> reservations) {
        this.confirmedReservations = reservations;
    }
}

// Persistence Service
class PersistenceService {
    private static final String FILENAME = "booking_data.ser";

    // Save state to file
    public void save(InventoryService inventory, BookingHistory history) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILENAME))) {
            oos.writeObject(inventory);
            oos.writeObject(history);
            System.out.println("\nSystem state saved to file.");
        } catch (IOException e) {
            System.err.println("Error saving system state: " + e.getMessage());
        }
    }

    // Load state from file
    public void load(InventoryService inventory, BookingHistory history) {
        File file = new File(FILENAME);
        if (!file.exists()) {
            System.out.println("No previous state found. Starting fresh.");
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILENAME))) {
            InventoryService savedInventory = (InventoryService) ois.readObject();
            BookingHistory savedHistory = (BookingHistory) ois.readObject();
            inventory.setAvailability(savedInventory.getAvailability());
            history.setConfirmedReservations(savedHistory.getConfirmedReservations());
            System.out.println("\nSystem state restored from file.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading system state: " + e.getMessage());
        }
    }
}

// Main Application
public class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("======================================");
        System.out.println("Book My Stay - Data Persistence & Recovery");
        System.out.println("Version 12.0");
        System.out.println("======================================");

        InventoryService inventoryService = new InventoryService();
        BookingHistory bookingHistory = new BookingHistory();
        PersistenceService persistenceService = new PersistenceService();

        // Load previous state if exists
        persistenceService.load(inventoryService, bookingHistory);

        // Sample bookings
        Reservation r1 = new Reservation("R301", "Alice", "Single");
        Reservation r2 = new Reservation("R302", "Bob", "Suite");

        // Process bookings
        if (inventoryService.allocateRoom(r1.getRoomType())) bookingHistory.addReservation(r1);
        if (inventoryService.allocateRoom(r2.getRoomType())) bookingHistory.addReservation(r2);

        // Display current state
        bookingHistory.displayAllReservations();
        inventoryService.displayInventory();

        // Save state for next run
        persistenceService.save(inventoryService, bookingHistory);
    }
}