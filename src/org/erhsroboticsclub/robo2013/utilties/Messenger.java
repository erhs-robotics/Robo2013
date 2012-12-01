package org.erhsroboticsclub.robo2013.utilties;

import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Timer;

/**
 *
 * This class uses the DriverStationLCD class to post messages to the driver
 * station It uses an array of strings to make the driver station lcd more like
 * a console
 *
 * @author Michael
 */
public class Messenger {

    private DriverStationLCD driverLCD;
    private String msg[];
    private final String emptySpace = "                             ";

    public Messenger() {
        driverLCD = DriverStationLCD.getInstance();
        msg = new String[6];
        for (int i = 0; i < 6; i++) {
            msg[i] = " ";
        }
    }

    private void moveUp() {
        for(int i=msg.length-1;i>0;i--) {
            msg[i] = msg[i - 1];    
        }        
    }

    private void write(DriverStationLCD.Line line, String _msg) {
        if (_msg.length() > 20) {
            msg[0] = _msg.substring(0, 20);
            driverLCD.println(line, 1, msg[0]);
            _msg = _msg.substring(20);

            this.printLn(_msg);
        } else {
            for (int i = 0; i < _msg.length(); i++) {
                String s = "" + _msg.toCharArray()[i];
                driverLCD.println(line, i + 1, s);
            }
        }
    }   

    /**
     * Clears the DriverStation LCD
     */
    public final void clearConsole() {
        driverLCD.println(DriverStationLCD.Line.kMain6, 1, emptySpace);
        driverLCD.println(DriverStationLCD.Line.kUser2, 1, emptySpace);
        driverLCD.println(DriverStationLCD.Line.kUser3, 1, emptySpace);
        driverLCD.println(DriverStationLCD.Line.kUser4, 1, emptySpace);
        driverLCD.println(DriverStationLCD.Line.kUser5, 1, emptySpace);
        driverLCD.println(DriverStationLCD.Line.kUser6, 1, emptySpace);
    }

    /**
     * Prints a message to a specific line on the Driver Station LCD
     *
     * @param s The String to be printed on the Driver Station
     * @param line The line to print the message to
     */
    public void printOnLn(String s, DriverStationLCD.Line line) {
        driverLCD.println(line, 1, "                             ");
        driverLCD.println(line, 1, s);
        driverLCD.updateLCD();        
    }

    /**
     * Prints a message to the Driver Station LCD in a console-like manner
     *
     * @param s The String to be printed on the Driver Station
     */
    public void printLn(String s) {
        clearConsole();
        moveUp();
        
        String time = String.valueOf(Timer.getFPGATimestamp());
        msg[0] = "[" + time + "]: " + s;       

        write(DriverStationLCD.Line.kMain6, msg[5]);
        write(DriverStationLCD.Line.kUser2, msg[4]);
        write(DriverStationLCD.Line.kUser3, msg[3]);
        write(DriverStationLCD.Line.kUser4, msg[2]);
        write(DriverStationLCD.Line.kUser5, msg[1]);
        write(DriverStationLCD.Line.kUser6, msg[0]);
        driverLCD.updateLCD();
    }
}
