package org.erhsroboticsclub.robo2013;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.networktables2.util.List;
import java.util.Hashtable;
import org.erhsroboticsclub.robo2013.utilities.MathX;
import org.erhsroboticsclub.robo2013.utilities.Messenger;
import org.erhsroboticsclub.robo2013.utilities.PIDControllerX;
import org.erhsroboticsclub.robo2013.utilities.Target;

public class AI {

    private RobotDrive drive;
    private Messenger msg;
    private Com com;
    private PIDControllerX pid;
    private LinearAccelerator launcher;    

    public AI(RobotDrive drive, LinearAccelerator launcher) {
        this.drive = drive;
        this.launcher = launcher;
        pid = new PIDControllerX(1, 0, 10);
        pid.capOutput(-1, 1);
        com = new Com("http://10.0.53.23");
        msg = new Messenger();        
    }

    private List getAllTargets() {
        Hashtable table = com.getValues("crio");
        if (table == null) {
            msg.printLn("BeagleBoard not responding!");
            return null;
        }
        return com.parseTargets((String) table.get("targets"));
    }

    public Target getTopTarget() {
        List targets = getAllTargets();
        if (targets == null) {
            return null;
        }
        Target high = (Target) targets.get(0);
        for (int i = 0; i < targets.size(); i++) {
            Target t = (Target) targets.get(i);
            if (t.height > high.height) {
                high = t;
            }
        }
        return high;
    }
    
    /**
     * Predicts a launch "angle" for the top target given the distance from the
     * robot to the top target. This function uses a cubic regression of 
     * empirical data to predict the angle.  
     * @param x The distance from the robot to the top target
     * @return The predicted launch "angle" using a cubic regression
     */
    public double distToLaunchAngle(double x) {
        double a = -.02221847131638;
        double b = 1.4830979608858;
        double c = -19.368452007211;
        double d = 90.161131786061;
        double x3 = MathX.pow(x, 3);
        double x2 = MathX.pow(x, 2);
        return a*x3 + b*x2 + c*x + d;
    }
    
    /**
     * Sets the launch angle for the top target assuming the top target is in
     * the field of view.
     * @return true if function was successful, false if otherwise
     */
    public boolean autoAimLauncher() {        
        Target target = getTopTarget();
        
        if(target == null) return false;
        
        double aot = distToLaunchAngle(target.distance);
        launcher.setAngle(aot);
        
        return true;
    }

    public boolean autoAimLauncher(int t) {
        List list = getAllTargets();
        if (list == null) {
            return false;
        }
        if (t >= list.size()) {
            msg.printLn("No target '" + t + "'");
            return false;
        }
        Target target = (Target) list.get(t);
        
        double aot = distToLaunchAngle(target.distance);
        launcher.setAngle(aot);
        return true;
    }
}
