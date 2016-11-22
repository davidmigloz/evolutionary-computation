package com.davazriel.airtafficcontroller.model;

import java.util.Arrays;

/**
 * Flight.
 */
public class Flight {

    private int id;
    private PlaneType type;
    private int[] runwayETAs;
    private int ATA;
    private Runway assignedRunway;

    public Flight(int id, PlaneType type, int[] runwayETAs) {
        this.id = id;
        this.type = type;
        this.runwayETAs = runwayETAs;
    }

    public int getId() {
        return id;
    }

    public PlaneType getPlaneType() {
        return type;
    }

    public int getETA(int runwayId) {
        return runwayETAs[runwayId];
    }

    public void setATA(int ATA) {
        this.ATA = ATA;
    }

    public void setAssignedRunway(Runway assignedRunway) {
        this.assignedRunway = assignedRunway;
    }
    
    public Runway getAssignedRunway(){
    	return assignedRunway;
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public int getDelay() {
        int minETA = Arrays.stream(runwayETAs).min().getAsInt();
        return ATA - minETA;
    }

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
