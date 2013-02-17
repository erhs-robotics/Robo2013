package org.erhsroboticsclub.robo2013;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import org.erhsroboticsclub.robo2013.utilities.Messenger;

public class Robo2013 extends IterativeRobot {

    private RobotDrive drive;
    private Joystick stickL, stickR;
    private CANJaguar TOP_LEFT_JAGUAR, BOTTOM_LEFT_JAGUAR, TOP_RIGHT_JAGUAR, BOTTOM_RIGHT_JAGUAR;
    private Messenger msg;
    private LinearAccelerator launcher;
    private AI agent;
    private final double speed = 0.5;

    /*
     * Called once cRIO boots up
     */
    public void robotInit() {
        msg = new Messenger();
        msg.printLn("Loading FRC 2013");
        try {
            TOP_LEFT_JAGUAR = new CANJaguar(RoboMap.TOP_LEFT_DRIVE_MOTOR);
            BOTTOM_LEFT_JAGUAR = new CANJaguar(RoboMap.BOTTOM_LEFT_DRIVE_MOTOR);
            TOP_RIGHT_JAGUAR = new CANJaguar(RoboMap.TOP_RIGHT_DRIVE_MOTOR);
            BOTTOM_RIGHT_JAGUAR = new CANJaguar(RoboMap.BOTTOM_RIGHT_DRIVE_MOTOR);
        } catch (CANTimeoutException ex) {
            msg.printLn("CAN network failed!");
            msg.printLn(ex.getMessage());
        }
        launcher = new LinearAccelerator();
        drive = new RobotDrive(TOP_LEFT_JAGUAR, BOTTOM_LEFT_JAGUAR, TOP_RIGHT_JAGUAR, BOTTOM_RIGHT_JAGUAR);
        stickL = new Joystick(RoboMap.LEFT_DRIVE_STICK);
        stickR = new Joystick(RoboMap.RIGHT_DRIVE_STICK);
        agent = new AI(drive, launcher);
        msg.printLn("Done Loading: FRC 2013");
    }

    /*
     * Called once at the start of autonomous mode
     */
    public void autonomousInit() {
        drive.setSafetyEnabled(false);
        msg.clearConsole();
        msg.printLn("Auto Started");
        
        try {
            autonomous();//start autonomous
        } catch(Exception e) {
            msg.printLn("Auto mode failed!");
            msg.printLn(e.getMessage());
        }
    }

    /*
     * Called once by autonomousInit
     */
    public void autonomous() {
        int targetNumber = 1; //should be the top
        
        // 0) set wheels to proper speed
        launcher.setWheels(launcher.AUTO_SHOOT_SPEED, launcher.AUTO_SHOOT_SPEED);
        // 1) turn to face target
        agent.turnToTarget(targetNumber);
        // 2) auto aim launcher
        agent.autoAimLauncher();
        // 3) wait for motor to come up to speed
        Timer.delay(5);
        // 4) fire all frisbees
        for (int i = 0; i < 3; i++) {
            launcher.launch();
        }       
    }

    /*
     * Called once at the start of teleop mode
     */
    public void teleopInit() {
        drive.setSafetyEnabled(false);
        msg.clearConsole();
        msg.printLn("Teleop Started");
    }

    /**
     * Called periodically during operator control
     */
    public void teleopPeriodic() {
        /* Simple Tank Drive **************************************************/
        drive.tankDrive(stickL.getY() * speed, stickR.getY() * speed); 

        /* Adjust shooting angle **********************************************/
        if (stickR.getRawButton(RoboMap.AUTO_AIM_BUTTON)) {
            agent.autoAimLauncher();
        }
        if (stickR.getRawButton(RoboMap.MANUAL_LAUNCHER_UP_BUTTON)) {
            launcher.bumpLauncherUp();
        } else if (stickR.getRawButton(RoboMap.MANUAL_LAUNCHER_DOWN_BUTTON)) {
            launcher.bumpLauncherDown();
        }

        /* Auto Turn To Target ************************************************/
        if (stickR.getRawButton(RoboMap.TURN_TO_TARGET_BUTTON)) {
            // Needs adjustment, need a way to specify which target we
            // are actually turning to
            agent.turnToTarget(0);
        }

        /* Fire the frisbee ***************************************************/
        if (stickL.getRawButton(RoboMap.FIRE_BUTTON)) {            
            launcher.launch();
        }
    }
}
