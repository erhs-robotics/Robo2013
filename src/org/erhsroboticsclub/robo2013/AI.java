package org.erhsroboticsclub.robo2013;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.networktables2.util.List;
import java.util.Hashtable;
import org.erhsroboticsclub.robo2013.utilities.MathX;
import org.erhsroboticsclub.robo2013.utilities.Messenger;
import org.erhsroboticsclub.robo2013.utilities.NeuralNet;
import org.erhsroboticsclub.robo2013.utilities.PIDControllerX;
import org.erhsroboticsclub.robo2013.utilities.Target;

public class AI {

    private RobotDrive drive;
    private Messenger msg;
    private Com com;
    private PIDControllerX pid;
    private LinearAccelerator launcher;
    private NeuralNet nn;

    public AI(RobotDrive drive, LinearAccelerator launcher) {
        this.drive = drive;
        this.launcher = launcher;
        pid = new PIDControllerX(1, 0, 10);
        com = new Com("http://10.0.53.23");
        msg = new Messenger();
        /* Neural Network Layers ****/
        // 0) Input Layer - (distance to target), (height of target)
        // 1) Hidden Layer - 7 nodes
        // 2) Hidden Layer - 7 nodes
        // 3) Output Layer - (angle of attack in percent)
        int[] layers = {2, 7, 7, 1};
        nn = new NeuralNet(layers);
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

    public boolean turnToTarget(int t) {
        pid.setSetpoint(320);

        Target target = new Target(0, 0, 0);

        do {
            List list = getAllTargets();
            if (list != null) {
                if (t > list.size() - 1) {
                    msg.printLn("No target '" + t + "'");
                    return false;
                }
                target = (Target) list.get(t);
                double correction = pid.doPID(target.x);
                drive.tankDrive(correction, -correction);
            } else {
                return false;
            }
        } while (!MathX.isWithin(target.x, 320, 7));
        pid.reset();
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

        double[] input = {target.distance, target.height};
        nn.calcOutputs(input);
        double aot = nn.getOutputs()[0];
        launcher.setAngle(aot);
        return true;
    }
}
