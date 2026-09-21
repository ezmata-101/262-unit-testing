package parking;

import java.time.LocalDateTime;
import java.util.List;

public class ParkingSlot {
    private String slotId;
    private ParkingSlotType slotType;
    private boolean isActive;
    private Wallet wallet;
    private List<Booking> bookings;

    public ParkingSlot(String slotId, ParkingSlotType slotType) {
        this.slotId = slotId;
        this.slotType = slotType;
        this.isActive = true;
        this.wallet = new Wallet();
        this.bookings = new java.util.ArrayList<>();
    }

    public boolean isCompatible(VehicleType type, LocalDateTime startTime, LocalDateTime endTime) {
        if (!isActive) {
            return false;
        }
        switch (type) {
            case MOTORCYCLE:
                if (slotType == ParkingSlotType.COMPACT || slotType == ParkingSlotType.REGULAR || slotType == ParkingSlotType.LARGE) {
                    return isAvailable(startTime, endTime);
                }
                break;
            case CAR:
                if (slotType == ParkingSlotType.REGULAR || slotType == ParkingSlotType.LARGE) {
                    return isAvailable(startTime, endTime);
                }
                break;
            case BUS:
                if (slotType == ParkingSlotType.LARGE) {
                    return isAvailable(startTime, endTime);
                }
                break;
            case BICYCLE:
                if (slotType == ParkingSlotType.COMPACT || slotType == ParkingSlotType.REGULAR || slotType == ParkingSlotType.LARGE || slotType == ParkingSlotType.HANDICAPPED) {
                    return isAvailable(startTime, endTime);
                }
                break;
            case MICROCAR:
                if (slotType == ParkingSlotType.COMPACT || slotType == ParkingSlotType.REGULAR) {
                    return isAvailable(startTime, endTime);
                }
            default:
                return false;
        }
        return false;
    }

    public boolean isAvailable(LocalDateTime startTime, LocalDateTime endTime) {
        for (Booking booking : bookings) {
            if (booking.getEndTime().isAfter(startTime) && booking.getStartTime().isBefore(endTime)) {
                return false;
            }
        }
        return true;
    }

    public String getSlotId() {
        return slotId;
    }

    public ParkingSlotType getSlotType() {
        return slotType;
    }

    public boolean isActive() {
        return isActive;
    }

    public void activate() {
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public double getBalance() {
        return wallet.getBalance();
    }
}