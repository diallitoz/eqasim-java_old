package org.eqasim.core.simulation.mode_choice.constraints;

import java.util.Collection;
import java.util.List;

import org.matsim.api.core.v01.population.Person;

import ch.ethz.matsim.discrete_mode_choice.model.DiscreteModeChoiceTrip;
import ch.ethz.matsim.discrete_mode_choice.model.constraints.AbstractTripConstraint;
import ch.ethz.matsim.discrete_mode_choice.model.trip_based.TripConstraint;
import ch.ethz.matsim.discrete_mode_choice.model.trip_based.TripConstraintFactory;

public class Car_PtConstraint extends AbstractTripConstraint {
	public static final String CAR_PT_MODE = "car_pt";

	@Override
	public boolean validateBeforeEstimation(DiscreteModeChoiceTrip trip, String mode, List<String> previousModes) {
		if (trip.getInitialMode().equals(CAR_PT_MODE)) {
			if (!mode.equals(CAR_PT_MODE)) {
				return false;
			}
		}

		if (mode.equals(CAR_PT_MODE)) {
			if (!trip.getInitialMode().equals(CAR_PT_MODE)) {
				return false;
			}
		}

		return true;
	}

	static public class Factory implements TripConstraintFactory {
		@Override
		public TripConstraint createConstraint(Person person, List<DiscreteModeChoiceTrip> planTrips,
				Collection<String> availableModes) {
			return new PassengerConstraint();
		}
	}

}
