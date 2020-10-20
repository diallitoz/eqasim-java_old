package org.eqasim.core.components.car_pt.routing;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.population.PopulationUtils;
import org.matsim.core.router.LinkWrapperFacility;
import org.matsim.core.router.RoutingModule;
import org.matsim.core.router.StageActivityTypes;
import org.matsim.core.router.StageActivityTypesImpl;
import org.matsim.facilities.Facility;

public class PtCarRoutingModule implements RoutingModule {

	private final RoutingModule carRoutingModule;
	private final Network network;

	// Create an object of a ptRoutingModule
	private final RoutingModule ptRoutingModule;

	public PtCarRoutingModule(RoutingModule ptRoutingModule, RoutingModule roadRoutingModule, Network network) {
		this.carRoutingModule = roadRoutingModule;
		this.ptRoutingModule = ptRoutingModule;
		this.network = network;

	}

	@Override
	public List<? extends PlanElement> calcRoute(Facility fromFacility, Facility toFacility, double departureTime,
			Person person) {
		// Park and ride lot location
		List<Coord> parkRideCoords = new ArrayList<Coord>();

		double[] xCoord = { 695217.09, 691365.43, 703543.53, 702770.20, 693929.84, 704530.69, 708963.08, 711811.05,
				685914.90, 712180.02, 702337.39, 709906.41 };

		double[] yCoord = { 7059186.19, 7065019.42, 7057923.10, 7056776.29, 7050511.72, 7057833.24, 7061460.64,
				7068903.84, 7047847.26, 7071112.37, 7049972.24, 7056430.63 };

		for (int i = 0; i < yCoord.length; i++) {
			Coord prCoord = new Coord(xCoord[i], yCoord[i]);
			parkRideCoords.add(prCoord);
		}

		Coord prCoord = parkRideCoords.get(0); // XY coords of some "test" P&R station somewhere in your scenario. Later
												// you'll want to choose one specifically

		Link prLink = NetworkUtils.getNearestLink(network, prCoord);

		Facility prFacility = new LinkWrapperFacility(prLink);

		// Creation of a PT trip from the destination point to PR facility
		List<? extends PlanElement> ptElements = ptRoutingModule.calcRoute(fromFacility, prFacility, departureTime,
				person);

		// double vehicleDistance = Double.NaN;
		double vehicleTravelTime = Double.NaN;
		// double price = Double.NaN;

		Leg leg = (Leg) ptElements.get(0);
		// vehicleDistance = leg.getRoute().getDistance();
		vehicleTravelTime = leg.getRoute().getTravelTime();

		// Given the request time, we can calculate the waiting time
		double timeToAccessCar = 300; // We take 5 min to park the car and access to PT

		double carDepartureTime = departureTime + vehicleTravelTime + timeToAccessCar;

		// Creation of a the car trip from the PR facility to the origin point (home)
		List<? extends PlanElement> carElements = carRoutingModule.calcRoute(prFacility, toFacility, carDepartureTime,
				null);

		// Creation interaction between car and pt
		Activity interactionActivtyPtCar = PopulationUtils.createActivityFromCoordAndLinkId("ptCar interaction",
				prCoord, prLink.getId());

		// Creation full trip
		List<PlanElement> allElements = new LinkedList<>();
		allElements.addAll(ptElements);
		allElements.add(interactionActivtyPtCar);
		allElements.addAll(carElements);

		return allElements;
	}

	@Override
	public StageActivityTypes getStageActivityTypes() {
		return new StageActivityTypesImpl("pt interaction", "ptCar interaction");
	}

}
