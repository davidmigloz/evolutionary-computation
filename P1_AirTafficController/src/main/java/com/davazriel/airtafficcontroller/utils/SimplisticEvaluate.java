package com.davazriel.airtafficcontroller.utils;

import java.util.ArrayList;
import java.util.List;

import com.davazriel.airtafficcontroller.model.Airport;
import com.davazriel.airtafficcontroller.model.Flight;

public class SimplisticEvaluate {
	public static void main(String[] args) {
		List<Flight> flights = new ArrayList<>();
		int[][] a = { { 20, 16, 20, 30, 30 }, { 0, 2, 6, 3, 2 }, { 7, 2, 12, 3, 3 }, { 16, 24, 17, 12, 10 },
				{ 12, 4, 17, 2, 4 }, { 7, 13, 2, 12, 15 }, { 0, 3, 9, 10, 4 }, { 15, 21, 9, 11, 22 }, { 1, 6, 8, 7, 0 },
				{ 10, 17, 16, 13, 12 }, { 3, 4, 2, 3, 0 }, { 17, 9, 18, 27, 13 }, { 5, 14, 3, 3, 1 },
				{ 13, 9, 21, 22, 20 }, { 18, 26, 12, 22, 28 }, { 19, 25, 10, 15, 23 }, { 2, 5, 8, 0, 10 },
				{ 20, 30, 11, 23, 17 }, { 13, 14, 12, 4, 12 }, { 17, 26, 14, 11, 17 }, { 7, 3, 9, 2, 13 },
				{ 5, 4, 9, 11, 6 }, { 18, 19, 21, 18, 11 }, { 0, 9, 4, 1, 2 } };
		flights.add(new Flight("0", Flight.PlaneType.HEAVY, a[0]));
		flights.add(new Flight("1", Flight.PlaneType.BIG, a[1]));
		flights.add(new Flight("2", Flight.PlaneType.HEAVY, a[2]));
		flights.add(new Flight("3", Flight.PlaneType.HEAVY, a[3]));
		flights.add(new Flight("4", Flight.PlaneType.SMALL, a[4]));
		flights.add(new Flight("5", Flight.PlaneType.HEAVY, a[5]));
		flights.add(new Flight("6", Flight.PlaneType.BIG, a[6]));
		flights.add(new Flight("7", Flight.PlaneType.HEAVY, a[7]));
		flights.add(new Flight("8", Flight.PlaneType.HEAVY, a[8]));
		flights.add(new Flight("9", Flight.PlaneType.SMALL, a[9]));
		flights.add(new Flight("10", Flight.PlaneType.HEAVY, a[10]));
		flights.add(new Flight("11", Flight.PlaneType.BIG, a[11]));
		flights.add(new Flight("12", Flight.PlaneType.HEAVY, a[12]));
		flights.add(new Flight("13", Flight.PlaneType.BIG, a[13]));
		flights.add(new Flight("14", Flight.PlaneType.HEAVY, a[14]));
		flights.add(new Flight("15", Flight.PlaneType.HEAVY, a[15]));
		flights.add(new Flight("16", Flight.PlaneType.SMALL, a[16]));
		flights.add(new Flight("17", Flight.PlaneType.HEAVY, a[17]));
		flights.add(new Flight("18", Flight.PlaneType.BIG, a[18]));
		flights.add(new Flight("19", Flight.PlaneType.HEAVY, a[19]));
		flights.add(new Flight("20", Flight.PlaneType.HEAVY, a[20]));
		flights.add(new Flight("21", Flight.PlaneType.SMALL, a[21]));
		flights.add(new Flight("22", Flight.PlaneType.HEAVY, a[22]));
		flights.add(new Flight("23", Flight.PlaneType.BIG, a[23]));
		int[][] waitTimes = { { 2, 3, 4 }, { 2, 3, 3 }, { 2, 2, 2 } };
		int nRunways = 5;
		int[] genotype ={};
			//{17,22,19,8,10,12,2,4,1,9,5,16,11,20,13,14,18,7,3,21,6,15,0,23}; 
			//{8,10,17,16,11,19,20,13,14,22,18,12,2,4,1,9,5,3,7,0,6,15,21,23};
		System.out.println(evaluate1(genotype, flights, nRunways, waitTimes));
		
	}

	public static int evaluate1(int[] genotype, List<Flight> flights, int nRunways, int[][] waitTimes) {
		// Create airport
		Airport airport = new Airport(nRunways, waitTimes);
		// schedule flights: genotype order = arrival order; genotipe values =
		// flight number
		for (int vuelo : genotype) {
			Flight flight = flights.get(vuelo);
			airport.scheduleFlight(flight);
		}
		// calculate fitness
		return airport.getAccumulatedDelay();
	}

}
