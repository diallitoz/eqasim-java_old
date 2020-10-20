package org.eqasim.core.simulation.mode_choice.utilities.estimators;

import java.util.List;

import org.eqasim.core.simulation.mode_choice.parameters.ModeParameters;
import org.eqasim.core.simulation.mode_choice.utilities.UtilityEstimator;
import org.eqasim.core.simulation.mode_choice.utilities.predictors.PersonPredictor;
import org.eqasim.core.simulation.mode_choice.utilities.predictors.PtCarPredictor;
import org.eqasim.core.simulation.mode_choice.utilities.variables.PtCarVariables;
//import org.eqasim.core.simulation.mode_choice.utilities.variables.PersonVariables;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.PlanElement;

import com.google.inject.Inject;

import ch.ethz.matsim.discrete_mode_choice.model.DiscreteModeChoiceTrip;

public class PtCarUtilityEstimator implements UtilityEstimator {
	private final ModeParameters parameters;
	private final PtCarPredictor ptCarPredictor;
	//private final PersonPredictor personPredictor;

	@Inject
	public PtCarUtilityEstimator(ModeParameters parameters, PersonPredictor personPredictor,
			PtCarPredictor ptCarPredictor) {
		this.parameters = parameters;
		this.ptCarPredictor = ptCarPredictor;
		//this.personPredictor = personPredictor;
	}

	protected double estimateConstantUtility() {
		return parameters.pt_car.alpha_u;
	}

	protected double estimateTravelTimeUtility(PtCarVariables variables) {
		return parameters.pt_car.betaTravelTime_u_min * variables.travelTime_min;
	}

	@Override
	public double estimateUtility(Person person, DiscreteModeChoiceTrip trip, List<? extends PlanElement> elements) {
		//PersonVariables personVariables = personPredictor.predictVariables(person, trip, elements);
		PtCarVariables ptCarVariables = ptCarPredictor.predictVariables(person, trip, elements);

		double utility = 0.0;

		utility += estimateConstantUtility();
		utility += estimateTravelTimeUtility(ptCarVariables);

		return utility;
	}
}
