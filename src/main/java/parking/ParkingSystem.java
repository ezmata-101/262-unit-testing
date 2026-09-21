package parking;

import java.time.LocalDateTime;
import java.util.List;

public class ParkingSystem {
    private List<Vehicle> vehicles;
    private List<ParkingSlot> parkingSlots;
    private List<Booking> bookings;
    private double PARKING_RATE_PER_HOUR = 10.0;
    private Wallet SYSTEM_WALLET = new Wallet();

    private static ParkingSystem instance = null;

    public static ParkingSystem getInstance() {
        if (instance == null) {
            instance = new ParkingSystem();
        }
        return instance;
    }

    private ParkingSystem() {
        vehicles = new java.util.ArrayList<>();
        parkingSlots = new java.util.ArrayList<>();
        bookings = new java.util.ArrayList<>();
    }

    public List<ParkingSlot> getAvailableParkingSlots(Vehicle vehicle, LocalDateTime startTime, LocalDateTime endTime) {
        List<ParkingSlot> availableSlots = new java.util.ArrayList<>();
        for (ParkingSlot slot : parkingSlots) {
            if (slot.isCompatible(vehicle.getVehicleType(), startTime, endTime)) {
                availableSlots.add(slot);
            }
        }
        return availableSlots;
    }

    public Booking book(Vehicle vehicle, ParkingSlot slot, LocalDateTime startTime, LocalDateTime endTime) {
        if (endTime.isBefore(startTime) || endTime.isEqual(startTime)) {
            throw new IllegalBookingTimeException();
        }

        if (!slot.isCompatible(vehicle.getVehicleType(), startTime, endTime)) {
            throw new IllegalArgumentException("Parking slot is not compatible or not available for the given time.");
        }

        double hours = java.time.Duration.between(startTime, endTime).toHours();
        double amount = hours * PARKING_RATE_PER_HOUR * getVehicleTypeRate(vehicle.getVehicleType()) * parkingSlotTypeMultiplier(slot.getSlotType());

        Booking booking = new Booking(bookings.size() + 1, vehicle, slot, startTime, endTime, amount);
        bookings.add(booking);

        vehicle.getWallet().transferFunds(SYSTEM_WALLET, amount);
        slot.getBookings().add(booking);

        return booking;
    }

    public void completeBooking(Booking booking) {
        booking.completeBooking();
        SYSTEM_WALLET.transferFunds(booking.getParkingSlot().getWallet(), booking.getAmount() * 0.8);
    }

    public void cancelBooking(Booking booking) {
        booking.cancelBooking();
        SYSTEM_WALLET.transferFunds(booking.getVehicle().getWallet(), booking.getAmount() * 0.9);
    }

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
    }

    public void addParkingSlot(ParkingSlot slot) {
        parkingSlots.add(slot);
    }

    private double getVehicleTypeRate(VehicleType type) {
        switch (type) {
            case BICYCLE:
                return 0.2;
            case MOTORCYCLE:
                return 0.5;
            case MICROCAR:
                return 1.5;
            case BUS:
                return 2.0;
            case TRUCK:
                return 3.0;
            default:
                return 1.0;
        }
    }

    private double parkingSlotTypeMultiplier(ParkingSlotType type) {
        switch (type) {
            case COMPACT:
                return 0.8;
            case LARGE:
                return 1.5;
            case HANDICAPPED:
                return 1.2;
            default:
                return 1.0;
        }
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public List<ParkingSlot> getParkingSlots() {
        return parkingSlots;
    }

    public void setParkingSlots(List<ParkingSlot> parkingSlots) {
        this.parkingSlots = parkingSlots;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public double getPARKING_RATE_PER_HOUR() {
        return PARKING_RATE_PER_HOUR;
    }

    public void setPARKING_RATE_PER_HOUR(double PARKING_RATE_PER_HOUR) {
        this.PARKING_RATE_PER_HOUR = PARKING_RATE_PER_HOUR;
    }

    public Wallet getSYSTEM_WALLET() {
        return SYSTEM_WALLET;
    }

    public void setSYSTEM_WALLET(Wallet SYSTEM_WALLET) {
        this.SYSTEM_WALLET = SYSTEM_WALLET;
    }

    public double getBalance() {
        return SYSTEM_WALLET.getBalance();
    }

    public void resetForTesting() {
        vehicles = new java.util.ArrayList<>();
        parkingSlots = new java.util.ArrayList<>();
        bookings = new java.util.ArrayList<>();
        PARKING_RATE_PER_HOUR = 10.0;
        SYSTEM_WALLET = new Wallet();
    }
}

class IllegalBookingTimeException extends RuntimeException {
    public IllegalBookingTimeException() {
        super("End time must be after start time");
    }
}

class IllegalBookingArgumentException extends RuntimeException {
    public IllegalBookingArgumentException() {
        super("Parking slot is not compatible or not available for the given time.");
    }
}

class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException() {
        super("Insufficient balance in wallet to complete the booking.");
    }
}