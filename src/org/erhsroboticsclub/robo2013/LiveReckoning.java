package org.erhsroboticsclub.robo2013;

import edu.wpi.first.wpilibj.RobotDrive;
import org.erhsroboticsclub.robo2013.utilties.Messenger;

public class LiveReckoning {
    
    RobotDrive drive;
    Messenger msg;
    
    public LiveReckoning(RobotDrive drive) {
        this.drive = drive;
    }
    
}
