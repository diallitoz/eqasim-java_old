package org.eqasim.ile_de_france.mode_choice.parameters;

import org.eqasim.core.simulation.mode_choice.parameters.ModeParameters;

public class MelModeParameters extends ModeParameters {
	public class IDFCarParameters {
		public double betaInsideUrbanArea;
		public double betaCrossingUrbanArea;
	}

	public class IDFBikeParameters {
		public double betaInsideUrbanArea;
	}

	public final IDFCarParameters idfCar = new IDFCarParameters();
	public final IDFBikeParameters idfBike = new IDFBikeParameters();

	public static MelModeParameters buildDefault() {
		MelModeParameters parameters = new MelModeParameters();

		// Cost
		parameters.betaCost_u_MU = -0.167;
		parameters.lambdaCostEuclideanDistance = -0.4;
		parameters.referenceEuclideanDistance_km = 3.0;//24 for IDF

		// Car
		parameters.car.alpha_u = -0.463;
		parameters.car.betaTravelTime_u_min = -0.0193;

		parameters.car.constantAccessEgressWalkTime_min = 4.0;
		parameters.car.constantParkingSearchPenalty_min = 4.0;

		parameters.idfCar.betaInsideUrbanArea = -0.5;
		parameters.idfCar.betaCrossingUrbanArea = -1.0;

		// PT
		parameters.pt.alpha_u = -1.37;
		parameters.pt.betaLineSwitch_u = -0.17;
		parameters.pt.betaInVehicleTime_u_min = -0.017;
		parameters.pt.betaWaitingTime_u_min = -0.0484;
		parameters.pt.betaAccessEgressTime_u_min = -0.0804;

		// Bike
		parameters.bike.alpha_u = -0.888;
		parameters.bike.betaTravelTime_u_min = -0.127;
		parameters.bike.betaAgeOver18_u_a = -0.0496;

		parameters.idfBike.betaInsideUrbanArea = 1.5;

		// Walk
		parameters.walk.alpha_u = 2.48;
		parameters.walk.betaTravelTime_u_min = -0.142;
		
		// To do: parameters for car_pt and pt_car modes

		return parameters;
	}
}
