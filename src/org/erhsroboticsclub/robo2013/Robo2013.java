package org.erhsroboticsclub.robo2013;

import com.sun.squawk.imp.ImpGlobal;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import org.erhsroboticsclub.robo2013.imaging.ImageProcessing;
import org.erhsroboticsclub.robo2013.utilities.Controls;
import org.erhsroboticsclub.robo2013.utilities.Messenger;

public class Robo2013 extends IterativeRobot {
    
    RobotDrive drive;
    Joystick sticky;
    CANJaguar TOP_LEFT_JAGUAR, BOTTOM_LEFT_JAGUAR, TOP_RIGHT_JAGUAR, BOTTOM_RIGHT_JAGUAR;
    Messenger msg;
    Controls driveControls;
    AxisCamera leftCam, rightCam;
    
    public void robotInit() {
        msg = new Messenger();
        msg.printLn("Loading FRC 2013");
        try {
            TOP_LEFT_JAGUAR = new CANJaguar(RoboMap.TOP_LEFT_MOTOR);
            BOTTOM_LEFT_JAGUAR = new CANJaguar(RoboMap.BOTTOM_LEFT_MOTOR);
            TOP_RIGHT_JAGUAR = new CANJaguar(RoboMap.TOP_RIGHT_MOTOR);
            BOTTOM_RIGHT_JAGUAR = new CANJaguar(RoboMap.BOTTOM_RIGHT_MOTOR);
            leftCam = AxisCamera.getInstance("10.0.53.11");
            rightCam = AxisCamera.getInstance("10.0.53.12");
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        drive = new RobotDrive(TOP_LEFT_JAGUAR, BOTTOM_LEFT_JAGUAR, TOP_RIGHT_JAGUAR, BOTTOM_RIGHT_JAGUAR);
        sticky = new Joystick(1);
        driveControls = new Controls(sticky);
        msg.printLn("Done Loading: FRC 2013");

    }

    public void autonomousInit() {
        msg.clearConsole();
        msg.printLn("Starting Autonomous");
        ImageProcessing imgProc = new ImageProcessing();
        msg.printLn("Calculating distance");
        double distance = imgProc.getDistance(leftCam, rightCam);
        msg.printLn("Distance = " + distance);
        
        
    }
    
    

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        //drive.arcadeDrive(sticky);
        if (driveControls.FOV_Top()) {
            drive.drive(.75, .75);
        }
        else if (driveControls.FOV_Bottom()) {
            drive.drive(-.75, -.75);
        }
        else if (driveControls.FOV_Left()) {
            drive.drive(-.75, .75);
        }
        else if (driveControls.FOV_Right()) {
            drive.drive(.75, -.75);
        }
        else {
            drive.arcadeDrive(sticky);
        }
        
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}
