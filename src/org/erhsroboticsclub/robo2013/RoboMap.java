package org.erhsroboticsclub.robo2013;

import edu.wpi.first.wpilibj.DriverStationLCD.Line;

public class RoboMap {
    
    /* Robot Constants */
    public static final double SPEED = 1;    
    
    /* CAN ID Numbers */
    public static final int SECONDARY_LAUNCH_MOTOR = 1; // second
    public static final int PRIMARY_LAUNCH_MOTOR = 2; // forward
    public static final int TOP_LEFT_DRIVE_MOTOR = 5;
    public static final int BOTTOM_LEFT_DRIVE_MOTOR = 6;
    public static final int TOP_RIGHT_DRIVE_MOTOR = 3;
    public static final int BOTTOM_RIGHT_DRIVE_MOTOR = 4;
    public static final int ELEVATOR_MOTOR = 7;
    
    /* PWM Out */
    public static final int LOAD_ARM_MOTOR1 = 1;
    public static final int LOAD_ARM_MOTOR2 = 2;
    
    /* Digital Input */
    public static final int LIMIT_SWITCH = 2;
    
    /* USB Input */
    public static final int LEFT_DRIVE_STICK = 1;
    public static final int RIGHT_DRIVE_STICK = 2;
    
    /* Analog Ports */
    public static final int LAUNCHER_ANGLE_POT = 1;
    
    /* Left Stick Controls */
    public static final int AUTO_AIM_BUTTON = 1;
    public static final int FIRE_BUTTON = 1; //LEFT
    public static final int TURN_TO_TARGET_BUTTON = 6;    
    
    /* Right Stick Controls */
    public static final int COLLECT_LAUNCHER_ANGLE_BUTTON = 1;//RIGHT
    public static final int FEED_ANGLE_BUTTON = 0;
    public static final int FAR_ANGLE_BUTTON = 0;
    public static final int NEAR_ANGLE_BUTTON = 0;
    public static final int LEVEL_ANGLE_BUTTON = 0;
    public static final int DYNAMIC_ANGLE_BUTTON = 0;

    /* Driver Station LCD Lines */
    public static final Line ANGLE_LINE = Line.kUser1;
    
    /* Linear Accelerator Angle Constants */
    public static final double LAUNCHER_ANGLE_MIN = 0;
    public static final double LAUNCHER_ANGLE_MAX = 35;
    public static final double LAUNCHER_POT_MIN   = 0.144214;
    public static final double LAUNCHER_POT_MAX   = 0.6279;   
    public static final double LAUNCHER_FEED_ANGLE = 14;
    public static final double LAUNCHER_LEVEL_ANGLE = 0;
    public static final double LAUNCHER_FAR_ANGLE = 27;// Behind the pyramid
    public static final double LAUNCHER_NEAR_ANGLE = 10;// Infront of the pyramid
    
    /* Linear Accelerator PID Constants */
    public static final double LAUNCHER_PID_P = 2.8;
    public static final double LAUNCHER_PID_I = 0.01;
    public static final double LAUNCHER_PID_D = 0;
    
    /* Turn To Target PID Constants */
    public static final double TURN_PID_P = 1;
    public static final double TURN_PID_I = 0;
    public static final double TURN_PID_D = 0;
    
    /* Dead Reckoning Constants */
    public static final double AUTO_BACKUP_TIME = .5;
    public static final double AUTO_MOVE_SPEED = -0.5;
    public static final double AUTO_SHOOT_ANGLE = 23;
    
}