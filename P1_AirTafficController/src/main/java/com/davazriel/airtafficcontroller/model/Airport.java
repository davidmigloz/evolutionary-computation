package com.davazriel.airtafficcontroller.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Modela un aeropuerto.
 */
public class Airport {

	private int[][] waitingTimes;
	private Runway[] runways;
	private List<Flight> flights;

	/**
	 * Constructor del aeropuerto.
	 * 
	 * @param numRunways numero de pistas.
	 * @param waitingTimes tiempos de espera.
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
	 * Planifica un vuelo. Asignándole la pista que le permita aterrizar lo antes posible.
	 * 
	 * @param flight vuelo.
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
	 * Devuelve la pista con el menor tiempo de espera para el avión en concreto.
	 * 
	 * @param planeType tipo de avión.
	 * @return mejor pista.
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
	 * Calcula lo que tiene que esperar un avión de un determinado tipo hasta poder
	 * aterrizar dependiendo del avión que haya aterrizado delante de él.
	 * 
	 * @param before primer avión en aterrizar.
	 * @param after segundo avión en aterrizar.
	 * @return tiempo que el segundo tiene que esperar.
	 */
	public int getWaitingTime(Flight.PlaneType before, Flight.PlaneType after) {
		return waitingTimes[before.getIndex()][after.getIndex()];
	}

	/**
	 * Calcula el retraso acumulado (sumatorio de todos los ATA-min(ETAs)).
	 * @return retraso acumulado.
	 */
	public int getAccumulatedDelay() {
		int total = 0;
		for (Flight flight : flights) {
			total += flight.getDelay();
		}
		return total;
	}

	/**
	 * Devuelve el ATA del último vuelo.
	 * @return ATA máximo.
	 */
	@SuppressWarnings("OptionalGetWithoutIsPresent")
    public int getMaxATA() {
		return Arrays.stream(runways).mapToInt(Runway::getCurrentATA).min().getAsInt();
	}
}
