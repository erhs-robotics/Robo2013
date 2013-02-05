package org.erhsroboticsclub.robo2013.utilities.neuralNet;


import com.sun.squawk.util.Assert;
import java.util.Random;

public class NeuronLayer {

    private double[][][] weights; //to next layer
    private double[][][] prevWeights;
    private double[][] delta;
    private double[][] outputs;//The output for each neuron in each layer
    private int numLayers;
    private int[] nodesInLayers;

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

        weights = new double[numLayers - 1][][];// -1 for input layer
        prevWeights = new double[numLayers - 1][][];
        Random r = new Random();
        for (int i = 0; i < numLayers - 1; i++) {
            weights[i] = new double[layers[i + 1]][layers[i] + 1];//+1 for bias
            for (int j = 0; j < layers[i] + 1; j++) {
                //assign an init weight from [-0.5, 0.5]
                weights[i][layers[i + 1] - 1][j] = r.nextDouble() - 0.5;
            }
            prevWeights[i] = new double[layers[i + 1]][layers[i]];
        }

        //check output array
        Assert.always(outputs.length == layers.length);
        for (int i = 0; i < layers.length; i++) {
            Assert.always(outputs[i].length == layers[i]);
        }
        //check delta array
        Assert.always(delta.length == layers.length);
        for (int i = 0; i < layers.length; i++) {
            Assert.always(delta[i].length == layers[i]);
        }
        //check weights
        Assert.always(weights.length == layers.length - 1);
        for (int i = 0; i < layers.length - 1; i++) {
            Assert.always(weights[i].length == layers[i + 1]);
            for (int j = 0; j < layers[i+1]; j++) {
                for (int k = 0; k < layers[i]; k++) {
                    Assert.always(weights[i][j][k] <= 1);
                    //System.out.println(weights[i][j][k]);
                    Assert.always(weights[i][j][k] >= -1);
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
                    sum += outputs[i - 1][k] * weights[i - 1][j][k];
                }
                //apply bias
                sum += weights[i - 1][j][nodesInLayers[i - 1]];
                outputs[i][j] = Utils.sigmoid(sum);                
            }

        }

    }

    public double[] getOutputs() {
        return outputs[numLayers - 1];
    }
   
}
