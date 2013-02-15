package org.erhsroboticsclub.robo2013;


import com.sun.squawk.platform.posix.linux.natives.SocketImpl;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.ServerSocketConnection;
import javax.microedition.io.SocketConnection;
import org.erhsroboticsclub.robo2013.utilities.Controls;
import org.erhsroboticsclub.robo2013.utilities.Messenger; 

public class Robo2013 extends IterativeRobot {

    private RobotDrive drive;
    private Joystick stickL, stickR, stickC/*the controll stick*/;
    private CANJaguar TOP_LEFT_JAGUAR, BOTTOM_LEFT_JAGUAR, TOP_RIGHT_JAGUAR, BOTTOM_RIGHT_JAGUAR;
    private Messenger msg;
    private Controls controls;
    private LinearAccelerator launcher;
    private final double speed = 0.5; 
    
    

    public void robotInit() {
        
        msg = new Messenger();
        msg.printLn("Loading FRC 2013");
        try {
            TOP_LEFT_JAGUAR = new CANJaguar(RoboMap.TOP_LEFT_MOTOR);
            BOTTOM_LEFT_JAGUAR = new CANJaguar(RoboMap.BOTTOM_LEFT_MOTOR);
            TOP_RIGHT_JAGUAR = new CANJaguar(RoboMap.TOP_RIGHT_MOTOR);
            BOTTOM_RIGHT_JAGUAR = new CANJaguar(RoboMap.BOTTOM_RIGHT_MOTOR);

        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        launcher = new LinearAccelerator();
        drive = new RobotDrive(TOP_LEFT_JAGUAR, BOTTOM_LEFT_JAGUAR, TOP_RIGHT_JAGUAR, BOTTOM_RIGHT_JAGUAR);
        stickL = new Joystick(1);
        stickR = new Joystick(2);
        controls = new Controls(stickC);
        msg.printLn("Done Loading: FRC 2013");



    }

    public void autonomousInit() {
        drive.setSafetyEnabled(false);

        msg.clearConsole();
        while (true) {
            try {
                drive.drive(0, 0);                
                DataInputStream in = Connector.openDataInputStream("http://10.0.53.23:80/crio");
                msg.printLn("Connected");
                
                System.out.println("received: " + in.readUTF());
                
                in.close();
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

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {       
        drive.tankDrive(stickL.getY() * speed, stickR.getY() * speed);
        
        if(stickC.getRawButton(RoboMap.AUTO_AIM_BUTTON)) {
            //auto aim code
        }        
        if(stickC.getRawButton(RoboMap.MANUAL_LAUNCHER_UP_BUTTON)) {
            //move launcher ever so slightly up
        } else if (stickC.getRawButton(RoboMap.MANUAL_LAUNCHER_DOWN_BUTTON)) {
            //move launcher ever so slightly down
        }
        if(stickC.getRawButton(RoboMap.MANUAL_SET_SPEED_BUTTON)) {
            double launch_speed = stickC.getThrottle();
            launcher.setWheels(launch_speed, launch_speed);
        }
        if(stickC.getTrigger()) {//semi-auto aim
            //turn to face target
        }
        
        

    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    }
}
