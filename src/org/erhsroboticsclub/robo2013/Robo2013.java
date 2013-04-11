package org.erhsroboticsclub.robo2013;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import org.erhsroboticsclub.robo2013.utilities.MathX;
import org.erhsroboticsclub.robo2013.utilities.Messenger;

public class Robo2013 extends SimpleRobot {

    private RobotDrive drive;
    private Joystick stickL, stickR;
    private CANJaguar topLeftJag, bottomLeftJag, topRightJag, bottomRightJag;
    private Messenger msg;
    private LinearAccelerator launcher;
    private AI agent;
    private double launchAngle = RoboMap.LAUNCHER_LEVEL_ANGLE;

    /**
     * Called once the cRIO boots up
     */
    public void robotInit() {

        msg = new Messenger();
        msg.printLn("Loading FRC 2013");
        try {
            topLeftJag = new CANJaguar(RoboMap.TOP_LEFT_DRIVE_MOTOR);
            bottomLeftJag = new CANJaguar(RoboMap.BOTTOM_LEFT_DRIVE_MOTOR);
            topRightJag = new CANJaguar(RoboMap.TOP_RIGHT_DRIVE_MOTOR);
            bottomRightJag = new CANJaguar(RoboMap.BOTTOM_RIGHT_DRIVE_MOTOR);

        } catch (CANTimeoutException ex) {
            msg.printLn("CAN network failed!");
            msg.printLn(ex.getMessage());
        }

        launcher = new LinearAccelerator();
        drive = new RobotDrive(topLeftJag, bottomLeftJag, topRightJag, bottomRightJag);
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
        int adjMode = -1; // -1 - dynamic, 0 - level, 1 - medium, 2 - high
        boolean bumpingDown = false;
        boolean bumpingUp = false;
        double lastZValue = 0;
        boolean firing = false;
        boolean triggerDown = false;
        boolean raiseButtonDown = false;
        boolean lowerButtonDown = false;
        String[] modeStrings = {"Dynamic", "Level", "Medium", "High"};

        while (isEnabled() && isOperatorControl()) {
            double startTime = System.currentTimeMillis();
            msg.printOnLn("Teleop Mode", RoboMap.STATUS_LINE);
            msg.printOnLn("Angle Mode: " + modeStrings[adjMode + 1], RoboMap.ANGLE_LINE);
            double actualAngle = launcher.readAngle();

            /* Simple Tank Drive **********************************************/
            double moveValue = MathX.max(stickL.getY(), stickR.getY());
            drive.tankDrive(stickL.getY() * RoboMap.SPEED,
                            stickR.getY() * RoboMap.SPEED);

            /* Disable shoot mode if we are driving ***************************/
            if (moveValue > 0.5) {                
                launcher.setWheels(0);
                firing = false;
            }

            /* Fire the frisbee ***********************************************/
            if (stickL.getRawButton(RoboMap.FIRE_BUTTON) && !firing) {
                launcher.setWheels(RoboMap.AUTO_SHOOT_SPEED);
                firing = true;
                triggerDown = true;
            } else if (!stickL.getRawButton(RoboMap.FIRE_BUTTON) && firing) {
                triggerDown = false;
            } else if (stickL.getRawButton(RoboMap.FIRE_BUTTON) && firing && !triggerDown) {
                launcher.launch();
            }

            /* Set angle adjustment mode **************************************/
            if (lastZValue != stickR.getZ()) {
                adjMode = -1;
            }

            if (stickR.getRawButton(RoboMap.RAISE_ANGLE_BUTTON) && !raiseButtonDown) {
                if (adjMode == -1 || adjMode == 3) {
                    double d1 = RoboMap.LAUNCHER_MED_ANGLE - actualAngle,
                            d2 = RoboMap.LAUNCHER_HIGH_ANGLE - actualAngle;
                    d1 = d1 < 0 ? Double.POSITIVE_INFINITY : d1;
                    adjMode = MathX.min(d1, d2) == d1 ? 1 : 2;
                } else {
                    adjMode = (int) MathX.clamp(++adjMode, 0, 2);
                }
                raiseButtonDown = true;
            } else if (!stickR.getRawButton(RoboMap.RAISE_ANGLE_BUTTON) && raiseButtonDown) {
                raiseButtonDown = false;
            }
            
            if (stickR.getRawButton(RoboMap.LOWER_ANGLE_BUTTON) && !lowerButtonDown) {
                if (adjMode == -1 || adjMode == 3) {
                    double d0 = actualAngle - RoboMap.LAUNCHER_LEVEL_ANGLE,
                            d1 = actualAngle - RoboMap.LAUNCHER_MED_ANGLE;
                    d1 = d1 < 0 ? Double.POSITIVE_INFINITY : d1;
                    adjMode = MathX.min(d0, d1) == d0 ? 0 : 1;
                } else {
                    adjMode = (int) MathX.clamp(--adjMode, 0, 2);
                }
                raiseButtonDown = true;
            } else if (!stickR.getRawButton(RoboMap.LOWER_ANGLE_BUTTON) && lowerButtonDown) {
                lowerButtonDown = false;
            }

            /* Allow minute adjustments of the launcher ***********************/
            boolean button_down = stickL.getRawButton(RoboMap.BUMP_UP_BUTTON);
            if (button_down && !bumpingUp) {
                launcher.bumpUp();
                bumpingUp = true;
            } else if (!button_down) {
                bumpingUp = false;
            }

            button_down = stickL.getRawButton(RoboMap.BUMP_DOWN_BUTTON);
            if (button_down && !bumpingDown) {
                launcher.bumpDown();
                bumpingDown = true;
            } else if (!button_down) {
                bumpingDown = false;
            }

            /* Set the launch angle *******************************************/
            lastZValue = stickR.getZ();

            switch (adjMode) {
                case -1:
                    launchAngle = MathX.map(stickR.getZ(), 1, -1, 
                                            RoboMap.LAUNCHER_ANGLE_MIN,
                                            RoboMap.LAUNCHER_ANGLE_MAX);
                    break;
                case 0:
                    launchAngle = RoboMap.LAUNCHER_LEVEL_ANGLE;
                    break;
                case 1:
                    launchAngle = RoboMap.LAUNCHER_MED_ANGLE;
                    break;
                case 2:
                    launchAngle = RoboMap.LAUNCHER_HIGH_ANGLE;
                    break;
                default:// Should not get here
                    msg.printLn("Invalid launch mode of " + adjMode + "!");
                    msg.printLn("Reseting to 0...");
                    adjMode = 0;
                    break;
            }

            if (stickR.getRawButton(RoboMap.FEED_ANGLE_BUTTON)) {
                launchAngle = RoboMap.LAUNCHER_FEED_ANGLE;
            }

            launcher.setAngle(launchAngle);

            /* Only adjust launcher if robot is not moving nor firing *********/
            if (moveValue < 0.1 && !firing) {
                launcher.adjustAngle();
            }

            /* Set the loop frequency *****************************************/
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
        launcher.setWheels(RoboMap.AUTO_SHOOT_SPEED);

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
        /* 0) Set the wheels to proper speed **********************************/
        msg.printLn("Starting up launcher...");
        launcher.setWheels(RoboMap.AUTO_SHOOT_SPEED);
        /* 1) Set the launch angle ********************************************/
        msg.printLn("Setting angle to " + RoboMap.AUTO_SHOOT_ANGLE + "...");
        launcher.setAngle(RoboMap.AUTO_SHOOT_ANGLE);
        launcher.waitForAngle(5000);
        /* 2) Fire all frisbees ***********************************************/
        msg.printLn("Starting launch!");
        for (int i = 0; i < 3; i++) {
            launcher.setAngle(RoboMap.AUTO_SHOOT_ANGLE);
            msg.printLn("Launching disk " + (i + 1) + "...");
            launcher.launch();
        }
    }

}