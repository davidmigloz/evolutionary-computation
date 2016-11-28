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

    public Flight(String id, PlaneType type, int[] runwayETAs) {
        this.id = id;
        this.type = type;
        this.runwayETAs = runwayETAs;
    }

    /**
     * Devuelve el id del vuelo.
     * @return id.
     */
    public String getId() {
        return id;
    }

    /**
     * Devuelve el tipo de avión.
     * @return tipo de avión.
     */
    public PlaneType getPlaneType() {
        return type;
    }

    /**
     * Devuelve el tiempo estimado de llegada del avión.
     * @param runwayId id de la pista.
     * @return ETA.
     */
    public int getETA(int runwayId) {
        return runwayETAs[runwayId];
    }

    /**
     * Permite establecer el tiempo actual de llegada.
     * @param ATA ATA.
     */
    public void setATA(int ATA) {
        this.ATA = ATA;
    }

    /**
     * Permite establecer la pista asignada.
     * @param assignedRunway pista.
     */
    public void setAssignedRunway(Runway assignedRunway) {
        this.assignedRunway = assignedRunway;
    }

    /**
     * Devuelve la pista asignada.
     * @return pista asignada.
     */
    public Runway getAssignedRunway(){
    	return assignedRunway;
    }

    /**
     * Calcula el retraso que ha tenido el avión.
     * Considerando el mínimo ETA.
     * ATA - min(ETAs).
     * @return retraso.
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public int getDelay() {
        int minETA = Arrays.stream(runwayETAs).min().getAsInt();
        return ATA - minETA;
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
}
