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
        com = new Com("http://10.0.53.23");
    }

    private List getAllTargets() {
        Hashtable table = com.getValues("crio");
        return com.parseTargets((String) table.get("targets"));
    }

    public Target getTopTarget() {
        List targets = getAllTargets();
        Target high = (Target) targets.get(0);
        for (int i = 0; i < targets.size(); i++) {
            Target t = (Target) targets.get(i);
            if (t.height > high.height) {
                high = t;
            }
        }
        return high;
    }

    public void turnToTarget(int t) {
        pid.setSetpoint(320);

        Target target;

        do {
            List list = getAllTargets();
            target = (Target) list.get(t);
            double correction = pid.doPID(target.x);
            drive.tankDrive(correction, -correction);
        } while (!MathX.isWithin(target.x, 320, 7));
    }

    public void autoAimLauncher() {
        // ToDo: Auto aim launcher angle
    }
}
