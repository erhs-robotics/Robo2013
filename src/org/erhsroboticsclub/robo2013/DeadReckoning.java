package org.erhsroboticsclub.robo2013;

import edu.wpi.first.wpilibj.RobotDrive;
import org.erhsroboticsclub.robo2013.utilities.Messenger;

public class DeadReckoning {
    
    RobotDrive drive;
    Messenger msg = new Messenger();
    
    public DeadReckoning(RobotDrive drive) {
        this.drive = drive;
    }
    
}
