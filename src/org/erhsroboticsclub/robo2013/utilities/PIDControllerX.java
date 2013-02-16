/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.erhsroboticsclub.robo2013.utilities;

import edu.wpi.first.wpilibj.Timer;

/**
 *
 * @author michael
 */
public class PIDControllerX {
    private double Kp, Ki, Kd, setpoint;
    private double lastError, totalError, lastTime;
    private boolean firstrun = true;
    

    public PIDControllerX(double Kp, double Ki, double Kd) {
        this.Kp = Kp;
        this.Ki = Ki;
        this.Kd = Kd;
        this.lastError = 0;
        this.totalError = 0;
        this.lastTime = 0;
    }

    public void setSetpoint(double setpoint) {
        this.setpoint = setpoint;
    }   
    
    public double doPID(double value) {
        double error = setpoint - value;
        double correction;
        
        if(firstrun) {
            correction = Kp * error;           
            firstrun = false;
        } else {
            double de = error - lastError;
            double dt = Timer.getFPGATimestamp() - lastTime;
            correction = Kp * error + Ki * totalError + Kd * (de/dt);
            totalError += error * dt;            
        }
        lastError = error;
        
        lastTime = Timer.getFPGATimestamp();      
        
        return correction;
    }
    
    public void reset() {
        firstrun = true;
    }
}
