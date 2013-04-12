package org.erhsroboticsclub.robo2013;

import edu.wpi.first.wpilibj.DriverStationLCD.Line;

public class RoboMap {

    /* Robot Constants */
    public static final double SPEED = 1;
    public static final double UPDATE_FREQ = 5;
    public static final long BUMP_TIME = 50;
    
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
    public static final int LAUNCHER_ACCEL = 1;
    
    /* Left Stick Controls */
    public static final int AUTO_AIM_BUTTON = 1;
    public static final int FIRE_BUTTON = 1; //LEFT
    public static final int TURN_TO_TARGET_BUTTON = 6;
    public static final int BUMP_UP_BUTTON = 3;
    public static final int BUMP_DOWN_BUTTON = 2;
    public static final int BUMP_DRIVE_LEFT = 4;
    public static final int BUMP_DRIVE_RIGHT = 5;
    
    
    /* Right Stick Controls */
    public static final int FEED_ANGLE_BUTTON = 2;
    public static final int TOP_ANGLE_BUTTON = 3;
    

    /* Driver Station LCD Lines */
    public static final Line STATUS_LINE = Line.kUser1;
    public static final Line ANGLE_LINE = Line.kUser2;
    
    /* Linear Accelerator */
    public static final double LAUNCHER_ANGLE_MIN = 0;
    public static final double LAUNCHER_ANGLE_MAX = 35;
    public static final double LAUNCHER_FEED_ANGLE = 12;    
    public static final double LAUNCHER_TOP_ANGLE = 33.63;
    public static final double LAUNCHER_MIDDLE_ANGLE = 28.4;
    public static final double VOLT_MIN = 1.008734630;
    public static final double VOLT_MAX = 1.569643725;    
    public static final double AUTO_SHOOT_SPEED = -.8;
    public static final int AVERAGING_BITS = 7;
    public static final int OVERSAMPLE_BITS = 4;
    
    /* Linear Accelerator PID Constants */
    public static final double LAUNCHER_PID_P = 0.115;
    public static final double LAUNCHER_PID_I = 0;
    public static final double LAUNCHER_PID_D = 0;
    public static final double LAUNCH_PID_MIN = -1;
    public static final double LAUNCH_PID_MAX = 1;
    
    /* Turn To Target PID Constants */
    public static final double TURN_PID_P = 1;
    public static final double TURN_PID_I = 0;
    public static final double TURN_PID_D = 0;

}