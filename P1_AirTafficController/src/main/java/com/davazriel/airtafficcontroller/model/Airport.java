package com.davazriel.airtafficcontroller.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Clase Airport. Sirve para modelar el aeropuerto, sus pistas, tiempos de
 * espera y vuelos entrantes.
 */
public class Airport {

	private int[][] waitingTimes;
	private Runway[] runways;
	private List<Flight> flights;

	/**
	 * Al construirlo le damos el numero de pistas y los tiempos de espera.
	 * 
	 * @param numRunways
	 * @param waitingTimes
	 */
	public Airport(int numRunways, int[][] waitingTimes) {
		flights = new ArrayList<>();
		// Create runways
		runways = new Runway[numRunways];
		for (int i = 0; i < numRunways; i++) {
			runways[i] = new Runway(i, this);
		}
		// Get waiting times
		this.waitingTimes = waitingTimes;
	}

	/**
	 * Añadimos un vuelo a llegadas como siguiente en la pista más rapida.
	 * 
	 * @param flight
	 */
	public void scheduleFlight(Flight flight) {
		// Find runway with lowest time for landing
		Runway runway = findBestRunway(flight.getPlaneType());
		// Schedule flight in that runway
		runway.addArrival(flight);
		// Save flight
		flights.add(flight);
	}

	/**
	 * Devolvemos la pista con menor tiempo de espera para el avion en concreto.
	 * 
	 * @param planeType
	 * @return
	 */
	private Runway findBestRunway(Flight.PlaneType planeType) {
		Runway bestRunway = null;
		int bestTime = Integer.MAX_VALUE;
		// Get runway with the best time
		for (Runway runway : runways) {
			int nextTimeAvailable = runway.getNextTimeRunwayAvailable(planeType);
			if (nextTimeAvailable < bestTime) {
				bestRunway = runway;
				bestTime = nextTimeAvailable;
			}
		}
		return bestRunway;
	}

	/**
	 * Calculamos lo que tiene que esperar un avion de un tipo hasta poder
	 * aterrizar tras que otro haya aterrizado antes.
	 * 
	 * @param before Primer avion en aterrizar.
	 * @param after Segundo avion en aterrizar.
	 * @return Tiempo que el segundo tiene que esperar.
	 */
	public int getWaitingTime(Flight.PlaneType before, Flight.PlaneType after) {
		return waitingTimes[before.getIndex()][after.getIndex()];
	}

	/**
	 * Calculamos y devolvemos el sumatorio de todos los ATA-min(ETAs)
	 * @return 
	 */
	public int getAccumulatedDelay() {
		int total = 0;
		for (Flight flight : flights) {
			total += flight.getDelay();
		}
		return total;
	}

	/**
	 * Devolvemos en que momento llega el ultimo vuelo (ultimo ATA)
	 * @return
	 */
	public int getMaxATA() {
		return Arrays.stream(runways).mapToInt(Runway::getCurrentATA).min().getAsInt();
	}
}
