package org.erhsroboticsclub.robo2013.utilities.neuralNet;

import org.erhsroboticsclub.robo2013.utilities.MathX;

public class Utils {

	private static final double b = 0.0001;
	
	public static double sigmoid(double n) {
		return 1 / (1 + MathX.pow(Math.E, (-b)*n));
	}
}
