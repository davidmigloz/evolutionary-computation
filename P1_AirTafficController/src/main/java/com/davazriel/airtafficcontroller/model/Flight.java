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
     * Devuelve los tiempo estimados de llegada del avión en las diferentes pistas.
     *
     * @return ETAs.
     */
    public int[] getETAs() {
        return runwayETAs.clone();
    }

    /**
     * Devuelve el menor tiempo estimado de llegada del avión.
     *
     * @return min ETA.
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public int getMinETA() {
        return Arrays.stream(runwayETAs).min().getAsInt();
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
     * Devuelve el momento final en el que aterrirza.
     *
     * @return ATA.
     */
    public int getATA() {
        return ATA;
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
    public int getDelay() {
        return ATA - getMinETA();
    }

    /**
     * Devuelve la diferencia entre tiempos de aterrizaje del vuelo actual y el asociado.
     *
     * @return retraso.
     */
    public int getContiguousFlightDifference() {
        return contiguousFlight == null ? 0 : Math.abs(this.getATA() - contiguousFlight.getATA());
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
    public void setContiguousFlight(Flight contiguousFlight) {
        this.contiguousFlight = contiguousFlight;
    }
}
