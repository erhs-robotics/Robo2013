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
        msg.printLn("CALIBRATOR");
        
        launcher = new LinearAccelerator();     
        
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

        while (isEnabled() && isOperatorControl()) {
            double startTime = System.currentTimeMillis();
           
            double angle = launcher.readAngle();            
            msg.printOnLn("Ave volt: " + launcher.pot.getAverageVoltage(), DriverStationLCD.Line.kUser1);
            msg.printOnLn("angle: " + angle, DriverStationLCD.Line.kUser2);        

            double y = stickR.getY();
            if(y > 0.7) {
                launcher.elevatorMotor.set(0.3);
            } else if (y < -0.7) {
                launcher.elevatorMotor.set(-0.3);
            } else {
                launcher.elevatorMotor.set(0);
            }        

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
        if (modePot.getAverageVoltage() < 2.5) {
            msg.printLn("Mode: side");
            launcher.setAngleSetpoint(RoboMap.LAUNCHER_TOP_SIDE_ANGLE);
        } else {
            msg.printLn("Mode: center");
            launcher.setAngleSetpoint(RoboMap.LAUNCHER_TOP_CENTER_ANGLE);
        }
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
