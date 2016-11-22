package com.davazriel.airtafficcontroller;

import net.sf.jclec.RunExperiment;

public class Run {
    public static void main(String[] args) {
    	String[] newArgs = new String[1];
    	newArgs[0]= "src/main/resources/cfg/AirTrafficController.cfg";
        RunExperiment.main(newArgs);
    }
}
