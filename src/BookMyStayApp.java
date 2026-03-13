import java.util.LinkedList;
import java.util.Queue;

/**
 * Book My Stay - Hotel Booking Management System
 *
 * Demonstrates handling booking requests using a FIFO queue.
 * Requests are stored in arrival order and processed later
 * by the allocation system.
 *
 * @author Student
 * @version 5.0
 */

// Reservation class representing a guest's booking request
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

    public void displayRequest() {
        System.out.println("Guest: " + guestName + " | Requested Room: " + roomType);
    }
}


// Booking Request Queue (FIFO)
class BookingRequestQueue {

    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    // Add booking request to queue
    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
        System.out.println("Booking request received from " + reservation.getGuestName());
    }

    // Display queued requests
    public void displayRequests() {

        System.out.println("\nCurrent Booking Request Queue:");
        System.out.println("--------------------------------");

        for (Reservation r : requestQueue) {
            r.displayRequest();
        }
    }
}


// Main Application
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("====================================");
        System.out.println("Book My Stay - Booking Request Queue");
        System.out.println("Version 5.0");
        System.out.println("====================================");

        // Initialize booking request queue
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Guests submitting booking requests
        Reservation r1 = new Reservation("Alice", "Single");
        Reservation r2 = new Reservation("Bob", "Double");
        Reservation r3 = new Reservation("Charlie", "Suite");
        Reservation r4 = new Reservation("David", "Single");

        // Add requests to queue (arrival order preserved)
        bookingQueue.addRequest(r1);
        bookingQueue.addRequest(r2);
        bookingQueue.addRequest(r3);
        bookingQueue.addRequest(r4);

        // Display queued requests
        bookingQueue.displayRequests();
    }
}