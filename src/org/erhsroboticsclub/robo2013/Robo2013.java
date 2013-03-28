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
    private boolean isAtFeedAngle = false;

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
    
    private void autoBackup() {
        return;/*
        drive.tankDrive(-1, -1);
        Timer.delay(RoboMap.AUTO_BACKUP_TIME);
        drive.tankDrive(0, 0);*/
    }

    /**
     * Plan A autonomous
     * Called once by autonomousInit
     */
    private void autonomousA() throws Exception {
        autoBackup();
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
     * Plan B autonomous
     * Called once by autonomousInit
     */
    private void autonomousB() {
        autoBackup();
        msg.printLn("Autonomous B:");
        // 0) Set the wheels to proper speed
        msg.printLn("Starting up launcher...");
        launcher.setWheels(LinearAccelerator.AUTO_SHOOT_SPEED);
        // 1) Set the launch angle
        msg.printLn("Setting angle to " + RoboMap.AUTO_SHOOT_ANGLE + "...");
        launcher.setAngle(RoboMap.AUTO_SHOOT_ANGLE);
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
     * Plan C autonomous
     * Called once by autonomousInit
     */
    private void autonomousC() {
        autoBackup();
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
        drive.tankDrive(stickL.getY() * RoboMap.SPEED, stickR.getY() * RoboMap.SPEED);

        /* Auto aim laincher **************************************************/
        if (stickR.getRawButton(RoboMap.AUTO_AIM_BUTTON)) {
            agent.autoAimLauncher();
        }      

        /* Fire the frisbee ***************************************************/
        if (stickL.getRawButton(RoboMap.FIRE_BUTTON)) {
            launcher.launch();
        }
        
        /* Set feed angle *****************************************************/
        if(stickR.getRawButton(4)) {
            isAtFeedAngle = true;
        } else if(stickR.getRawButton(5)) {
            isAtFeedAngle = false;
        }
    

        /* Setting the launch angle *******************************************/
        if (isAtFeedAngle) {
            launcher.setAngle(RoboMap.LAUNCHER_FEED_ANGLE);
        } else {
            double angle = MathX.map(stickR.getZ(), 1, -1, RoboMap.LAUNCHER_ANGLE_MIN, 
                                                           RoboMap.LAUNCHER_ANGLE_MAX);
            launcher.setAngle(angle);
            msg.printLn("" + angle);
        }

        launcher.adjustAngle();
        System.out.println(launcher.anglePotentiometer.getAverageVoltage());
        //msg.printOnLn("Angle: " + launcher.getAngle(), RoboMap.ANGLE_LINE);
    }
}
