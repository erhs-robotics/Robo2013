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

    RobotDrive drive;
    Joystick sticky;
    CANJaguar TOP_LEFT_JAGUAR, BOTTOM_LEFT_JAGUAR, TOP_RIGHT_JAGUAR, BOTTOM_RIGHT_JAGUAR;
    Messenger msg;
    Controls driveControls;
    LinearAccelerator launcher;
    

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
        sticky = new Joystick(1);
        driveControls = new Controls(sticky);
        msg.printLn("Done Loading: FRC 2013");



    }

    public void autonomousInit() {
        drive.setSafetyEnabled(false);

        msg.clearConsole();
        while (true) {
            try {
                drive.drive(0, 0);                
                DataInputStream in = Connector.openDataInputStream("socket://10.0.53.23:80");
                msg.printLn("Connected");
                
                System.out.println("received: " + in.readUTF());
                
                in.close();
                msg.printLn("Success!");

            } catch (Exception ex) {
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
        launcher.launch();
        //drive.arcadeDrive(sticky);
        if (driveControls.FOV_Top()) {
            drive.drive(.75, .75);
        } else if (driveControls.FOV_Bottom()) {
            drive.drive(-.75, -.75);
        } else if (driveControls.FOV_Left()) {
            drive.drive(-.75, .75);
        } else if (driveControls.FOV_Right()) {
            drive.drive(.75, -.75);
        } else {
            drive.arcadeDrive(sticky);
        }

    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    }
}
