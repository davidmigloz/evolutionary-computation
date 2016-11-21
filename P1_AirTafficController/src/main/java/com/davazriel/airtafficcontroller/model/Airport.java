package com.davazriel.airtafficcontroller.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Airport.
 */
public class Airport {

    private int[][] waitingTimes;
    private Runway[] runways;
    private List<Flight> flights;

    public Airport(int numRunways, int[][] waitingTimes) {
        flights = new ArrayList<>();
        // Create runways
        runways = new Runway[numRunways];
        for (int i = 0; i < numRunways; i++) {
            runways[i] = new Runway(i, this);
        }
        // Get waiting times
        this.waitingTimes = waitingTimes;
    }

    public void scheduleFlight(Flight flight) {
        // Find runway with lowest time for landing
        Runway runway = findBestRunway(flight.getPlaneType());
        // Schedule flight in that runway
        runway.addArrival(flight);
        // Save flight
        flights.add(flight);
    }

    private Runway findBestRunway(Flight.PlaneType planeType) {
        Runway bestRunway = null;
        int bestTime = Integer.MAX_VALUE;
        // Get runway with the best time
        for (Runway runway : runways) {
            int nextTimeAvailable = runway.getNextTimeRunwayAvailable(planeType);
            if (nextTimeAvailable < bestTime) {
                bestRunway = runway;
                bestTime = nextTimeAvailable;
            }
        }
        return bestRunway;
    }

    public int getWaitingTime(Flight.PlaneType before, Flight.PlaneType after) {
        return waitingTimes[before.getIndex()][after.getIndex()];
    }

    public int getAccumulatedDelay() {
        int total = 0;
        for (Flight flight : flights) {
            total += flight.getDelay();
        }
        return total;
    }
}
