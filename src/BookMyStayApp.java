import java.util.*;

/**
 * Book My Stay - Hotel Booking Management System
 *
 * Demonstrates booking history tracking and reporting.
 * Confirmed reservations are stored in chronological order
 * and reports can be generated for administrative review.
 *
 * @author Student
 * @version 8.0
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

// Booking History (stores confirmed reservations)
class BookingHistory {

    private List<Reservation> history = new ArrayList<>();

    // Add reservation to history
    public void addReservation(Reservation reservation) {
        history.add(reservation);
        System.out.println("Reservation " + reservation.getReservationId() + " added to history.");
    }

    // Retrieve all reservations
    public List<Reservation> getAllReservations() {
        return Collections.unmodifiableList(history); // read-only
    }
}

// Booking Report Service (generates summaries/reports)
class BookingReportService {

    private BookingHistory bookingHistory;

    public BookingReportService(BookingHistory bookingHistory) {
        this.bookingHistory = bookingHistory;
    }

    // Display all reservations
    public void displayAllReservations() {
        System.out.println("\nBooking History Report:");
        System.out.println("----------------------");
        for (Reservation r : bookingHistory.getAllReservations()) {
            r.displayReservation();
        }
    }

    // Generate summary report by room type
    public void displaySummaryByRoomType() {
        Map<String, Integer> summary = new HashMap<>();

        for (Reservation r : bookingHistory.getAllReservations()) {
            summary.put(r.getRoomType(), summary.getOrDefault(r.getRoomType(), 0) + 1);
        }

        System.out.println("\nBooking Summary by Room Type:");
        System.out.println("----------------------------");
        for (String type : summary.keySet()) {
            System.out.println(type + " → " + summary.get(type) + " bookings");
        }
    }
}

// Main Application
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("========================================");
        System.out.println("Book My Stay - Booking History & Report");
        System.out.println("Version 8.0");
        System.out.println("========================================");

        // Initialize booking history
        BookingHistory history = new BookingHistory();

        // Sample confirmed reservations
        Reservation r1 = new Reservation("R101", "Alice", "Single");
        Reservation r2 = new Reservation("R102", "Bob", "Suite");
        Reservation r3 = new Reservation("R103", "Charlie", "Double");
        Reservation r4 = new Reservation("R104", "David", "Single");

        // Add reservations to history
        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);
        history.addReservation(r4);

        // Generate reports
        BookingReportService reportService = new BookingReportService(history);

        // Display detailed reservation history
        reportService.displayAllReservations();

        // Display summary by room type
        reportService.displaySummaryByRoomType();
    }
}