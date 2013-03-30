package org.erhsroboticsclub.robo2013.utilities;

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
    private final String EMPTY_SPACE = "                             ";
    private final int MAX_MESSAGE_LENGTH = 20;
    private final DriverStationLCD.Line[] LINE = {DriverStationLCD.Line.kUser1,
                                                  DriverStationLCD.Line.kUser2, 
                                                  DriverStationLCD.Line.kUser3, 
                                                  DriverStationLCD.Line.kUser4,
                                                  DriverStationLCD.Line.kUser5,
                                                  DriverStationLCD.Line.kUser6};

    public Messenger() {
        driverLCD = DriverStationLCD.getInstance();
        msg = new String[6];
        for (int i = 0; i < 6; i++) {
            msg[i] = " ";
        }
    }

    /**
     * Moves up all lines like a scrolling console
     */
    private void moveUp() {
        for (int i = msg.length - 1; i > 0; i--) {
            msg[i] = msg[i - 1];
        }
    }

    /**
     * Writes _msg to the driver station and wraps around long strings
     *
     * @param line The line to write to
     * @param _msg The message to write
     */
    private void write(DriverStationLCD.Line line, String _msg) {
        if (_msg.length() > MAX_MESSAGE_LENGTH) {
            msg[0] = _msg.substring(0, MAX_MESSAGE_LENGTH);
            driverLCD.println(line, 1, msg[0]);
            _msg = _msg.substring(MAX_MESSAGE_LENGTH);

            this.printLn(_msg);
        } else {
            driverLCD.println(line, 1, _msg);
        }
    }

    /**
     * Clears the DriverStation LCD
     */
    public final void clearConsole() {
        for (int i = 0; i < 6; i++) {
            msg[i] = EMPTY_SPACE;
        }
        for (int i = 0; i < 6; i++) {
            write(LINE[i], msg[5 - i]);
        }
    }

    /**
     * Prints a message to a specific line on the Driver Station LCD
     *
     * @param s The String to be printed on the Driver Station
     * @param line The line to print the message to
     */
    public void printOnLn(String s, DriverStationLCD.Line line) {
        driverLCD.println(line, 1, EMPTY_SPACE);
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

        String time = String.valueOf((int) Timer.getFPGATimestamp());
        msg[0] = "[" + time + "] " + s;
        
        for (int i = 0; i < 6; i++) {
            write(LINE[i], msg[5 - i]);
        }
        
        driverLCD.updateLCD();
    }
}
