package org.erhsroboticsclub.robo2013.utilities.neuralNet;

public class NeuronLayer {

	private double[][] weights; //to next layer 
	private double inputs[];
	private double outputs[];
	private int neurons;
	
	public NeuronLayer(int neurons, int inputs) {
		this.inputs = new double[inputs+1];  //With bias
		this.inputs[0] = 1;
		this.outputs = new double[neurons];
		this.weights = new double[neurons][inputs+1];
		this.neurons=neurons;
	}
	
	public void calcOutputs() {
		for(int i=0; i<neurons; i++) {
			double total=0;
			for(int j=0; j<inputs.length; i++) {
				total += inputs[j] * weights[i][j];
			}
			outputs[i] = Utils.sigmoid(total);
		}
	}
	
	public double[] getInputs() {return inputs;}
	public void setInputs(double[] in) {inputs = in;}
	public void setInput(double in, int index) {inputs[index] = in;}
	
	public double[] getOutputs() {return outputs;}
	public double getOutput(int i) {return outputs[i];}
	
	public double[][] getWeights() {return weights;}
	public void setWeights(double[][] w) {weights = w;}
	public void setWeight(double w, int i, int j) {weights[i][j] = w;}
	public double getWeight(int i, int j) {return weights[i][j];}
	
	
}
