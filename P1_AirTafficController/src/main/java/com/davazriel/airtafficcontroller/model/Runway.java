package com.davazriel.airtafficcontroller.model;

/**
 * Class.
 *
 * @author davidmigloz
 * @since 21/11/2016
 */
public class Runway {

    private Airport airport;
    private int currentATA;
    private Flight.PlaneType currentPlaneType;

    public Runway() {
        currentATA = 0;
    }

    public int getCurrentATA() {
        return currentATA;
    }

    public Flight.PlaneType getCurrentPlaneType() {
        return currentPlaneType;
    }


    public int getNextTimeRunwayAvailable(Flight.PlaneType planeType) {
        return planeType == null ? currentATA : currentATA + airport.getWaitingTime(currentPlaneType, planeType);
    }

    public void addArrival(int ETA, Flight.PlaneType planeType) {
        int minATA = getNextTimeRunwayAvailable(planeType);
        currentATA = minATA > ETA ? minATA : ETA;
        currentPlaneType = planeType;
    }
}
