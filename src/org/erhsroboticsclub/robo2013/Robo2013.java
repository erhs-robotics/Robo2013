package org.erhsroboticsclub.robo2013;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import org.erhsroboticsclub.robo2013.utilities.MathX;
import org.erhsroboticsclub.robo2013.utilities.Messenger;

public class Robo2013 extends IterativeRobot {

    private RobotDrive drive;
    private Joystick stickL, stickR;
    private CANJaguar TOP_LEFT_JAGUAR, BOTTOM_LEFT_JAGUAR, TOP_RIGHT_JAGUAR, BOTTOM_RIGHT_JAGUAR;
    private Messenger msg;
    private LinearAccelerator launcher;
    private AI agent;
    private int angleFlag = 0; // 0 - dynamic, 1 - feeder angle, 2 - level (0 deg)
    private double launchAngle = RoboMap.LAUNCHER_LEVEL_ANGLE;


    /*
     * Called once the cRIO boots up
     */
    public void robotInit() {
        msg = new Messenger();
        msg.printLn("Loading FRC 2013");
        try {
            TOP_LEFT_JAGUAR = new CANJaguar(RoboMap.TOP_LEFT_DRIVE_MOTOR);
            BOTTOM_LEFT_JAGUAR = new CANJaguar(RoboMap.BOTTOM_LEFT_DRIVE_MOTOR);
            TOP_RIGHT_JAGUAR = new CANJaguar(RoboMap.TOP_RIGHT_DRIVE_MOTOR);
            BOTTOM_RIGHT_JAGUAR = new CANJaguar(RoboMap.BOTTOM_RIGHT_DRIVE_MOTOR);
        } catch (CANTimeoutException ex) {
            msg.printLn("CAN network failed!");
            msg.printLn(ex.getMessage());
        }

        launcher = new LinearAccelerator();
        drive = new RobotDrive(TOP_LEFT_JAGUAR, BOTTOM_LEFT_JAGUAR, TOP_RIGHT_JAGUAR, BOTTOM_RIGHT_JAGUAR);
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
        stickL = new Joystick(RoboMap.LEFT_DRIVE_STICK);
        stickR = new Joystick(RoboMap.RIGHT_DRIVE_STICK);
        agent = new AI(drive, launcher);
        msg.printLn("Done Loading: FRC 2013");
    }

    /**
     * Called once at the start of autonomous mode
     */
    public void autonomousInit() {
        drive.setSafetyEnabled(false);
        Watchdog.getInstance().kill();
        msg.clearConsole();
        msg.printLn("Auto Started");

        try {
            //autonomousA();//start autonomous (Plan A)
            autonomousB();//start autonomous (Plan B)
            //autonomousC();//start autonomous (Plan C)
        } catch (Exception e) {
            msg.printLn("Auto mode failed!");
            msg.printLn(e.getMessage());
        }
    }

    /**
     * Plan A autonomous Called once by autonomousInit
     */
    private void autonomousA() throws Exception {
        msg.printLn("Autonomous A:");
        int fails = 0;
        boolean success;

        // 0) Set wheels to proper speed
        msg.printLn("Starting up launcher...");
        launcher.setWheels(LinearAccelerator.AUTO_SHOOT_SPEED);

        // 1) Auto aim launcher
        msg.printLn("Aiming launcher...");
        do {
            if (!isAutonomous()) {
                throw new Exception("Ran out of time!");
            }
            success = agent.autoAimLauncher();
            if (!success) {
                msg.printLn("turnToTarget failed!");
                fails++;
            }
            if (fails > 500) {
                msg.printLn("Giving up...");
                break;
            } else {
                msg.printLn("Retrying...");
            }
        } while (!success);

        // 2) Wait for motors to come up to speed
        msg.printLn("Waiting for motors...");
        Timer.delay(5);

        // 3) Fire all frisbees
        msg.printLn("Starting launch!");
        for (int i = 0; i < 3; i++) {
            msg.printLn("Launching disk " + (i + 1) + "...");
            launcher.launch();
        }
    }

    /**
     * Plan B autonomous Called once by autonomousInit
     */
    private void autonomousB() {
        msg.printLn("Autonomous B:");
        // 0) Set the wheels to proper speed
        msg.printLn("Starting up launcher...");
        launcher.setWheels(LinearAccelerator.AUTO_SHOOT_SPEED);
        // 1) Set the launch angle
        msg.printLn("Setting angle to " + RoboMap.AUTO_SHOOT_ANGLE + "...");
        launcher.setAngle(RoboMap.AUTO_SHOOT_ANGLE);
        launcher.waitForAngle(2000);
        // 2) Wait for motors to come up to speed
        msg.printLn("Waiting for motors...");
        Timer.delay(5);
        // 3) Fire all frisbees
        msg.printLn("Starting launch!");
        for (int i = 0; i < 3; i++) {
            msg.printLn("Launching disk " + (i + 1) + "...");
            launcher.launch();
        }
        // 4) Lower launcher
        msg.printLn("Lowering launcher...");
        launcher.setAngle(RoboMap.LAUNCHER_LEVEL_ANGLE);
        launcher.waitForAngle(1500);
        // 5) Back up out of pyramid
        drive.drive(RoboMap.AUTO_MOVE_SPEED, 0);
        Timer.delay(RoboMap.AUTO_BACKUP_TIME);
        drive.drive(0, 0);
    }

    /**
     * Plan C autonomous Called once by autonomousInit
     */
    private void autonomousC() {
        msg.printLn("Autonomous C:");
        // 0) Set the wheels to the proper speed
        msg.printLn("Starting up launcher...");
        launcher.setWheels(LinearAccelerator.AUTO_SHOOT_SPEED);
        // 1) move a specific distance to the target?
        // 2) Fire all frisbees
        msg.printLn("Starting Launch");
        for (int i = 0; i < 3; i++) {
            msg.printLn("Launching Disk " + (i + 1));
            launcher.launch();
        }
    }

    /*
     * Called once at the start of teleop mode
     */
    public void teleopInit() {
        drive.setSafetyEnabled(false);
        msg.clearConsole();
        msg.printLn("Teleop Started");
    }

    /**
     * Called periodically during operator control
     */
    public void teleopPeriodic() {
        launcher.setWheels(LinearAccelerator.AUTO_SHOOT_SPEED);
        /* Simple Tank Drive **************************************************/
        double moveFactor = MathX.abs(stickL.getY()) + MathX.abs(stickR.getY());
        drive.tankDrive(stickL.getY() * RoboMap.SPEED, stickR.getY() * RoboMap.SPEED);

        /* Fire the frisbee ***************************************************/
        if (stickL.getRawButton(RoboMap.FIRE_BUTTON)) {
            launcher.launch();
        }

        /* Set angle adjustment mode ******************************************/
        if (stickR.getRawButton(RoboMap.DYNAMIC_ANGLE_BUTTON)) {
            angleFlag = 0;// dynamic
        } else if (stickR.getRawButton(RoboMap.LEVEL_ANGLE_BUTTON)) {
            angleFlag = 1;// level (0 deg)
            launchAngle = RoboMap.LAUNCHER_LEVEL_ANGLE;
        } else if (stickR.getRawButton(RoboMap.NEAR_ANGLE_BUTTON)) {
            angleFlag = 1;// infront of the pyramic angle
            launchAngle = RoboMap.LAUNCHER_NEAR_ANGLE;
        } else if (stickR.getRawButton(RoboMap.FAR_ANGLE_BUTTON)) {
            angleFlag = 1;// behind the pyramid angle
            launchAngle = RoboMap.LAUNCHER_FAR_ANGLE;
        }

        /* Setting the launch angle *******************************************/
        switch (angleFlag) {
            case 0:                
                if (stickR.getRawButton(RoboMap.FEED_ANGLE_BUTTON)) {
                    launchAngle = RoboMap.LAUNCHER_FEED_ANGLE;
                } else {
                    launchAngle = MathX.map(stickR.getZ(), 1, -1, RoboMap.LAUNCHER_ANGLE_MIN,
                            RoboMap.LAUNCHER_ANGLE_MAX);
                }                
                
                break;
            case 1:
                launcher.setAngle(angleFlag);
            default:
                angleFlag = 0;//should not reach here
                break;
        }
        
        launcher.setAngle(launchAngle);
        if(moveFactor <= 0.1) launcher.adjustAngle();
        
        msg.printOnLn("Angle: " + launcher.getAngle(), RoboMap.ANGLE_LINE);
    }
}
