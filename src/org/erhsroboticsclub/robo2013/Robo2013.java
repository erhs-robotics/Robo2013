package org.erhsroboticsclub.robo2013;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import org.erhsroboticsclub.robo2013.utilities.MathX;
import org.erhsroboticsclub.robo2013.utilities.Messenger;

public class Robo2013 extends SimpleRobot {

    private RobotDrive drive;
    private Joystick stickL, stickR;
    private CANJaguar topLeftJaguar, bottomLeftJaguar, topRightJaguar, bottomRightJaguar;
    private Messenger msg;
    private LinearAccelerator launcher;
    private AI agent;
    private double launchAngle = RoboMap.LAUNCHER_LEVEL_ANGLE;
    private boolean dynamicMode = true;
    private boolean bumpingDown = false;
    private boolean bumpingUp = false;

    /**
     * Called once the cRIO boots up
     */
    public void robotInit() {

        msg = new Messenger();
        msg.printLn("Loading FRC 2013");
        try {
            topLeftJaguar = new CANJaguar(RoboMap.TOP_LEFT_DRIVE_MOTOR);
            bottomLeftJaguar = new CANJaguar(RoboMap.BOTTOM_LEFT_DRIVE_MOTOR);
            topRightJaguar = new CANJaguar(RoboMap.TOP_RIGHT_DRIVE_MOTOR);
            bottomRightJaguar = new CANJaguar(RoboMap.BOTTOM_RIGHT_DRIVE_MOTOR);

        } catch (CANTimeoutException ex) {
            msg.printLn("CAN network failed!");
            msg.printLn(ex.getMessage());
        }

        launcher = new LinearAccelerator();
        drive = new RobotDrive(topLeftJaguar, bottomLeftJaguar, topRightJaguar, bottomRightJaguar);
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
     * This function is called once each time the robot enters autonomous mode.
     */
    public void autonomous() {
        drive.setSafetyEnabled(false);        
        Watchdog.getInstance().setExpiration(Double.MAX_VALUE);
        Watchdog.getInstance().kill();        
        msg.clearConsole();
        msg.printLn("Auto Started");
        launcher.setAngle(RoboMap.AUTO_SHOOT_ANGLE);

        try {
            drive.setSafetyEnabled(false);            
            Watchdog.getInstance().kill();            
            //autonomousA();//start autonomous (Plan A)
            autonomousB();//start autonomous (Plan B)
            //autonomousC();//start autonomous (Plan C)
        } catch (Exception e) {
            msg.printLn("Auto mode failed!");
            msg.printLn(e.getMessage());
        }

    }

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
        drive.setSafetyEnabled(false);
        Watchdog.getInstance().kill();
        msg.clearConsole();        

        msg.printOnLn("Teleop Mode", RoboMap.STATUS_LINE);

        while (isEnabled() && isOperatorControl()) {
            double startTime = System.currentTimeMillis();
            
            /* Simple Tank Drive **********************************************/
            double moveValue = MathX.max(stickL.getY(), stickR.getY());
            drive.tankDrive(stickL.getY() * RoboMap.SPEED, 
                            stickR.getY() * RoboMap.SPEED);

            /* Fire the frisbee ***********************************************/
            if (stickL.getRawButton(RoboMap.FIRE_BUTTON)) {
                launcher.launch();
            }

            /* Set angle adjustment mode **************************************/
            if (stickR.getRawButton(RoboMap.DYNAMIC_ANGLE_BUTTON)) {
                dynamicMode = true;
            } else if (stickR.getRawButton(RoboMap.LEVEL_ANGLE_BUTTON)) {
                dynamicMode = false;
                launchAngle = RoboMap.LAUNCHER_LEVEL_ANGLE;
            } else if (stickR.getRawButton(RoboMap.NEAR_ANGLE_BUTTON)) {
                dynamicMode = false;
                launchAngle = RoboMap.LAUNCHER_NEAR_ANGLE;
            } else if (stickR.getRawButton(RoboMap.FAR_ANGLE_BUTTON)) {
                dynamicMode = false;
                launchAngle = RoboMap.LAUNCHER_FAR_ANGLE;
            }            

            /* Allow minute adjustments of the launcher ***********************/
            boolean button_down = stickL.getRawButton(RoboMap.BUMP_UP_BUTTON);
            if (button_down && !bumpingUp) {
                launcher.bumpUp();
                bumpingUp = true;
            } else if(!button_down) {
                bumpingUp = false;
            }
            
            button_down = stickL.getRawButton(RoboMap.BUMP_DOWN_BUTTON);
            if(button_down && !bumpingDown) {
                launcher.bumpDown();
                bumpingDown = true;
            } else if(!button_down){
                bumpingDown = false;
            }

            /* Set the launch angle *******************************************/
            if (dynamicMode) {
                launchAngle = MathX.map(stickR.getZ(), 1, -1, RoboMap.LAUNCHER_ANGLE_MIN,
                                        RoboMap.LAUNCHER_ANGLE_MAX);
            }
            if (stickR.getRawButton(RoboMap.FEED_ANGLE_BUTTON)) {
                launchAngle = RoboMap.LAUNCHER_FEED_ANGLE;
            }
            launcher.setAngle(launchAngle);
            
            /* Only adjust launcher if robot is not moving ********************/
            if (moveValue < 0.1) {
                launcher.adjustAngle();
            }
            
            /* Display launcher status ****************************************/
            double actualAngle = launcher.readAngle();
            double error = launchAngle - actualAngle;
            msg.printOnLn("angle: " + actualAngle, RoboMap.ANGLE_LINE);
            msg.printOnLn("setpt: " + launchAngle, RoboMap.SETPT_LINE);
            msg.printOnLn("error: " + error,       RoboMap.ERROR_LINE);

            while (System.currentTimeMillis() - startTime < RoboMap.UPDATE_FREQ) {
                //Do nothing
            }
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
        launcher.waitForAngle(5000);

        // 2) Fire all frisbees
        msg.printLn("Starting launch!");
        for (int i = 0; i < 3; i++) {
            launcher.setAngle(RoboMap.AUTO_SHOOT_ANGLE);
            msg.printLn("Launching disk " + (i + 1) + "...");
            launcher.launch();
        }
    }

}
