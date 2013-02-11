/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.erhsroboticsclub.robo2013;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import org.erhsroboticsclub.robo2013.utilities.MathX;

/**
 *
 * @author michael
 */
public class LinearAccelerator {
    private AnalogChannel pot = new AnalogChannel(RoboMap.POTENTIOMETER);    
    private CANJaguar primaryWheel;
    private CANJaguar secondaryWheel;
    private PWM loadArmM1, loadArmM2;
    private final double STOW = 2.5, DEPLOY = 4, KP = 10;
    

    public LinearAccelerator() { 
        loadArmM1 = new PWM(RoboMap.LOAD_ARM_MOTOR1);
        loadArmM2= new PWM(RoboMap.LOAD_ARM_MOTOR2);
        
        try {
            primaryWheel = new CANJaguar(RoboMap.PRIMARY_LAUNCH_MOTOR);
            secondaryWheel = new CANJaguar(RoboMap.SECONDARY_LAUNCH_MOTOR);
            
        } catch(Exception e) {            
            
        }        
    }
    
    public void setWheels(double primary, double secondary) {
        try {
            primaryWheel.setX(primary);
            secondaryWheel.setX(secondary);
        }catch (CANTimeoutException e) {
            
        }
    }
    
    public void launch() {
        double startTime = Timer.getFPGATimestamp();
        double setpoint = DEPLOY;
        while(true) {
            double time = Timer.getFPGATimestamp() - startTime;
            double value = pot.getAverageVoltage();
            double e = setpoint - value;
            double correction = KP * e;
            
            loadArmM1.setRaw((int)MathX.map(correction, -50, 50, 0, 255));
            loadArmM2.setRaw((int)MathX.map(correction, -50, 50, 0, 255));
            
            if((MathX.isWithin(value, setpoint, 0.1) && setpoint == DEPLOY)|| time == 2000) {
                startTime = Timer.getFPGATimestamp();
                setpoint = STOW;
            } else if(MathX.isWithin(value, setpoint, 0.1) && setpoint == STOW || time == 2000) {                
                break;
            }     
            
        }
        
    }  
    
}
