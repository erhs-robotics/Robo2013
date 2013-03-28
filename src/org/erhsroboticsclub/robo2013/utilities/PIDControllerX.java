package org.erhsroboticsclub.robo2013.utilities;

import edu.wpi.first.wpilibj.Timer;

/**
 *
 * @author michael
 */
public class PIDControllerX {
    private double Kp, Ki, Kd, setpoint;
    private double lastError, totalError, lastTime;
    private boolean firstrun;
    private double min = 0, max = 0;    

    public PIDControllerX(double Kp, double Ki, double Kd) {
        this.Kp = Kp;
        this.Ki = Ki;
        this.Kd = Kd;
        firstrun = true;
        this.lastError = 0;
        this.totalError = 0;
        this.lastTime = 0;
    }

    public void setSetpoint(double setpoint) {
        this.setpoint = setpoint;
    }
    
    public void tune(double Kp, double Ki, double Kd) {
        this.Kp = Kp;
        this.Ki = Ki;
        this.Kd = Kd;
    }
    
    public void capOutput(double min, double max) {
        this.min = min;
        this.max = max;
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
        
        if(min != max) {
            correction = MathX.clamp(correction, min, max);
        }
        
        return correction;
    }

    public double getKd() {
        return Kd;
    }

    public double getKi() {
        return Ki;
    }

    public double getKp() {
        return Kp;
    }

    public double getSetpoint() {
        return setpoint;
    }
    
    public void reset() {
        firstrun = true;
        this.lastError = 0;
        this.totalError = 0;
        this.lastTime = 0;
    }
}