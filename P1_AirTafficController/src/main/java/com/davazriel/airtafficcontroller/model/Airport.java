package com.davazriel.airtafficcontroller.model;

public class Airport {

    private int[][] waitingTimes;
    private Runway[] runways;

    public Airport(int numRunways, int[][] waitingTimes) {
        // Create runways
        runways = new Runway[numRunways];
        for (int i = 0; i < numRunways; i++) {
            runways[i] = new Runway();
         }
        // Get waiting times
        this.waitingTimes = waitingTimes;
    }

    public void scheduleFlight(Flight flight) {
        Runway freeRunway = findFreeRunway(flight.getPlaneType());
    }

    private Runway findFreeRunway(Flight.PlaneType type) {
        return null;
    }

    public int getWaitingTime(Flight.PlaneType before, Flight.PlaneType after) {
        return waitingTimes[before.getIndex()][after.getIndex()];
    }
}
