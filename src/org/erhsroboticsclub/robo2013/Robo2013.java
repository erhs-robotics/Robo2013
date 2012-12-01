package org.erhsroboticsclub.robo2013;

import org.erhsroboticsclub.robo2013.utilties.Messenger;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;

public class Robo2013 extends IterativeRobot {
    
    Messenger msg;
    
    RobotDrive drive;
    Joystick leftDriveStick, rightDriveStick, controlStick;
    

    public void robotInit() {
        msg = new Messenger();
        msg.printLn("Loading Robot Code - Please Wait...");
        
        drive = new RobotDrive(RoboMap.TOP_LEFT_MOTOR, RoboMap.BOTTOM_LEFT_MOTOR,
                               RoboMap.TOP_RIGHT_MOTOR, RoboMap.BOTTOM_RIGHT_MOTOR);
        leftDriveStick = new Joystick(RoboMap.LEFT_DRIVE_STICK);
        rightDriveStick = new Joystick(RoboMap.RIGHT_DRIVE_STICK);
        controlStick = new Joystick(RoboMap.CONTROL_STICK);

        msg.printLn("Done Loading: FRC 2013");
    }
    
    public void teleopPeriodic() {
        
        drive.tankDrive(leftDriveStick, rightDriveStick);
        
    }

    public void autonomousPeriodic() {

    }
    
}
