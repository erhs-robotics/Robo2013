package org.erhsroboticsclub.robo2013;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import org.erhsroboticsclub.robo2013.utilities.MathX;
import org.erhsroboticsclub.robo2013.utilities.Messenger;

public class Robo2013 extends SimpleRobot {

    private RobotDrive drive;
    private Joystick stickL, stickR;
    private CANJaguar TOP_LEFT_JAGUAR, BOTTOM_LEFT_JAGUAR, TOP_RIGHT_JAGUAR, BOTTOM_RIGHT_JAGUAR;
    private Messenger msg;
    private LinearAccelerator launcher;
    private AI agent;
    private int angleFlag = 0; // 0 - dynamic, 1 - feeder angle, 2 - level (0 deg)
    private double launchAngle = RoboMap.LAUNCHER_LEVEL_ANGLE;    

    /**
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
     * This function is called once each time the robot enters autonomous mode.
     */
    public void autonomous() {        
        drive.setSafetyEnabled(false);
        msg.printLn("setSafetyEnabled call 0 autonomous(): " + drive.isSafetyEnabled()); //debug
        Watchdog.getInstance().setExpiration(Double.MAX_VALUE);
        Watchdog.getInstance().kill();
        msg.printLn("Watchdog alive call 0 autonomous(): " + Watchdog.getInstance().isAlive()); //debug
        msg.clearConsole();
        msg.printLn("Auto Started");
        launcher.setAngle(RoboMap.AUTO_SHOOT_ANGLE);

        try {
            drive.setSafetyEnabled(false);
            msg.printLn("setSafetyEnabled call 1 autonomous(): " + drive.isSafetyEnabled()); //debug
            Watchdog.getInstance().kill();
            msg.printLn("Watchdog alive call 1 autonomous(): " + Watchdog.getInstance().isAlive()); //debug
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
        //debug
        msg.printLn("setSafetyEnabled call 0 operatorControl(): " + drive.isSafetyEnabled());
        msg.printLn("Watchdog alive call 0 autonomous(): " + Watchdog.getInstance().isAlive());
         //end debug
        msg.printLn("Teleop Started");
        
        while (isEnabled() && isOperatorControl()) {
            launcher.setWheels(LinearAccelerator.AUTO_SHOOT_SPEED);
            double start = System.currentTimeMillis();
            
            /* Simple Tank Drive **************************************************/
            double moveValue = MathX.max(stickL.getY(), stickR.getY());
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
            if(moveValue < 0.1) launcher.adjustAngle();

            msg.printOnLn("Angle: " + launcher.getAngle(), RoboMap.ANGLE_LINE);
            
            
            while(System.currentTimeMillis() - start < RoboMap.UPDATE_FREQ) {
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
