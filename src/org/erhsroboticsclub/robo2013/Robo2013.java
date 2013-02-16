package org.erhsroboticsclub.robo2013;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import java.util.Hashtable;
import org.erhsroboticsclub.robo2013.utilities.Controls;
import org.erhsroboticsclub.robo2013.utilities.MathX;
import org.erhsroboticsclub.robo2013.utilities.Messenger;

public class Robo2013 extends IterativeRobot {

    private RobotDrive drive;
    private Joystick stickL, stickR;
    private CANJaguar TOP_LEFT_JAGUAR, BOTTOM_LEFT_JAGUAR, TOP_RIGHT_JAGUAR, BOTTOM_RIGHT_JAGUAR;
    private Messenger msg;
    private Controls controls;
    private LinearAccelerator launcher;
    private final double speed = 0.5;
    private int mode = 0;
    private AI agent;


    /*
     * Called once cRIO boots up
     */
    public void robotInit() {
        msg = new Messenger();
        msg.printLn("Loading FRC 2013");
        /*
         try {
         TOP_LEFT_JAGUAR = new CANJaguar(RoboMap.TOP_LEFT_MOTOR);
         BOTTOM_LEFT_JAGUAR = new CANJaguar(RoboMap.BOTTOM_LEFT_MOTOR);
         TOP_RIGHT_JAGUAR = new CANJaguar(RoboMap.TOP_RIGHT_MOTOR);
         BOTTOM_RIGHT_JAGUAR = new CANJaguar(RoboMap.BOTTOM_RIGHT_MOTOR);

         } catch (CANTimeoutException ex) {
         ex.printStackTrace();
         }
         */
        launcher = new LinearAccelerator();
        //drive = new RobotDrive(TOP_LEFT_JAGUAR, BOTTOM_LEFT_JAGUAR, TOP_RIGHT_JAGUAR, BOTTOM_RIGHT_JAGUAR);
        stickL = new Joystick(1);
        stickR = new Joystick(1);
        agent = new AI(null);
        msg.printLn("Done Loading: FRC 2013");

    }

    /*
     * Called once at the start of autonomous mode
     */
    public void autonomousInit() {
        drive.setSafetyEnabled(false);
        msg.clearConsole();




        autonomous();//start autonomous
    }

    /*
     * Called once by autonomousInit
     */
    public void autonomous() {
        // 0) turn to face target
        // 1) auto aim launcher
        // 2) wait for motor to come up to speed
        // 3) fire all firsbees
    }

    /*
     * Called once at the start of teleop mode
     */
    public void teleopInit() {
    }

    /**
     * Called periodically during operator control
     */
    public void teleopPeriodic() {
        System.out.println(launcher.limitSwitch.get());
        //drive.tankDrive(stickL.getY() * speed, stickR.getY() * speed);   
        if (stickR.getRawButton(RoboMap.AUTO_AIM_BUTTON)) {
        }

        if (stickR.getRawButton(RoboMap.MANUAL_LAUNCHER_UP_BUTTON)) {
        } else if (stickR.getRawButton(RoboMap.MANUAL_LAUNCHER_DOWN_BUTTON)) {
        }
        //double value = stickL.getY();
        //value = MathX.map(value, -1, 1, 0, 255);
        //msg.printLn("" + value);
        
        //launcher.loadArmM1.setRaw((int)value);
        //launcher.loadArmM2.setRaw((int)value);

        if (stickL.getRawButton(RoboMap.FIRE_BUTTON)) {
            msg.printLn("Launch!");
            launcher.launch();
        }
    }
}
