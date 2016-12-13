package com.davazriel.airtafficcontroller.model;

import java.util.List;
import com.davazriel.airtafficcontroller.model.Flight.PlaneType;

/**
 * Modela una pista del aeropuerto.
 */
@SuppressWarnings("WeakerAccess")
public class Runway {

    private int id;
    private Airport airport;
    private int currentATA;
    private PlaneType currentPlaneType;
    private List<PlaneType> notAllowedPlaneTypes;
    private int violatedRestriction;

    
    public Runway(int id, Airport airport, List<PlaneType> notAllowedPlaneTypes) {
        this.id = id;
        this.airport = airport;
        this.notAllowedPlaneTypes = notAllowedPlaneTypes;
        this.currentATA = 0;
        this.violatedRestriction = 0;
    }

    /**
     * Devuelve el id de la pista.
     *
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
    public PlaneType getCurrentPlaneType() {
        return currentPlaneType;
    }

    /**
     * Devuelve el instante de tiempo en el que la pista quedará disponible para que aterrize
     * un avión del tipo indicado.
     * @param planeType tipo de avión.
     * @return cuando estará libre.
     */
    public int getNextTimeRunwayAvailable(PlaneType planeType) {
        return currentPlaneType == null ? currentATA : currentATA + airport.getWaitingTime(currentPlaneType, planeType);
    }

    /**
     * Añadir llegada a la pista.
     * @param flight vuelo.
     */
    public void addArrival(Flight flight) {
        // Check whether runway restriction is violated
        if(notAllowedPlaneTypes!=null && notAllowedPlaneTypes.contains(flight.getPlaneType())){
            violatedRestriction++;
        }
        int minATA = getNextTimeRunwayAvailable(flight.getPlaneType());
        int ETA = flight.getETA(id);
        // Set new arrival details
        currentATA = minATA > ETA ? minATA : ETA;
        currentPlaneType = flight.getPlaneType();
        flight.setATA(currentATA);
        flight.setAssignedRunway(this);
    }

    /**
     * Devuelve el numero de veces que se ha violado la restriccion de pista.
     * @return El numero de veces.
     */
    public int getViolationsRunwayRestriction(){
        return violatedRestriction;
    }
}
