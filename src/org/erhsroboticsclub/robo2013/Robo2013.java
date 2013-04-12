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
    private double launchAngle = 0;
    
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
        launcher.setAngle(RoboMap.LAUNCHER_TOP_CENTER_ANGLE);

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
        int adjMode = -1; // -1 - dynamic, 0 - top, 1 - feeding, 2 - bumping
        int last_adjMode = -1;
        boolean bumpingDown = false;
        boolean bumpingUp = false;
        boolean bumpingLeft = false;
        boolean bumpingRight = false;
        double lastZValue = 0;        
        boolean topButtonDown = false;
        boolean feedButtonDown = false;
        String[] modeStrings = {"Dynamic", "Top", "Feeding", "Bumping"};

        while (isEnabled() && isOperatorControl()) {
            double startTime = System.currentTimeMillis();
            launcher.setWheels(RoboMap.AUTO_SHOOT_SPEED);
            double actualAngle = launcher.readAngle();
            msg.printOnLn("Teleop Mode", RoboMap.STATUS_LINE);
            msg.printOnLn("Angle Mode: " + modeStrings[adjMode + 1], RoboMap.ANGLE_MODE_LINE);            
            msg.printOnLn("Ave volt: " + launcher.pot.getAverageVoltage(), RoboMap.VOLTAGE_LINE);
            msg.printOnLn("angle: " + actualAngle, RoboMap.ANGLE_LINE);
            msg.printOnLn("setp: " + launcher.getAngle(), RoboMap.SETPOINT_LINE);
            msg.printOnLn("error: " + (launchAngle - actualAngle), RoboMap.ERROR_LINE);            

            /* Simple Tank Drive **********************************************/           
            drive.tankDrive(stickL.getY() * RoboMap.SPEED,
                            stickR.getY() * RoboMap.SPEED);           

            /* Fire the frisbee ***********************************************/
            if (stickL.getRawButton(RoboMap.FIRE_BUTTON)) {                
                launcher.launch();
            }

            /* Allow minute adjustments of the launcher ***********************/
            boolean button_down = stickL.getRawButton(RoboMap.BUMP_UP_BUTTON);
            if (button_down && !bumpingUp) {                
                adjMode = 2;
                launchAngle += 0.5;
                bumpingUp = true;
            } else if (!button_down) {
                bumpingUp = false;
            }

            button_down = stickL.getRawButton(RoboMap.BUMP_DOWN_BUTTON);
            if (button_down && !bumpingDown) {                
                adjMode = 2;
                launchAngle -= 0.5;
                bumpingDown = true;
            } else if (!button_down) {
                bumpingDown = false;
            }
            
            /* Allow minute adjustments of the drive train ********************/
            button_down = stickL.getRawButton(RoboMap.BUMP_DRIVE_LEFT);
            if(button_down && !bumpingLeft) {
                drive.tankDrive(-RoboMap.SPEED, RoboMap.SPEED);
                try {
                    Thread.sleep(RoboMap.BUMP_TIME);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                drive.tankDrive(0, 0);
                bumpingLeft = true;
            } else if(!button_down) {
                bumpingLeft = false;
            }
            
            button_down = stickL.getRawButton(RoboMap.BUMP_DRIVE_RIGHT);
            if(button_down && !bumpingRight) {
                drive.tankDrive(RoboMap.SPEED, -RoboMap.SPEED);
                try {
                    Thread.sleep(RoboMap.BUMP_TIME);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                drive.tankDrive(0, 0);
                bumpingRight = true;
            } else if(!button_down) {
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
                                            RoboMap.LAUNCHER_ANGLE_MAX);
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
            launcher.setAngle(launchAngle);
            launcher.adjustAngle();            

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
        msg.printLn("Start launcher");
        launcher.setWheels(RoboMap.AUTO_SHOOT_SPEED);
        /* 1) Set the launch angle ********************************************/
        msg.printLn("Angle: " + RoboMap.LAUNCHER_TOP_CENTER_ANGLE);
        launcher.setAngle(RoboMap.LAUNCHER_TOP_CENTER_ANGLE);
        launcher.waitForAngle(5000);
        /* 2) Fire all frisbees ***********************************************/
        msg.printLn("Launch!");
        for (int i = 0; i < 3; i++) {
            launcher.setWheels(RoboMap.AUTO_SHOOT_SPEED);
            msg.printLn("Launch disk " + (i + 1));
            launcher.launch();
        }
    }

}
