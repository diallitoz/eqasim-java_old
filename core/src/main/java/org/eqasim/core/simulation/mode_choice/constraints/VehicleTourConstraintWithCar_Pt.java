package org.eqasim.core.simulation.mode_choice.constraints;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.matsim.api.core.v01.BasicLocation;
import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.population.Person;

import ch.ethz.matsim.discrete_mode_choice.components.utils.LocationUtils;
import ch.ethz.matsim.discrete_mode_choice.components.utils.home_finder.HomeFinder;
import ch.ethz.matsim.discrete_mode_choice.model.DiscreteModeChoiceTrip;
import ch.ethz.matsim.discrete_mode_choice.model.tour_based.TourCandidate;
import ch.ethz.matsim.discrete_mode_choice.model.tour_based.TourConstraint;
import ch.ethz.matsim.discrete_mode_choice.model.tour_based.TourConstraintFactory;

public class VehicleTourConstraintWithCar_Pt implements TourConstraint {
	private final Collection<String> restrictedModes;
	private final Id<? extends BasicLocation> homeLocationId;
	private final List<Coord> parkRideCoords;

	public VehicleTourConstraintWithCar_Pt(Collection<String> restrictedModes,
			Id<? extends BasicLocation> homeLocationId, List<Coord> parkRideCoords) {
		this.restrictedModes = restrictedModes;// What are these modes for agent????
		this.homeLocationId = homeLocationId;
		this.parkRideCoords = parkRideCoords;
	}

	private int getFirstIndex(String mode, List<String> modes) {
		for (int i = 0; i < modes.size(); i++) {
			if (modes.get(i).equals(mode)) {
				return i;
			}
		}

		return -1;
	}

	private int getLastIndex(String mode, List<String> modes) {
		for (int i = modes.size() - 1; i >= 0; i--) {
			if (modes.get(i).equals(mode)) {
				return i;
			}
		}

		return -1;
	}

	@Override
	public boolean validateBeforeEstimation(List<DiscreteModeChoiceTrip> tour, List<String> modes,
			List<List<String>> previousModes) {// What are the modes for an agent???
		// if (modes.contains("car_pt")) {
		if (restrictedModes.contains("car_pt") && !restrictedModes.contains("pt_car")) {
			return false;
		}

		if (restrictedModes.contains("pt_car") && !restrictedModes.contains("car_pt")) {
			return false;
		}
		
		if (!restrictedModes.contains("pt_car") || !restrictedModes.contains("car_pt")) {
			return false;
		}
		
		//if(Collections.frequency(modes, "pt_car")!=Collections.frequency(modes, "car_pt")) {
		//	return false;
		//}

		// * If the mode used is car-pt for the outward trip then for the return, the
		// mode
		// * to be used must be pt_car. For the moment, it is assumed that the user
		// always
		// * comes home with his car, necessarily passing through the relay car park.
		for (String restrictedMode : restrictedModes) {

			if (modes.contains(restrictedMode)) {
				int firstIndex = getFirstIndex(restrictedMode, modes);
				int lastIndex = getLastIndex(restrictedMode, modes);

				if (homeLocationId != null) {
					Id<? extends BasicLocation> startLocationId = LocationUtils
							.getLocationId(tour.get(firstIndex).getOriginActivity());
					Id<? extends BasicLocation> endLocationId = LocationUtils
							.getLocationId(tour.get(lastIndex).getDestinationActivity());

					if (!startLocationId.equals(homeLocationId)) {
						return false;
					}

					if (!endLocationId.equals(homeLocationId)) {
						return false;
					}
				} else {
					if (firstIndex > 0 || lastIndex < modes.size() - 1) {
						return false;
					}
				}
			}
			
			if (restrictedMode.equals("car_pt")) {
				int first_car_pt = getFirstIndex(restrictedMode, modes);
				DiscreteModeChoiceTrip trip = tour.get(first_car_pt);
				if (!trip.getInitialMode().equals("car_pt")) {
					return false;
				}
				
			}

			if (restrictedMode.equals("pt_car")) {
				int first_pt_car = getFirstIndex(restrictedMode, modes);
				DiscreteModeChoiceTrip trip = tour.get(first_pt_car);
				if (!trip.getInitialMode().equals("pt_car")) {
					return false;
				}
			}
			
/*
			for (int i = 0; i < tour.size(); i++) {
				if (i < tour.size() - 1) {
					DiscreteModeChoiceTrip nextTrip = tour.get(i + 1);

					if (restrictedMode == "car_pt") {
						if (!nextTrip.getInitialMode().equals("pt_car")) {
							return false;
						}
					}

					if (restrictedMode == "pt_car") {
						if (!nextTrip.getInitialMode().equals("car_pt")) {
							return false;
						}
					}
				}

			}
*/
		}

		
		return true;
	}

	@Override
	public boolean validateAfterEstimation(List<DiscreteModeChoiceTrip> tour, TourCandidate candidate,
			List<TourCandidate> previousCandidates) {
		return true;
	}

	public static class Factory implements TourConstraintFactory {
		private final Collection<String> restrictedModes;
		private final HomeFinder homeFinder;
		private final List<Coord> parkRideCoords;

		public Factory(Collection<String> restrictedModes, HomeFinder homeFinder, List<Coord> parkRideCoords) {
			this.restrictedModes = restrictedModes;
			this.homeFinder = homeFinder;
			this.parkRideCoords = parkRideCoords;
		}

		@Override
		public TourConstraint createConstraint(Person person, List<DiscreteModeChoiceTrip> planTrips,
				Collection<String> availableModes) {
			return new VehicleTourConstraintWithCar_Pt(restrictedModes, homeFinder.getHomeLocationId(planTrips),
					parkRideCoords);
		}
	}

}
