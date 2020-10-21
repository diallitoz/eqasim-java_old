package org.eqasim.core.analysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.matsim.api.core.v01.events.ActivityStartEvent;
import org.matsim.api.core.v01.events.handler.ActivityStartEventHandler;

public class CarPtEventHandler implements ActivityStartEventHandler {
	private long intermodalCountCarPt = 0;
	private long intermodalCountPtCar = 0;
	//private OutputDirectoryHierarchy outputDirectory;

	@Override
	public void handleEvent(ActivityStartEvent event) {
		if (event.getActType().equals("carPt interaction")) {
			intermodalCountCarPt += 1;
		}
		
		if (event.getActType().equals("ptCar interaction")) {
			intermodalCountPtCar += 1;
		}

	}

	@Override
	public void reset(int iteration) {
		String counter1 = "Number of carPt interaction = " + intermodalCountCarPt + "\n";
		
		String counter2 = "Number of ptCar interaction = " + intermodalCountPtCar + "\n";

		File outputFile = new File("/home/dialloaziseoumar/AziseThesis/GenerationPopulationSynthetique/mel/output/intermodalCount" + iteration + ".html");
		
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
		
			writer.write(counter1);
			writer.flush();
			writer.write(counter2);
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		//BufferedWriter writer = new BufferedWriter("/home/dialloaziseoumar/AziseThesis/GenerationPopulationSynthetique/mel/output/CarPtInteractionCount.json");

		
		intermodalCountCarPt = 0;
		intermodalCountPtCar = 0;

	}

}
