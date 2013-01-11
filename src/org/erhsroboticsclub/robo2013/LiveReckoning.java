package org.erhsroboticsclub.robo2013;

import edu.wpi.first.wpilibj.RobotDrive;
import org.erhsroboticsclub.robo2013.utilities.Messenger;

public class LiveReckoning {
    
    RobotDrive drive;
    Messenger msg;
    
    public LiveReckoning(RobotDrive drive) {
        this.drive = drive;
    }
    
}
