package org.erhsroboticsclub.robo2013;

import edu.wpi.first.wpilibj.*;
import org.erhsroboticsclub.robo2013.utilities.MathX;
import org.erhsroboticsclub.robo2013.utilities.Messenger;

public class Robo2013 extends SimpleRobot {

    private RobotDrive drive;
    private Joystick stickL, stickR;
    private Messenger msg;
    private LinearAccelerator launcher;
    private AnalogChannel modePot;
    private double launchAngle = 0;

    /**
     * Called once the cRIO boots up
     */
    public void robotInit() {
        msg = new Messenger();
        msg.printLn("Loading FRC 2013");
        modePot = new AnalogChannel(RoboMap.MODE_POT);
        launcher = new LinearAccelerator();
        drive = new RobotDrive(new Talon(RoboMap.TOP_LEFT_DRIVE_MOTOR),
                new Talon(RoboMap.BOTTOM_LEFT_DRIVE_MOTOR),
                new Talon(RoboMap.TOP_RIGHT_DRIVE_MOTOR),
                new Talon(RoboMap.BOTTOM_RIGHT_DRIVE_MOTOR));
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
        stickL = new Joystick(RoboMap.LEFT_DRIVE_STICK);
        stickR = new Joystick(RoboMap.RIGHT_DRIVE_STICK);
        msg.printLn("Done: FRC 2013");
    }

    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    public void autonomous() {
        Watchdog.getInstance().setExpiration(Double.MAX_VALUE); // really not necessary
        killSafety();
        msg.clearConsole();
        msg.printLn("Auto Started");
        launcher.setAngleSetpoint(RoboMap.LAUNCHER_TOP_CENTER_ANGLE);

        try {
            killSafety(); // probably don't need to kill the safety twice
            autonomousProcedure();
        } catch (Exception e) {
            msg.printLn("Autonomous Failed");
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
        // TODO: Make this index start at 0 instead of -1.  
        // Seriously, who thought this was a good idea.
        int adjMode = -1; // -1 - dynamic, 0 - top, 1 - feeding, 2 - bumping
        boolean bumpingButtonDown;
        boolean bumpingDown = false;
        boolean bumpingUp = false;
        boolean bumpingLeft = false;
        boolean bumpingRight = false;
        double lastZValue = 0;
        boolean topButtonDown = false;
        boolean feedButtonDown = false;
        boolean shooterOn = true;
        double launchSpeed = RoboMap.AUTO_SHOOT_SPEED;
        String[] modeStrings = {"Dynamic", "Top", "Feeding", "Bumping"};

        while (isEnabled() && isOperatorControl()) {
            double startTime = System.currentTimeMillis();
            launcher.setWheels(launchSpeed);
            double actualAngle = launcher.readAngle();
            msg.printOnLn("Teleop Mode", RoboMap.STATUS_LINE);
            msg.printOnLn("Angle Mode: " + modeStrings[adjMode + 1], RoboMap.ANGLE_MODE_LINE);
            msg.printOnLn("Ave volt: " + launcher.pot.getAverageVoltage(), RoboMap.VOLTAGE_LINE);
            msg.printOnLn("angle: " + actualAngle, RoboMap.ANGLE_LINE);
            msg.printOnLn("setp: " + launcher.getAngleSetpoint(), RoboMap.SETPOINT_LINE);
            msg.printOnLn("error: " + (launchAngle - actualAngle), RoboMap.ERROR_LINE);

            /* Simple Tank Drive **********************************************/
            drive.tankDrive(stickL.getY() * RoboMap.SPEED,
                    stickR.getY() * RoboMap.SPEED);

            /* Fire the frisbee ***********************************************/
            if (stickL.getRawButton(RoboMap.FIRE_BUTTON)) {
                launcher.launch();
            }

            /* Set boolean launch value ***************************************/
            if (stickR.getRawButton(RoboMap.LAUNCHER_OFF_BUTTON)) {
                launchSpeed = 0;
            } else if (stickR.getRawButton(RoboMap.LAUNCHER_ON_BUTTON)) {
                launchSpeed = RoboMap.AUTO_SHOOT_SPEED;
            }

            /* Allow minute adjustments of the launcher ***********************/
            bumpingButtonDown = stickL.getRawButton(RoboMap.BUMP_UP_BUTTON);
            if (bumpingButtonDown && !bumpingUp) {
                adjMode = 2;
                launchAngle += 0.5;
                bumpingUp = true;
            } else if (!bumpingButtonDown) {
                bumpingUp = false;
            }

            bumpingButtonDown = stickL.getRawButton(RoboMap.BUMP_DOWN_BUTTON);
            if (bumpingButtonDown && !bumpingDown) {
                adjMode = 2;
                launchAngle -= 0.5;
                bumpingDown = true;
            } else if (!bumpingButtonDown) {
                bumpingDown = false;
            }

            /* Allow minute adjustments of the drive train ********************/
            bumpingButtonDown = stickL.getRawButton(RoboMap.BUMP_DRIVE_LEFT);
            if (bumpingButtonDown && !bumpingLeft) {
                drive.tankDrive(-RoboMap.SPEED, RoboMap.SPEED);
                try {
                    Thread.sleep(RoboMap.BUMP_TIME);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                drive.tankDrive(0, 0);
                bumpingLeft = true;
            } else if (!bumpingButtonDown) {
                bumpingLeft = false;
            }

            bumpingButtonDown = stickL.getRawButton(RoboMap.BUMP_DRIVE_RIGHT);
            if (bumpingButtonDown && !bumpingRight) {
                drive.tankDrive(RoboMap.SPEED, -RoboMap.SPEED);
                try {
                    Thread.sleep(RoboMap.BUMP_TIME);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                drive.tankDrive(0, 0);
                bumpingRight = true;
            } else if (!bumpingButtonDown) {
                bumpingRight = false;
            }

            /* Set angle adjustment mode **************************************/
            if (lastZValue != stickR.getZ()) {
                adjMode = -1;
            }

            if (stickR.getRawButton(RoboMap.TOP_ANGLE_BUTTON) && !topButtonDown) {
                adjMode = 0;
                topButtonDown = true;
            } else if (!stickR.getRawButton(RoboMap.TOP_ANGLE_BUTTON) && topButtonDown) {
                topButtonDown = false;
            }

            if (stickR.getRawButton(RoboMap.FEED_ANGLE_BUTTON) && !feedButtonDown) {
                adjMode = 1;
                feedButtonDown = true;
            } else if (!stickR.getRawButton(RoboMap.FEED_ANGLE_BUTTON) && feedButtonDown) {
                feedButtonDown = false;
            }

            /* Set the launch angle *******************************************/
            lastZValue = stickR.getZ();

            switch (adjMode) {
                case -1:
                    launchAngle = MathX.map(stickR.getZ(), 1, -1,
                            RoboMap.LAUNCHER_ANGLE_MIN,
                            30);
                    launchAngle = MathX.clamp(launchAngle, RoboMap.LAUNCHER_ANGLE_MIN,
                            RoboMap.LAUNCHER_ANGLE_MAX);
                    break;
                case 0:
                    launchAngle = RoboMap.LAUNCHER_TOP_CENTER_ANGLE;
                    break;
                case 1:
                    launchAngle = RoboMap.LAUNCHER_FEED_ANGLE;
                    break;
                case 2:
                    //Do nothing
                    break;
                default:// Should not get here
                    msg.printLn("Invalid launch mode of " + adjMode + "!");
                    msg.printLn("Reseting to 0...");
                    adjMode = 0;
                    break;
            }

            /* Adjust the angle with the PID Controller ***********************/
            launcher.setAngleSetpoint(launchAngle);
            launcher.adjustAngle();

            /* Set the loop frequency *****************************************/
            while (System.currentTimeMillis() - startTime < RoboMap.UPDATE_FREQ) {
                //Do nothing
            }
        }
    }

    /**
     * Autonomous Procedure to be called from the autonomous() method.
     */
    private void autonomousProcedure() {
        /* 0) Set the wheels to proper speed **********************************/
        msg.printLn("Start launcher");
        launcher.setWheels(RoboMap.AUTO_SHOOT_SPEED);

        /* 1) Set the launch angle dependent on starting position *************/
        
        launcher.setAngleSetpoint(RoboMap.LAUNCHER_TOP_SIDE_ANGLE);
        
        msg.printLn("Angle: " + launcher.getAngleSetpoint());
        launcher.waitForAngle(5000);

        /* 2) Fire all frisbees ***********************************************/
        msg.printLn("Launch!");
        for (int i = 0; i < 3; i++) {
            double error = launcher.getAngleSetpoint() - launcher.readAngle();
            launcher.setWheels(RoboMap.AUTO_SHOOT_SPEED);
            msg.printLn("Launch disk " + (i + 1));
            msg.printLn("Angle error: " + error);
            launcher.launch();
        }
    }

    /**
     * Kills the various safety elements of the WPILIB that usually result in a
     * non-functioning robot.
     */
    private void killSafety() {
        drive.setSafetyEnabled(false);
        Watchdog.getInstance().kill();
    }
}
