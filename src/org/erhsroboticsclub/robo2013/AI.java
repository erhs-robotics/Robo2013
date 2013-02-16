package org.erhsroboticsclub.robo2013;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.networktables2.util.List;
import java.util.Hashtable;
import org.erhsroboticsclub.robo2013.utilities.Messenger;
import org.erhsroboticsclub.robo2013.utilities.Target;

public class AI {
    
    private RobotDrive drive;
    private Messenger msg;
    private Com com;
    
    public AI(RobotDrive drive) {
        this.drive = drive;
    }
    private List getAllTargets() {
        Hashtable table = com.getValues("crio");
        return com.parseTargets((String)table.get("targets"));        
    }
    
    public Target getTopTarget() {        
        List targets = getAllTargets();
        Target high = (Target)targets.get(0);
        for(int i=0;i<targets.size();i++) {
            Target t = (Target)targets.get(i);
            if(t.height > high.height) {
                high = t;
            }        
        }
        
        return high;        
    }
    
    public void turnToTarget() {
        
    }
    
}
