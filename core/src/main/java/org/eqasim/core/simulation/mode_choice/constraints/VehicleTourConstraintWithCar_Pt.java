package org.eqasim.core.simulation.mode_choice.constraints;

import java.util.Collection;
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
		this.restrictedModes = restrictedModes;//What are these modes for agent????
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
			List<List<String>> previousModes) {//What are the modes for an agent???
		//if (modes.contains("car_pt")) {
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
						if (firstIndex > 0 || lastIndex < modes.size() - 1) {//what does this condition mean?
							return false;
						}
					}
					

					Id<? extends BasicLocation> currentLocationId = LocationUtils
							.getLocationId(tour.get(firstIndex).getDestinationActivity());

					for (int index = firstIndex + 1; index <= lastIndex; index++) {
						if (modes.get(index).equals(restrictedMode)) {//Si c'est toujours le meme mode pour le prochain deplacement
							DiscreteModeChoiceTrip trip = tour.get(index);

							if (!currentLocationId.equals(LocationUtils.getLocationId(trip.getOriginActivity()))) {
								return false;
							}

							currentLocationId = LocationUtils.getLocationId(trip.getDestinationActivity());
						}
					}
					
					// Case of car_pt
					
					if(restrictedMode=="car_pt") {
						Id<? extends BasicLocation> starLoId = LocationUtils
								.getLocationId(tour.get(modes.indexOf("car_pt")).getOriginActivity());
						Id<? extends BasicLocation> endLoId= LocationUtils
								.getLocationId(tour.get(modes.indexOf("car_pt")).getDestinationActivity());
						
					}
				}
			}

		//}

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
			return new VehicleTourConstraintWithCar_Pt(restrictedModes, homeFinder.getHomeLocationId(planTrips),parkRideCoords);
		}
	}

}
