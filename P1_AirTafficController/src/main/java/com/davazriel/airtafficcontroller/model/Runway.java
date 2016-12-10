package com.davazriel.airtafficcontroller.model;

import java.util.List;

/**
 * Modela una pista del aeropuerto.
 */
@SuppressWarnings("WeakerAccess")
public class Runway {

    private int id;
    private Airport airport;
    private int currentATA;
    private Flight.PlaneType currentPlaneType;
    private List<Flight.PlaneType> nonAllowedPlaneTypes;
    private int violatedRestriction;
    
    
    public Runway(int id, Airport airport) {
        this.id = id;
        this.airport = airport;
        this.currentATA = 0;
    }

    /**
     * Devuelve el id de la pista.
     * @return id.
     */
    public int getId() {
        return id;
    }

    /**
     * Devuelve el ATA del avión que está aterrizando.
     * @return ATA.
     */
    public int getCurrentATA() {
        return currentATA;
    }

    /**
     * Devuelve el tipo del avión que está aterrizando en la pista.
     * @return tipo del avión.
     */
    public Flight.PlaneType getCurrentPlaneType() {
        return currentPlaneType;
    }

    /**
     * Devuelve el instante de tiempo en el que la pista quedará disponible para que aterrize
     * un avión del tipo indicado.
     * @param planeType tipo de avión.
     * @return cuando estará libre.
     */
    public int getNextTimeRunwayAvailable(Flight.PlaneType planeType) {
    	if(nonAllowedPlaneTypes.contains(planeType)){
    		violatedRestriction++;
    	}
        return currentPlaneType == null ? currentATA : currentATA + airport.getWaitingTime(currentPlaneType, planeType);
    }

    /**
     * Devuelve el numero de veces que se ha violado la restriccion de pista.
     * @return El numero de veces.
     */
    public int getViolatedRestriction(){
    	return violatedRestriction;
    }
    
    /**
     * Añadir llegada a la pista.
     * @param flight vuelo.
     */
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
