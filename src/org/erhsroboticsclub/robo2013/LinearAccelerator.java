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
    private AnalogChannel anglePotentiometer;
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
        pid.capOutput(-0.75, 0.75);
        
        try {
            primaryWheel = new CANJaguar(RoboMap.PRIMARY_LAUNCH_MOTOR);
            secondaryWheel = new CANJaguar(RoboMap.SECONDARY_LAUNCH_MOTOR);
            elevatorMotor = new CANJaguar(RoboMap.ELEVATOR_MOTOR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setWheels(double primary, double secondary) {
        try {
            primaryWheel.setX(primary);
            secondaryWheel.setX(secondary);
        } catch (CANTimeoutException e) {
        }
    }
    
    public void setWheels(double speed) {
        this.setWheels(speed, speed);
    }

    public void launch() {
        loadArmM1.setRaw(1);
        loadArmM2.setRaw(1);
        try {
            Thread.sleep(500);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        double time = Timer.getFPGATimestamp();
        while (true) {
            if (!limitSwitch.get()) {
                break;
            }
            // stops the launch if it has taken more than 10 seconds
            if (Timer.getFPGATimestamp() - time > 10000) {
                msg.printLn("Limit switch not found!");
                msg.printLn("Stopping launch...");
                break;
            }
        }
        loadArmM1.setRaw(127);
        loadArmM2.setRaw(127);
    }
    
    public void waitForAngle(double sleep) {
        double start = Timer.getFPGATimestamp();
        while(Timer.getFPGATimestamp() - start < sleep) {
            adjustAngle();            
        }
    }
    
    public void adjustAngle() {
        double setpoint = MathX.map(angle, 0, 35, 4.14, 4.75);
        double voltage = anglePotentiometer.getAverageVoltage();        
        pid.setSetpoint(setpoint);
        double correction = pid.doPID(voltage);
        
        try {
            elevatorMotor.setX(-correction);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
    
    public void setAngle(double angle) {
        this.angle = MathX.clamp(angle, 0, 30);
    }

    public double getAngle() {
        return angle;
    }
    
    public double gePOTasAngle() {
        double voltage = anglePotentiometer.getAverageVoltage();
        return MathX.map(voltage, 0, 5, 0, 35);
    }
}
