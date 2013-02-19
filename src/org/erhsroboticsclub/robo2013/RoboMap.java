package org.erhsroboticsclub.robo2013;

public class RoboMap {
    
    /* CAN ID Numbers */
    public static final int SECONDARY_LAUNCH_MOTOR = 1; // second
    public static final int PRIMARY_LAUNCH_MOTOR = 2; // forward
    public static final int TOP_LEFT_DRIVE_MOTOR = 5;
    public static final int BOTTOM_LEFT_DRIVE_MOTOR = 6;
    public static final int TOP_RIGHT_DRIVE_MOTOR = 3;
    public static final int BOTTOM_RIGHT_DRIVE_MOTOR = 4;
    public static final int ELEVATOR_MOTOR = 7;
    
    /* Digital Output */
    public static final int LOAD_ARM_MOTOR1 = 1;
    public static final int LOAD_ARM_MOTOR2 = 2;
    
    /* Digital Input */
    public static final int LIMIT_SWITCH = 2;
    
    /* USB Input */
    public static final int LEFT_DRIVE_STICK = 1;
    public static final int RIGHT_DRIVE_STICK = 2;
    
    /* Analog Ports */
    public static final int LAUNCHER_ANGLE_POT = 1;
    
    /* Controls */
    public static final int AUTO_AIM_BUTTON = 1;
    public static final int FIRE_BUTTON = 1; //LEFT
    public static final int MANUAL_LAUNCHER_UP_BUTTON = 3;
    public static final int MANUAL_LAUNCHER_DOWN_BUTTON = 4;
    public static final int MANUAL_SET_SPEED_BUTTON = 5;
    public static final int TURN_TO_TARGET_BUTTON = 6;
    public static final int TURN_TO_TARGET_0 = 4; //LEFT
    public static final int TURN_TO_TARGET_1 = 3; //LEFT
    public static final int TURN_TO_TARGET_2 = 5; //LEFT
    public static final int TURN_TO_TARGET_3 = 2; //LEFT
}