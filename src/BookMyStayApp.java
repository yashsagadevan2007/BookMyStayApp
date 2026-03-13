import java.util.*;

/**
 * Book My Stay - Hotel Booking Management System
 *
 * Demonstrates add-on service selection for confirmed reservations.
 * Each reservation can have multiple optional services attached.
 * Core booking and inventory remain unchanged.
 *
 * @author Student
 * @version 7.0
 */

// Represents a single optional service
class Service {
    private String name;
    private double cost;

    public Service(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }

    public void displayService() {
        System.out.println("- " + name + " (₹" + cost + ")");
    }
}

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
}

// Manager for add-on services associated with reservations
class AddOnServiceManager {

    // Map reservation ID → List of selected services
    private Map<String, List<Service>> serviceMap = new HashMap<>();

    // Attach a service to a reservation
    public void addService(Reservation reservation, Service service) {
        serviceMap
                .computeIfAbsent(reservation.getReservationId(), k -> new ArrayList<>())
                .add(service);

        System.out.println("Added service '" + service.getName() +
                "' to reservation " + reservation.getReservationId());
    }

    // Display all services for a reservation
    public void displayServices(Reservation reservation) {
        List<Service> services = serviceMap.get(reservation.getReservationId());

        System.out.println("\nServices for Reservation " + reservation.getReservationId() +
                " (" + reservation.getGuestName() + "):");

        if (services == null || services.isEmpty()) {
            System.out.println("No add-on services selected.");
            return;
        }

        double totalCost = 0;
        for (Service s : services) {
            s.displayService();
            totalCost += s.getCost();
        }

        System.out.println("Total Additional Cost: ₹" + totalCost);
    }
}

// Main Application
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("========================================");
        System.out.println("Book My Stay - Add-On Service Selection");
        System.out.println("Version 7.0");
        System.out.println("========================================");

        // Sample confirmed reservations
        Reservation r1 = new Reservation("R101", "Alice", "Single");
        Reservation r2 = new Reservation("R102", "Bob", "Suite");

        // Available add-on services
        Service breakfast = new Service("Breakfast", 500);
        Service spa = new Service("Spa Session", 1500);
        Service airportPickup = new Service("Airport Pickup", 800);

        // Manager for optional services
        AddOnServiceManager serviceManager = new AddOnServiceManager();

        // Attach services to reservations
        serviceManager.addService(r1, breakfast);
        serviceManager.addService(r1, airportPickup);
        serviceManager.addService(r2, spa);
        serviceManager.addService(r2, breakfast);

        // Display services for each reservation
        serviceManager.displayServices(r1);
        serviceManager.displayServices(r2);
    }
}