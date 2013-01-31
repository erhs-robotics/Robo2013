package neuralNet;

public class Utils {

	private static final double b = 0.0001;
	
	public static double sigmoid(double n) {
		return 1 / (1 + Math.pow(Math.E, (-b)*n));
	}
}
