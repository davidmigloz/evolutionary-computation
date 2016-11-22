package com.davazriel.airtafficcontroller.model;

/**
 * Runaway.
 */
public class Runway {

    private int id;
    private Airport airport;
    private int currentATA;
    private Flight.PlaneType currentPlaneType;

    public Runway(int id, Airport airport) {
        this.id = id;
        this.airport = airport;
        this.currentATA = 0;
    }

    public Runway(Airport airport, int id) {
        this.airport = airport;
        this.id = id;
        this.currentATA = 0;
    }

    public int getId() {
        return id;
    }

    public int getCurrentATA() {
        return currentATA;
    }

    public Flight.PlaneType getCurrentPlaneType() {
        return currentPlaneType;
    }

    public int getNextTimeRunwayAvailable(Flight.PlaneType planeType) {
        return currentPlaneType == null ? currentATA : currentATA + airport.getWaitingTime(currentPlaneType, planeType);
    }

    public void addArrival(Flight flight) {
        int minATA = getNextTimeRunwayAvailable(flight.getPlaneType());
        int ETA = flight.getETA(id);
        // Set new arrival details
        currentATA = minATA > ETA ? minATA : ETA;
        currentPlaneType = flight.getPlaneType();
        flight.setATA(currentATA);
        flight.setAssignedRunway(this);
    }
}
