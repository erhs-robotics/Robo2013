package org.erhsroboticsclub.robo2013.utilities;

import edu.wpi.first.wpilibj.AnalogChannel;

/**
 * Uses an accelerometer to calculate angle in the y-direction
 * 
 * @author nick
 */
public class Angleometer {
    
    private AnalogChannel analogChannel;
    private double voltageMin, voltageMax;
    
    public Angleometer(int analogChannel) {
        this.analogChannel = new AnalogChannel(analogChannel);
    }
    
    public Angleometer(int analogChannel, double voltageMin, double voltageMax) {
        this(analogChannel);
        this.voltageMin = voltageMin;
        this.voltageMax = voltageMax;
    }
    
    public double getAngle() {
        return MathX.asin(MathX.map(analogChannel.getAverageVoltage(),
                                    voltageMin, voltageMax, 0, 1));
    }
    
    
}
