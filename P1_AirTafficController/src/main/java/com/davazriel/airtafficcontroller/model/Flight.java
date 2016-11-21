package com.davazriel.airtafficcontroller.model;

public class Flight {

    private int id;
    private PlaneType type;
    private int[] eta;

    public Flight(int id, PlaneType type, int[] eta) {
        this.id = id;
        this.type = type;
        this.eta = eta;
    }

    public int getId() {
        return id;
    }

    public PlaneType getPlaneType() {
        return type;
    }

    public int getEta(int trackNumber) {
        return eta[trackNumber];
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
