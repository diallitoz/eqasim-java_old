package org.eqasim.san_francisco.mode_choice.utilities.predictors;

import java.util.List;

import org.eqasim.core.simulation.mode_choice.utilities.predictors.CachedVariablePredictor;
import org.eqasim.san_francisco.mode_choice.utilities.variables.SanFranciscoPersonVariables;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.PlanElement;

import ch.ethz.matsim.discrete_mode_choice.model.DiscreteModeChoiceTrip;

public class SanFranciscoPersonPredictor extends CachedVariablePredictor<SanFranciscoPersonVariables> {
	@Override
	protected SanFranciscoPersonVariables predict(Person person, DiscreteModeChoiceTrip trip,
			List<? extends PlanElement> elements) {
		boolean hasSubscription = SanFranciscoPredictorUtils.hasSubscription(person);
		return new SanFranciscoPersonVariables(hasSubscription);
	}
}
