package org.erhsroboticsclub.robo2013;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import org.erhsroboticsclub.robo2013.utilities.MathX;
import org.erhsroboticsclub.robo2013.utilities.Messenger;
import org.erhsroboticsclub.robo2013.utilities.PIDControllerX;

public class LinearAccelerator {

    private CANJaguar primaryWheel;
    private CANJaguar secondaryWheel;
    private CANJaguar elevatorMotor;
    private PWM loadArmM1, loadArmM2;
    private DigitalInput limitSwitch;
    public AnalogChannel anglePotentiometer;
    private Messenger msg = new Messenger();
    private PIDControllerX pid;    
    private double angle = 31;
    
    public static final double AUTO_SHOOT_SPEED = -.8;    

    public LinearAccelerator() {
        loadArmM1 = new PWM(RoboMap.LOAD_ARM_MOTOR1);
        loadArmM2 = new PWM(RoboMap.LOAD_ARM_MOTOR2);        
        limitSwitch = new DigitalInput(RoboMap.LIMIT_SWITCH);
        anglePotentiometer = new AnalogChannel(RoboMap.LAUNCHER_ANGLE_POT);
        pid = new PIDControllerX(RoboMap.LAUNCHER_PID_P, RoboMap.LAUNCHER_PID_I, 
                                 RoboMap.LAUNCHER_PID_D);
        pid.capOutput(RoboMap.LAUNCH_PID_MIN, RoboMap.LAUNCH_PID_MAX);
        
        try {
            primaryWheel = new CANJaguar(RoboMap.PRIMARY_LAUNCH_MOTOR);
            secondaryWheel = new CANJaguar(RoboMap.SECONDARY_LAUNCH_MOTOR);
            elevatorMotor = new CANJaguar(RoboMap.ELEVATOR_MOTOR);
            //timeouts not needed for CAN, according to CD in 2012
            //primaryWheel.setExpiration(RoboMap.AUTO_SHOOT_TIMEOUT); 
            //secondaryWheel.setExpiration(RoboMap.AUTO_SHOOT_TIMEOUT);
            //elevatorMotor.setExpiration(RoboMap.AUTO_SHOOT_TIMEOUT);
            primaryWheel.setSafetyEnabled(false);
            secondaryWheel.setSafetyEnabled(false);
            elevatorMotor.setSafetyEnabled(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Sets the speed of both launch wheels separately
     * @param primary The speed of the first launch wheel
     * @param secondary The speed of the second launch wheel
     */
    public void setWheels(double primary, double secondary) {
        try {
            primaryWheel.setX(primary);
            secondaryWheel.setX(secondary);
        } catch (CANTimeoutException e) {
        }
    }
    
    /**
     * Sets both launch wheels to the same speed
     * @param speed The speed for both launch wheels
     */
    public void setWheels(double speed) {
        this.setWheels(speed, speed);
    }

    /**
     * Launches a Frisby
     */
    public void launch() {
        loadArmM1.setRaw(1);
        loadArmM2.setRaw(1);
        try {
            double start  = System.currentTimeMillis();
            while(System.currentTimeMillis() - start < 500) {
                //setWheels(LinearAccelerator.AUTO_SHOOT_SPEED);
                adjustAngle();
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        double time = System.currentTimeMillis();
        while (true) {
            if (!limitSwitch.get()) {
                break;
            }
            // stops the launch if it has taken more than 10 seconds
            if (System.currentTimeMillis() - time > 5000) {
                msg.printLn("Limit switch not found!");
                msg.printLn("Stopping launch...");
                break;
            }
            //setWheels(LinearAccelerator.AUTO_SHOOT_SPEED);
            adjustAngle();
        }
        loadArmM1.setRaw(127);
        loadArmM2.setRaw(127);
    }
    
    /**
     * Runs the PID loop for the specified amount of time
     * @param sleep The amount of time in milliseconds to run the PID 
     */
    public void waitForAngle(double sleep) {
        double start = System.currentTimeMillis();
        while(System.currentTimeMillis() - start < sleep) {            
            adjustAngle();
            //setWheels(LinearAccelerator.AUTO_SHOOT_SPEED);
        }
        msg.printLn("DONE!");
        
    }
    
    /**
     * Runs one iteration of the PID controller
     */
    public void adjustAngle() {
        double setpoint = MathX.map(angle, RoboMap.LAUNCHER_ANGLE_MIN,
                                    RoboMap.LAUNCHER_ANGLE_MAX, RoboMap.LAUNCHER_POT_MIN, 
                                    RoboMap.LAUNCHER_POT_MAX);
        double voltage = anglePotentiometer.getAverageVoltage();        
        pid.setSetpoint(setpoint);
        double correction = pid.doPID(voltage);
        
        try {
            elevatorMotor.setX(-correction);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
    
    
    
    /**
     * Sets the target angle. DOES NOT ACTUAL MOVE ANYTHING. The angle is
     * clamped between the min and max values
     * @param angle The new target angle
     */
    public void setAngle(double angle) {
        this.angle = MathX.clamp(angle, RoboMap.LAUNCHER_ANGLE_MIN, RoboMap.LAUNCHER_ANGLE_MAX);
    }
   
    public double getAngle() {
        return angle;
    }
    
    /**
     * Converts the pot value to an angle
     * @return  The pot value as an angle
     */
    public double getPOTasAngle() {
        double voltage = anglePotentiometer.getAverageVoltage();
        return MathX.map(voltage, RoboMap.LAUNCHER_POT_MIN,
                                  RoboMap.LAUNCHER_POT_MAX, 
                                  RoboMap.LAUNCHER_ANGLE_MIN, 
                                  RoboMap.LAUNCHER_ANGLE_MAX);
    }

}
