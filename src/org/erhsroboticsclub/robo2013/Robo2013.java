package org.erhsroboticsclub.robo2013;


import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import java.util.Hashtable;
import org.erhsroboticsclub.robo2013.utilities.Controls;
import org.erhsroboticsclub.robo2013.utilities.Messenger; 

public class Robo2013 extends IterativeRobot {

    private RobotDrive drive;
    private Joystick stickL, stickR;
    private CANJaguar TOP_LEFT_JAGUAR, BOTTOM_LEFT_JAGUAR, TOP_RIGHT_JAGUAR, BOTTOM_RIGHT_JAGUAR;
    private Messenger msg;
    private Controls controls;
    private LinearAccelerator launcher;
    private Com com;
    private final double speed = 0.5; 
    
    
    
    

    public void robotInit() {
        
        
        msg = new Messenger();
        com = new Com("http://10.0.53.23:80/");
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
        msg.printLn("Done Loading: FRC 2013");



    }

    public void autonomousInit() {
        drive.setSafetyEnabled(false);

        msg.clearConsole();
        while (true) {
            try {
                drive.drive(0, 0);                
                
                msg.printLn("Connecting...");
                Hashtable table = com.getValues("crio");
                
                System.out.println("received: " + table.get("status"));                
                
                msg.printLn("Success!");

            } catch (Exception ex) {
                System.out.println(ex.getClass());
                System.out.println(ex.getMessage());
                System.out.println(ex.toString());
                msg.printLn("Connection Failed!");
                break;
            }
        }
        

    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    }

    public void teleopInit() {
        
    }

    
    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        System.out.println(launcher.limitSwitch.get());
        //drive.tankDrive(stickL.getY() * speed, stickR.getY() * speed);   
            if (stickR.getRawButton(RoboMap.AUTO_AIM_BUTTON)) {}
                //Auto aim, mothafuckas
            if (stickR.getRawButton(RoboMap.MANUAL_LAUNCHER_UP_BUTTON)) {}
                //Manually move launcher up a notch, mothafuckas
            else if (stickR.getRawButton(RoboMap.MANUAL_LAUNCHER_DOWN_BUTTON)) {}
                //Manually move launcher down a notch, mothafuckas
            if (stickL.getRawButton(RoboMap.FIRE_BUTTON)) {
                msg.printLn("Launch!");
                launcher.launch();
            }
            
    }
    public void testPeriodic() {
    }
}
