package org.eqasim.ile_de_france.mode_choice;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.eqasim.core.analysis.CarPtEventHandler;
import org.eqasim.core.components.car_pt.routing.ParkRideManager;
//import org.eqasim.core.components.car_pt.routing.CarPtRoutingModule;
import org.eqasim.core.components.config.EqasimConfigGroup;
import org.eqasim.core.simulation.mode_choice.AbstractEqasimExtension;
import org.eqasim.core.simulation.mode_choice.ParameterDefinition;
import org.eqasim.core.simulation.mode_choice.parameters.ModeParameters;
import org.eqasim.core.simulation.mode_choice.utilities.estimators.CarPtUtilityEstimator;
import org.eqasim.core.simulation.mode_choice.utilities.estimators.PtCarUtilityEstimator;
import org.eqasim.core.simulation.mode_choice.utilities.predictors.CarPtPredictor;
import org.eqasim.core.simulation.mode_choice.utilities.predictors.PtCarPredictor;
import org.eqasim.ile_de_france.mode_choice.costs.IDFCarCostModel;
import org.eqasim.ile_de_france.mode_choice.costs.IDFPtCostModel;
import org.eqasim.ile_de_france.mode_choice.parameters.IDFCostParameters;
import org.eqasim.ile_de_france.mode_choice.parameters.IDFModeParameters;
//import org.eqasim.ile_de_france.mode_choice.parameters.IDFModeParameters;
import org.eqasim.ile_de_france.mode_choice.parameters.MelModeParameters;
import org.eqasim.ile_de_france.mode_choice.utilities.estimators.IDFBikeUtilityEstimator;
import org.eqasim.ile_de_france.mode_choice.utilities.estimators.IDFCarUtilityEstimator;
import org.eqasim.ile_de_france.mode_choice.utilities.predictors.IDFPersonPredictor;
import org.eqasim.ile_de_france.mode_choice.utilities.predictors.IDFSpatialPredictor;
import org.eqasim.core.simulation.mode_choice.constraints.IntermodalModesConstraint;
import org.eqasim.core.simulation.mode_choice.constraints.VehicleTourConstraintWithCar_Pt;
import org.eqasim.core.simulation.mode_choice.cost.CostModel;
import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.network.Network;
<<<<<<< HEAD
import org.matsim.api.core.v01.population.PopulationFactory;
=======
>>>>>>> e0722bc60eef3c96feb9cde3f4cd44107fc000e2
import org.matsim.core.config.CommandLine;
import org.matsim.core.config.CommandLine.ConfigurationException;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.network.algorithms.TransportModeNetworkFilter;
import org.matsim.core.router.RoutingModule;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import ch.ethz.matsim.discrete_mode_choice.components.utils.home_finder.HomeFinder;
import ch.ethz.matsim.discrete_mode_choice.modules.config.DiscreteModeChoiceConfigGroup;
import ch.ethz.matsim.discrete_mode_choice.modules.config.VehicleTourConstraintConfigGroup;

public class IDFModeChoiceModule extends AbstractEqasimExtension {
	private final CommandLine commandLine;

	public static final String MODE_AVAILABILITY_NAME = "IDFModeAvailability";

	public static final String CAR_COST_MODEL_NAME = "IDFCarCostModel";
	public static final String PT_COST_MODEL_NAME = "IDFPtCostModel";

	public static final String CAR_ESTIMATOR_NAME = "IDFCarUtilityEstimator";
	public static final String BIKE_ESTIMATOR_NAME = "IDFBikeUtilityEstimator";

	public final List<Coord> parkRideCoords;
	public final Network network;
<<<<<<< HEAD
	private final PopulationFactory populationFactory;

	public IDFModeChoiceModule(CommandLine commandLine, List<Coord> parkRideCoords, Network network,
			PopulationFactory populationFactory) {
		this.commandLine = commandLine;
		this.parkRideCoords = parkRideCoords;
		this.network = network;
		this.populationFactory = populationFactory;
=======

	public IDFModeChoiceModule(CommandLine commandLine, List<Coord> parkRideCoords, Network network) {
		this.commandLine = commandLine;
		this.parkRideCoords = parkRideCoords;
		this.network = network;
>>>>>>> e0722bc60eef3c96feb9cde3f4cd44107fc000e2
	}

	@Override
	protected void installEqasimExtension() {
		bindModeAvailability(MODE_AVAILABILITY_NAME).to(IDFModeAvailability.class);

		bind(IDFPersonPredictor.class);

		bindCostModel(CAR_COST_MODEL_NAME).to(IDFCarCostModel.class);
		bindCostModel(PT_COST_MODEL_NAME).to(IDFPtCostModel.class);

		bindUtilityEstimator(CAR_ESTIMATOR_NAME).to(IDFCarUtilityEstimator.class);
		bindUtilityEstimator(BIKE_ESTIMATOR_NAME).to(IDFBikeUtilityEstimator.class);

		// Register the estimator
		bindUtilityEstimator("car_pt").to(CarPtUtilityEstimator.class);
		bindUtilityEstimator("pt_car").to(PtCarUtilityEstimator.class);

		bind(IDFSpatialPredictor.class);

		// Register the predictor
		bind(ParkRideManager.class);
		bind(CarPtPredictor.class);
		bind(PtCarPredictor.class);

		bind(ModeParameters.class).to(IDFModeParameters.class);
<<<<<<< HEAD
		// bind(ModeParameters.class).to(MelModeParameters.class);

		// Constraint register
		bindTourConstraintFactory("VehicleTourConstraintWithCar_Pt").to(VehicleTourConstraintWithCar_Pt.Factory.class);
		bindTourConstraintFactory("IntermodalModesConstraint").to(IntermodalModesConstraint.Factory.class);

		// Intermodal count
=======
		
		//Constraint register
		bindTourConstraintFactory("VehicleTourConstraintWithCar_Pt")
		.to(VehicleTourConstraintWithCar_Pt.Factory.class);
		bindTourConstraintFactory("IntermodalModesConstraint")
		.to(IntermodalModesConstraint.Factory.class);
		
		//Intermodal count
>>>>>>> e0722bc60eef3c96feb9cde3f4cd44107fc000e2
		addEventHandlerBinding().to(CarPtEventHandler.class);

	}

	@Provides
	@Singleton
	public IDFModeParameters provideModeChoiceParameters(EqasimConfigGroup config)
			throws IOException, ConfigurationException {
		IDFModeParameters parameters = IDFModeParameters.buildDefault();

		if (config.getModeParametersPath() != null) {
			ParameterDefinition.applyFile(new File(config.getModeParametersPath()), parameters);
		}

		ParameterDefinition.applyCommandLine("mode-choice-parameter", commandLine, parameters);
		return parameters;
	}

	@Provides
	@Singleton
	public MelModeParameters provideMelModeChoiceParameters(EqasimConfigGroup config)
			throws IOException, ConfigurationException {
		MelModeParameters parameters = MelModeParameters.buildDefault();

		if (config.getModeParametersPath() != null) {
			ParameterDefinition.applyFile(new File(config.getModeParametersPath()), parameters);
		}

		ParameterDefinition.applyCommandLine("mode-choice-parameter", commandLine, parameters);
		return parameters;
	}

	@Provides
	@Singleton
	public IDFCostParameters provideCostParameters(EqasimConfigGroup config) {
		IDFCostParameters parameters = IDFCostParameters.buildDefault();

		if (config.getCostParametersPath() != null) {
			ParameterDefinition.applyFile(new File(config.getCostParametersPath()), parameters);
		}

		ParameterDefinition.applyCommandLine("cost-parameter", commandLine, parameters);
		return parameters;
	}

	@Provides
	@Singleton
	public VehicleTourConstraintWithCar_Pt.Factory provideVehicleTourConstraintWithCar_PtFactory(
			DiscreteModeChoiceConfigGroup dmcConfig, @Named("tour") HomeFinder homeFinder) {
		VehicleTourConstraintConfigGroup config = dmcConfig.getVehicleTourConstraintConfig();
		return new VehicleTourConstraintWithCar_Pt.Factory(config.getRestrictedModes(), homeFinder, parkRideCoords);
	}
<<<<<<< HEAD

=======
	
>>>>>>> e0722bc60eef3c96feb9cde3f4cd44107fc000e2
	@Provides
	@Singleton
	public IntermodalModesConstraint.Factory provideIntermodalModesConstraintFactory(
			DiscreteModeChoiceConfigGroup dmcConfig, @Named("tour") HomeFinder homeFinder) {
		VehicleTourConstraintConfigGroup config = dmcConfig.getVehicleTourConstraintConfig();
		return new IntermodalModesConstraint.Factory(config.getRestrictedModes(), homeFinder, parkRideCoords, network);
	}
<<<<<<< HEAD

=======
>>>>>>> e0722bc60eef3c96feb9cde3f4cd44107fc000e2
}
