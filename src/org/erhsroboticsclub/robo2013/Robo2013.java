package org.erhsroboticsclub.robo2013;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import org.erhsroboticsclub.robo2013.utilities.Messenger;

public class Robo2013 extends IterativeRobot {
    
    RobotDrive drive;
    Joystick sticky;
    Messenger msg;
    CANJaguar TOP_LEFT_JAGUAR, BOTTOM_LEFT_JAGUAR, TOP_RIGHT_JAGUAR, BOTTOM_RIGHT_JAGUAR;
    
    public void robotInit() {
        msg = new Messenger();
        msg.printLn("Loading FRC 2013");
        try {
            TOP_LEFT_JAGUAR = new CANJaguar(RoboMap.TOP_LEFT_MOTOR);
            BOTTOM_LEFT_JAGUAR = new CANJaguar(RoboMap.BOTTOM_LEFT_MOTOR);
            TOP_RIGHT_JAGUAR = new CANJaguar(RoboMap.TOP_RIGHT_MOTOR);
            BOTTOM_RIGHT_JAGUAR = new CANJaguar(RoboMap.BOTTOM_RIGHT_MOTOR);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        drive = new RobotDrive(TOP_LEFT_JAGUAR, BOTTOM_LEFT_JAGUAR, TOP_RIGHT_JAGUAR, BOTTOM_RIGHT_JAGUAR);
        sticky = new Joystick(1);
        msg.printLn("Done Loading: FRC 2013");

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
        drive.arcadeDrive(sticky);
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}
