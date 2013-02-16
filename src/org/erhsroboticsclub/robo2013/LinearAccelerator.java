/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.erhsroboticsclub.robo2013;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import org.erhsroboticsclub.robo2013.utilities.MathX;
import org.erhsroboticsclub.robo2013.utilities.Messenger;

/**
 *
 * @author michael
 */
public class LinearAccelerator {       
    private CANJaguar primaryWheel;
    private CANJaguar secondaryWheel;
    public PWM loadArmM1, loadArmM2;    
    public DigitalInput limitSwitch;
    private Messenger msg = new Messenger();
    

    public LinearAccelerator() { 
        loadArmM1 = new PWM(RoboMap.LOAD_ARM_MOTOR1);
        loadArmM2 = new PWM(RoboMap.LOAD_ARM_MOTOR2);
        limitSwitch = new DigitalInput(RoboMap.LIMIT_SWITCH);
        /*
        try {
            primaryWheel = new CANJaguar(RoboMap.PRIMARY_LAUNCH_MOTOR);
            secondaryWheel = new CANJaguar(RoboMap.SECONDARY_LAUNCH_MOTOR);
            
        } catch(Exception e) {            
            
        }  
        */
    }
    
    public void setWheels(double primary, double secondary) {
        try {
            primaryWheel.setX(primary);
            secondaryWheel.setX(secondary);
        }catch (CANTimeoutException e) {
            
        }
    }
    
    public void launch() {                
        loadArmM1.setRaw(1);
        loadArmM2.setRaw(1);
        
        
        try {
            Thread.sleep(500);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        while(true) {
            if(!limitSwitch.get()) {
                break;
            }
            msg.printLn("looping");
        }
        
        loadArmM1.setRaw(127);
        loadArmM2.setRaw(127);        
        
    }  
    
}
