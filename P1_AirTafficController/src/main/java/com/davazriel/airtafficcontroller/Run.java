package com.davazriel.airtafficcontroller;

import net.sf.jclec.RunExperiment;

public class Run {
    public static void main(String[] args) {
        String[] newArgs = new String[1];
        switch (args[0]) {
            case "1":
                newArgs[0] = "src/main/resources/cfg/ScheduleFlights1.cfg";
                break;

            case "2":
                newArgs[0] = "src/main/resources/cfg/ScheduleFlights2.cfg";
                break;

            default:
                newArgs[0] = args[0];
                break;
        }
        RunExperiment.main(newArgs);
    }
}
