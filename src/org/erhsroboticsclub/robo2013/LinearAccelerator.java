package org.erhsroboticsclub.robo2013;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.Talon;
import org.erhsroboticsclub.robo2013.utilities.MathX;
import org.erhsroboticsclub.robo2013.utilities.Messenger;
import org.erhsroboticsclub.robo2013.utilities.PIDControllerX;

public class LinearAccelerator {

    private Talon primaryWheel;
    private Talon secondaryWheel;
    public Talon elevatorMotor;
    private PWM loadArmM1, loadArmM2;
    private DigitalInput limitSwitch;
    public  AnalogChannel pot;
    private Messenger msg = new Messenger();
    private PIDControllerX pid;
    private double angleSetpoint = 31;

    public LinearAccelerator() {
        loadArmM1 = new PWM(RoboMap.LOAD_ARM_MOTOR1);
        loadArmM2 = new PWM(RoboMap.LOAD_ARM_MOTOR2);
        limitSwitch = new DigitalInput(RoboMap.LIMIT_SWITCH);
        pot = new AnalogChannel(RoboMap.LAUNCHER_ACCEL);
        pot.setAverageBits(RoboMap.AVERAGING_BITS);
        pot.setOversampleBits(RoboMap.OVERSAMPLE_BITS);
        pid = new PIDControllerX(RoboMap.LAUNCHER_PID_P, RoboMap.LAUNCHER_PID_I,
                                 RoboMap.LAUNCHER_PID_D);
        pid.capOutput(RoboMap.LAUNCH_PID_MIN, RoboMap.LAUNCH_PID_MAX);
        primaryWheel = new Talon(RoboMap.PRIMARY_LAUNCH_MOTOR);
        secondaryWheel = new Talon(RoboMap.SECONDARY_LAUNCH_MOTOR);
        elevatorMotor = new Talon(RoboMap.ELEVATOR_MOTOR);
    }

    /**
     * Sets the speed of both launch wheels separately
     *
     * @param primary The speed of the first launch wheel
     * @param secondary The speed of the second launch wheel
     */
    public void setWheels(double primary, double secondary) {
        primaryWheel.set(primary);
        secondaryWheel.set(secondary);
    }

    /**
     * Sets both launch wheels to the same speed
     *
     * @param speed The speed for both launch wheels
     */
    public void setWheels(double speed) {
        this.setWheels(speed, speed);
    }

    /**
     * Launches a disk.  This function operates on the same Thread as the rest
     * of the code,  so the robot cannot be driven while it is trying to launch
     * a disk.  The launch has a timeout of 5 seconds.
     */
    public void launch() {
        setWheels(RoboMap.AUTO_SHOOT_SPEED);
        loadArmM1.setRaw(1);
        loadArmM2.setRaw(1);
        
        // This should no longer need to be try-catched with the move to the Talons
        try {
            double start = System.currentTimeMillis();
            while (System.currentTimeMillis() - start < 500) {
                setWheels(RoboMap.AUTO_SHOOT_SPEED);
                //adjustAngle();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        double time = System.currentTimeMillis();
        while (true) {
            if (!limitSwitch.get()) {
                break;
            }
            // stops the launch if it has taken more than 5 seconds
            if (System.currentTimeMillis() - time > 5000) {
                msg.printLn("Limit switch not found!");
                msg.printLn("Stopping launch...");
                break;
            }
            // this shouldn't be necessary with the move to the Talons
            setWheels(RoboMap.AUTO_SHOOT_SPEED);
            //adjustAngle();
        }
        loadArmM1.setRaw(127);
        loadArmM2.setRaw(127);
    }

    /**
     * Runs the PID loop for the specified amount of time
     *
     * @param sleep The amount of time in milliseconds to run the PID
     */
    public void waitForAngle(double sleep) {
        double start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < sleep) {
            adjustAngle();
            setWheels(RoboMap.AUTO_SHOOT_SPEED);
        }
        msg.printLn("DONE!");
    }

    /**
     * Runs one iteration of the PID controller
     */
    public void adjustAngle() {
        double currentAngle = readAngle();
        pid.setSetpoint(angleSetpoint);
        double correction = pid.doPID(currentAngle);
        elevatorMotor.set(correction);
    }

    /**
     * Sets the target angle. DOES NOT ACTUAL MOVE ANYTHING. The angle is
     * clamped between the min and max values established in RoboMap
     *
     * @param angle The new target angle
     */
    public void setAngleSetpoint(double angle) {
        this.angleSetpoint = MathX.clamp(angle, RoboMap.LAUNCHER_ANGLE_MIN, RoboMap.LAUNCHER_ANGLE_MAX);
    }

    /**
     * Returns the target angle.  THIS IS NOT THE ACTUAL ANGLE and may not even
     * be close to the actual angle.
     * 
     * @return The target angle
     */
    public double getAngleSetpoint() {
        return angleSetpoint;
    }

    /**
     * Converts the accelerometer voltage to degrees.  Returns the actual angle
     * of the launcher as measured by the potentiometer.
     *
     * @return The angle in degrees
     */
    public double readAngle() {
        double voltage = pot.getAverageVoltage();
        return MathX.map(voltage, RoboMap.VOLT_MIN, RoboMap.VOLT_MAX,
                RoboMap.LAUNCHER_ANGLE_MIN, RoboMap.LAUNCHER_ANGLE_MAX);
    }
}
