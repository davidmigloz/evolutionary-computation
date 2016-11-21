package com.davazriel.airtafficcontroller.model;

public class Airport {

    private int[][] waitingTimes;

    public Airport(int[][] waitingTimes) {
        this.waitingTimes = waitingTimes;
    }

    public int getWaitingTime(Flight.Type before, Flight.Type after) {
        return waitingTimes[before.getIndex()][after.getIndex()];
    }
}
