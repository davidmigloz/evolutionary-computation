package com.davazriel.airtafficcontroller.model;

import java.util.Arrays;

/**
 * Modela un vuelo.
 */
@SuppressWarnings("WeakerAccess")
public class Flight {

    private String id;
    private PlaneType type;
    private int[] runwayETAs;
    private int ATA;
    private Runway assignedRunway;
	private Flight contiguousFlight;

    public Flight(String id, PlaneType type, int[] runwayETAs) {
        this(id, type, runwayETAs, null);
    }

    public Flight(String id, PlaneType type, int[] runwayETAs, Flight contiguousFlight) {
        this.id = id;
        this.type = type;
        this.runwayETAs = runwayETAs;
        this.contiguousFlight = contiguousFlight;
    }

    /**
     * Devuelve el id del vuelo.
     *
     * @return id.
     */
    public String getId() {
        return id;
    }

    /**
     * Devuelve el tipo de avión.
     *
     * @return tipo de avión.
     */
    public PlaneType getPlaneType() {
        return type;
    }

    /**
     * Devuelve el tiempo estimado de llegada del avión.
     *
     * @param runwayId id de la pista.
     * @return ETA.
     */
    public int getETA(int runwayId) {
        return runwayETAs[runwayId];
    }

    /**
     * Permite establecer el tiempo actual de llegada.
     *
     * @param ATA ATA.
     */
    public void setATA(int ATA) {
        this.ATA = ATA;
    }

    /**
     * Permite establecer la pista asignada.
     *
     * @param assignedRunway pista.
     */
    public void setAssignedRunway(Runway assignedRunway) {
        this.assignedRunway = assignedRunway;
    }

    /**
     * Devuelve la pista asignada.
     *
     * @return pista asignada.
     */
    public Runway getAssignedRunway() {
        return assignedRunway;
    }

    /**
     * Calcula el retraso que ha tenido el avión.
     * Considerando el mínimo ETA.
     * ATA - min(ETAs).
     *
     * @return retraso.
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public int getDelay() {
        int minETA = Arrays.stream(runwayETAs).min().getAsInt();
        return ATA - minETA;
    }

    /**
     * Comprueba si tiene vuelo contiguo o no.
     *
     * @return si tiene vuelo contiguo.
     */
    public boolean hasContiguousFlight() {
        return contiguousFlight != null;
    }

    /**
     * Comprueba si el avión contiguo asociado ha aterrizado en la misma pista.
     * Es responsabilidad del cliente asegurarse de que tiene contiguousFlight.
     *
     * @return si se ha cumplido la restricción o no.
     */
    public boolean isFlightRestrictionViolated() {
        return this.getAssignedRunway().getId() != contiguousFlight.getAssignedRunway().getId();
    }

    /**
     * Devuelve el retraso sufrido por el vuelo contiguo.
     * Es responsabilidad del cliente asegurarse de que tiene contiguousFlight.
     *
     * @return retraso.
     */
    public int getContiguousFlightDelay() {
        return contiguousFlight.getDelay();
    }

    /**
     * Tipo de avión.
     */
    public enum PlaneType {
        HEAVY(0), BIG(1), SMALL(2);
        private int index;

        PlaneType(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }
    
    /**
     * Setter for contiguousFlight
     */
    public void setContiguousFlight(Flight contiguousFlight){
    	this.contiguousFlight = contiguousFlight;
    }
}
