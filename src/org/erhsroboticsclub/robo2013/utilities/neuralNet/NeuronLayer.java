package org.erhsroboticsclub.robo2013.utilities.neuralNet;


import com.sun.squawk.util.Assert;
import java.util.Random;

public class  NeuronLayer{

    private double[][][] weights; //to next layer
    private double[][][] prevWeights;
    private double[][] delta;
    private double[][] outputs;//The output for each neuron in each layer
    private int numLayers;
    private int[] nodesInLayers;
    private final double alpha = 0.1; // learning rate
    private final double beta = 0.5; // learning momentum

    public NeuronLayer(int[] layers) {
        nodesInLayers = layers;
        //init the output for each layer
        numLayers = layers.length;

        outputs = new double[numLayers][];
        delta = new double[numLayers][];
        for (int i = 0; i < numLayers; i++) {
            outputs[i] = new double[layers[i]];
            delta[i] = new double[layers[i]];
        }

        weights = new double[numLayers][][];
        prevWeights = new double[numLayers][][];

        for (int i = 1; i < numLayers; i++) {
            weights[i] = new double[layers[i]][layers[i - 1] + 1];//+1 for bias
            prevWeights[i] = new double[layers[i]][layers[i - 1] + 1];
        }

        Random r = new Random();
        //init weights
        for (int i = 1; i < numLayers; i++) {
            for (int j = 0; j < outputs[i].length; j++) {
                for (int k = 0; k < weights[i][j].length; k++) {
                    weights[i][j][k] = r.nextDouble() - 0.5;
                    prevWeights[i][j][k] = 0;
                }
            }
        }


    }

    public void calcOutputs(double[] inputs) {

        //place inputs in input layer
        for (int i = 0; i < nodesInLayers[0]; i++) {
            outputs[0][i] = inputs[i];
        }

        //calculate outputs
        //foreach layer
        for (int i = 1; i < numLayers; i++) {
            //foreach node in layer
            for (int j = 0; j < nodesInLayers[i]; j++) {
                double sum = 0.0;
                //foreach weight in node
                for (int k = 0; k < nodesInLayers[i - 1]; k++) {
                    sum += outputs[i - 1][k] * weights[i][j][k];
                }
                //apply bias
                sum += weights[i][j][nodesInLayers[i - 1]];
                outputs[i][j] = Utils.sigmoid(sum);
            }

        }

    }

    public void bpgt(double[] input, double[] target) {
        calcOutputs(input);
        //find delta for output layer
        for (int i = 0; i < nodesInLayers[numLayers - 1]; i++) {
            double y = outputs[numLayers - 1][i]; //the output of the node
            // y * (1-y) is f'(x) and (target[i] - y) is the error
            delta[numLayers - 1][i] = y * (1 - y) * (target[i] - y);
        }

        //find delta for hidden layers
        //i=numLayers-2 because output and input layers are not hidden layers
        //for each layer
        for (int i = numLayers - 2; i > 0; i--) {
            //for each node in layer
            for (int j = 0; j < nodesInLayers[i]; j++) {
                double blame = 0.0;
                //for each wieght connecting the node to the next layer
                for (int k = 0; k < nodesInLayers[i + 1]; k++) {
                    blame += delta[i + 1][k] * weights[i + 1][k][j];
                }
                delta[i][j] = outputs[i][j] * (1 - outputs[i][j]) * blame;
            }
        }

        //apply momentum
        for (int i = 1; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                for (int k = 0; k < weights[i][j].length; k++) {
                    weights[i][j][k] -= alpha * prevWeights[i][j][k];
                }
            }
        }

        //adjust weights
        for (int i = 1; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                for (int k = 0; k < nodesInLayers[i - 1]; k++) {
                    prevWeights[i][j][k] = beta * delta[i][j] * outputs[i - 1][k];
                    weights[i][j][k] += prevWeights[i][j][k];
                }
            }
        }




    }

    public double mse(double[] targets) {
        double mse = 0;
        for (int i = 0; i < targets.length; i++) {
            mse += (targets[i] - outputs[outputs.length - 1][i])
                    * (targets[i] - outputs[outputs.length - 1][i]);
        }
        return mse / 2;
    }

    public double[] getOutputs() {
        return outputs[numLayers - 1];
    }

    public double[][][] getWeights() {
        return weights;
    }

    public static void main(String[] args) {

        int[] layers = {2, 7, 7, 1};
        NeuronLayer nn = new NeuronLayer(layers);
        double[][][] data = {{{0, 0}, {0}},
                            {{0, 1}, {1}},
                            {{1, 0}, {1}},
                            {{1, 1}, {0}}};
        // Train the nn
        double mse = 0.11;
        int i = 0;
        while (mse > 0.084) {
            double ave_mse = 0;
            for (int j = 0; j < data.length; j++) {
                nn.bpgt(data[j][0], data[j][1]);
                ave_mse += nn.mse(data[j][1]);


            }
            ave_mse /= data.length;
            mse = ave_mse;

            System.out.print("MSE: ");
            System.out.println(mse);
            i++;
        }
        System.out.print("Trained in ");
        System.out.print(i);
        System.out.println(" iterations");

        // Test the nn
        for (int j = 0; j < data.length; j++) {
            nn.calcOutputs(data[j][0]);
            System.out.print("Input: ");
            //System.out.println(Arrays.deepToString(data[j]));
            System.out.print("Output:");
            System.out.println(nn.getOutputs()[0]);
        }
        


    }
}
