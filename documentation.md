# Parking Slot Booking (With Intentional Defects)

A minimal Java model of a parking-slot booking system intended for **Software Testing & QA** assignments (JUnit).

> **Student rule:** Do **not** modify code. Capture behavior with tests, document assumptions, and suggest fixes.

---

## System Overview

* **Vehicles** (each with a **Wallet**) book **ParkingSlots** through a **ParkingSystem**.
* A **Booking** spans a start–end time window, computes a fee, and moves money between wallets.

---

## Domain Model (informal)

```
Vehicle ──has──> Wallet
ParkingSlot ──has──> Wallet
ParkingSlot ──contains──> [Booking*]
Booking ──references──> Vehicle, ParkingSlot
ParkingSystem ──manages──> [Vehicle*], [ParkingSlot*], [Booking*], System Wallet
```

---

## Business Rules

### Booking lifecycle

1. **book(vehicle, slot, start, end)**

    * `end` must be strictly after `start` (else `IllegalBookingTimeException`).
    * Slot must be active, compatible, and available.
    * Price is computed and a booking is created with status `ACTIVE` and added to the system and slot lists.
    * **100% of the price is then transferred from vehicle → system**.

2. **completeBooking(booking)**

    * Status → `COMPLETED`.
    * **80%** of the fare moves **system → slot**; system retains **20%**.

3. **cancelBooking(booking)**

    * Status → `CANCELLED`.
    * **90%** refund **system → vehicle**; system keeps **10%**.

> The system wallet acts like **escrow**: full amount at booking, settle on complete/cancel.

---

### Pricing model

* Base: `PARKING_RATE_PER_HOUR = 10.0`
* `price = hours * base * vehicleTypeRate * slotTypeMultiplier`

**Vehicle type rates**
`BICYCLE=0.2`, `MOTORCYCLE=0.5`, `MICROCAR=1.5`, `BUS=2.0`, `TRUCK=3.0`, default `1.0`

**Slot type multipliers**
`COMPACT=0.8`, `REGULAR=1.0`, `LARGE=1.5`, `HANDICAPPED=1.2`

---

### Availability

A slot is **available** if **no stored booking** overlaps the requested window:
`existing.end > requested.start` **AND** `existing.start < requested.end` → blocks.

---

### Compatibility matrix

| VehicleType    | Allowed `ParkingSlotType`            |
| -------------- | ------------------------------------ |
| **MOTORCYCLE** | COMPACT, REGULAR, LARGE              |
| **CAR**        | REGULAR, LARGE                       |
| **BUS**        | LARGE                                |
| **BICYCLE**    | COMPACT, REGULAR, LARGE, HANDICAPPED |
| **MICROCAR**   | COMPACT, REGULAR                     |

---

## API reference (concise)

### Enums

* `VehicleType`: `CAR`, `MOTORCYCLE`, `TRUCK`, `BICYCLE`, `MICROCAR`, `BUS`
* `ParkingSlotType`: `COMPACT`, `REGULAR`, `LARGE`, `HANDICAPPED`
* `BookingStatus`: `ACTIVE`, `COMPLETED`, `CANCELLED`

### Booking

* `Booking(...)` — create a new **ACTIVE** booking.
* `void completeBooking()` — mark booking **COMPLETED**.
* `void cancelBooking()` — mark booking **CANCELLED**.

### ParkingSlot

* `ParkingSlot(...)` — create an active slot with its own wallet.
* `boolean isCompatible(VehicleType, start, end)` — check type rules **and** availability window.
* `boolean isAvailable(start, end)` — reject time-window overlaps.
* `void activate()` — enable the slot.
* `void deactivate()` — disable the slot.

### ParkingSystem

* `List<ParkingSlot> getAvailableParkingSlots(Vehicle, start, end)` — find compatible, free slots.
* `Booking book(Vehicle, ParkingSlot, start, end)` — validate, price, create and store the booking, then **charge vehicle → system**.
* `void completeBooking(Booking)` — settle **80% system → slot**, mark completed.
* `void cancelBooking(Booking)` — **refund 90% system → vehicle**, mark cancelled.
* `void addVehicle(Vehicle)` — register a vehicle in the singleton system.
* `void addParkingSlot(ParkingSlot)` — register a slot in the singleton system.
* `static ParkingSystem getInstance()` — return the lazily created singleton.
* `List<Vehicle> getVehicles()`, `List<ParkingSlot> getParkingSlots()`, `List<Booking> getBookings()` — expose the backing collections.
* `void setVehicles(...)`, `void setParkingSlots(...)`, `void setBookings(...)` — replace the backing collections.
* `double getPARKING_RATE_PER_HOUR()` / `setPARKING_RATE_PER_HOUR(...)` — read or replace the hourly base rate.
* `Wallet getSYSTEM_WALLET()` / `setSYSTEM_WALLET(...)`, `double getBalance()` — access the system wallet or its balance.
* `void resetForTesting()` — clear registered entities and bookings, restore the `10.0` hourly rate, and replace the system wallet.

### Wallet

* `Wallet()` — start with zero balance.
* `Wallet(balance)` — start with initial balance.
* `void addFunds(amount)` — add positive amount (else throws).
* `void deductFunds(amount)` — subtract if sufficient (else throws).
* `void transferFunds(Wallet, amount)` — move funds with validation.

### Vehicle

* `Vehicle(int, VehicleType, Wallet)` — create a vehicle using the supplied wallet.
* `Vehicle(int, VehicleType, double)` — create a vehicle with a wallet initialized to the supplied balance.
* `int getVehicleId()`, `VehicleType getVehicleType()`, `Wallet getWallet()`, `double getBalance()` — access vehicle identity, type, wallet, and balance.

## Exceptions

* `IllegalBookingTimeException` — thrown when the booking end is equal to or before its start.
* `IllegalArgumentException` — thrown when the requested slot is inactive, incompatible, or unavailable.
* `InvalidAmountException` — thrown for non-positive wallet operations.
* `InsufficientFundsException` — thrown when a wallet deduction or transfer exceeds its balance.

These exception classes are package-private implementation classes, although their runtime types are observable by callers in tests.

---