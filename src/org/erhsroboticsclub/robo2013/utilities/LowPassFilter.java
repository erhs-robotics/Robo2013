/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.erhsroboticsclub.robo2013.utilities;

/**
 *
 * @author michael
 */
public class LowPassFilter {

    public double[] memory;
    private double[] w;
    private double w_sum;

    public void setMAMemory(double[] memory) {
        this.memory = new double[memory.length];
        System.arraycopy(memory, 0, this.memory, 0, memory.length);
        this.w = new double[memory.length + 1];
        w_sum = 0;
        for (int i = 0; i < w.length; i++) {
            w[i] = 0.5 * (1.0 / (w.length - i));
            w_sum += w[i];
        }

    }

    private void move(double x, int i) {
        double buf = memory[i];
        memory[i] = x;
        if (i == 0) return;        
        move(buf, i - 1);
    }

    public double MA(double x) {
        double sum = x, count = 0;
        for (int i = 0; i < memory.length; i++) {
            count++;
            sum += memory[i];
        }
        move(x, memory.length - 1);
        return sum / (memory.length + 1);
    }

    public double WMA(double x) {
        double sum = x * w[w.length - 1];
        for (int i = 0; i < memory.length; i++) {
            sum += memory[i] * w[i];
        }
        move(x, memory.length - 1);
        return sum / w_sum;
    }
}
