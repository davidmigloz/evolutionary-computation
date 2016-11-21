package com.davazriel.airtafficcontroller.model;

public class Flight {

    public enum Type {
        HEAVY(0), BIG(1), SMALL(2);
        private int index;

        Type(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }

    private int id;
    private Type type;
    private int[] eta;

    public Flight(int id, Type type, int[] eta) {
        this.id = id;
        this.type = type;
        this.eta = eta;
    }

    public int getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public int[] getEta() {
        return eta;
    }
}
