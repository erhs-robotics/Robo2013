package org.erhsroboticsclub.robo2013;

import edu.wpi.first.wpilibj.DriverStationLCD.Line;

public class RoboMap {
    
    /* Robot Constants */
    public static final double SPEED = 1;       // 0 = no move; 1 = max speed
    public static final double UPDATE_FREQ = 5; // ms
    public static final long BUMP_TIME = 50;    // ms
    
    
    /* PWM Out */    
    public static final int TOP_LEFT_DRIVE_MOTOR = 4;
    public static final int BOTTOM_LEFT_DRIVE_MOTOR = 2;
    public static final int TOP_RIGHT_DRIVE_MOTOR = 3;
    public static final int BOTTOM_RIGHT_DRIVE_MOTOR = 1;
    public static final int SECONDARY_LAUNCH_MOTOR = 5; // second
    public static final int PRIMARY_LAUNCH_MOTOR = 6; // forward
    public static final int ELEVATOR_MOTOR = 7;
    public static final int LOAD_ARM_MOTOR1 = 8;
    public static final int LOAD_ARM_MOTOR2 = 9;
    
    /* Digital Input */    
    
    public static final int LIMIT_SWITCH = 8;
    
    /* USB Input */
    public static final int LEFT_DRIVE_STICK = 1;
    public static final int RIGHT_DRIVE_STICK = 2;
    
    /* Analog In Ports */
    public static final int LAUNCHER_ACCEL = 1;
    public static final int MODE_POT = 2;
    
    /* Left Stick Controls */
    //public static final int AUTO_AIM_BUTTON = 1;
    public static final int FIRE_BUTTON = 1;
    public static final int BUMP_UP_BUTTON = 3;
    public static final int BUMP_DOWN_BUTTON = 2;
    public static final int BUMP_DRIVE_LEFT = 4;
    public static final int BUMP_DRIVE_RIGHT = 5;
    
    /* Right Stick Controls */
    public static final int FEED_ANGLE_BUTTON = 2;
    public static final int TOP_ANGLE_BUTTON = 3;
    public static final int LAUNCHER_OFF_BUTTON = 8;
    public static final int LAUNCHER_ON_BUTTON = 9;
    
    /* Teleop Driver Station LCD Lines */
    public static final Line STATUS_LINE = Line.kUser1;
    public static final Line ANGLE_MODE_LINE = Line.kUser2;
    public static final Line VOLTAGE_LINE = Line.kUser3;
    public static final Line ANGLE_LINE = Line.kUser4;
    public static final Line SETPOINT_LINE = Line.kUser5;
    public static final Line ERROR_LINE = Line.kUser6;
    
    /* Linear Accelerator */
    public static final double LAUNCHER_ANGLE_MIN = 0;
    public static final double LAUNCHER_ANGLE_MAX = 34;
    public static final double LAUNCHER_FEED_ANGLE = 12;    
    public static final double LAUNCHER_TOP_CENTER_ANGLE = 20;
    public static final double LAUNCHER_TOP_SIDE_ANGLE = 20;
    public static final double LAUNCHER_MIDDLE_ANGLE = 28.4;
    public static final double VOLT_MIN = 0.805469407;//34
    public static final double VOLT_MAX = 1.525903107;      
    public static final double AUTO_SHOOT_SPEED = -.8;
    public static final int AVERAGING_BITS = 7;
    public static final int OVERSAMPLE_BITS = 4;
    
    /* Linear Accelerator PID Constants */
    public static final double LAUNCHER_PID_P = 0.115;
    public static final double LAUNCHER_PID_I = 0.001;
    public static final double LAUNCHER_PID_D = 0;
    public static final double LAUNCH_PID_MIN = -1;
    public static final double LAUNCH_PID_MAX = 1;

}