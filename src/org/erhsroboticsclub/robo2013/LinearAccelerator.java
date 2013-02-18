package org.erhsroboticsclub.robo2013;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import org.erhsroboticsclub.robo2013.utilities.Messenger;

public class LinearAccelerator {

    private CANJaguar primaryWheel;
    private CANJaguar secondaryWheel;
    private CANJaguar elevatorMotor;
    public PWM loadArmM1, loadArmM2;
    public DigitalInput limitSwitch;
    public AnalogChannel anglePotentiometer;
    private Messenger msg = new Messenger();
    
    public final double AUTO_SHOOT_SPEED = .8;
    private final double BUMP_TIME = .3;

    public LinearAccelerator() {
        loadArmM1 = new PWM(RoboMap.LOAD_ARM_MOTOR1);
        loadArmM2 = new PWM(RoboMap.LOAD_ARM_MOTOR2);
        limitSwitch = new DigitalInput(RoboMap.LIMIT_SWITCH);
        anglePotentiometer = new AnalogChannel(RoboMap.LAUNCHER_ANGLE_POT);

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
            if(Timer.getFPGATimestamp() - time > 10000) {
                msg.printLn("Limit switch not found!");
                msg.printLn("Stopping launch...");
                break;
            }
        }
        loadArmM1.setRaw(127);
        loadArmM2.setRaw(127);
    }

    // not done
    public void setAngle(double angle) {
        double voltage = anglePotentiometer.getAverageVoltage();
        while (true) {;
        }
    }

    // done, untested
    public void bumpLauncherUp() {
        try {
            elevatorMotor.setX(1);
            Timer.delay(BUMP_TIME);
            elevatorMotor.setX(0);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    // done, untested
    public void bumpLauncherDown() {
        try {
            elevatorMotor.setX(-1);
            Timer.delay(BUMP_TIME);
            elevatorMotor.setX(0);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
}
