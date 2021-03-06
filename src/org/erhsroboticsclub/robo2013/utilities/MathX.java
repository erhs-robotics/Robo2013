package org.erhsroboticsclub.robo2013.utilities;

import com.sun.squawk.util.MathUtils;

/**
 * Contains a lot of useful math functions that FRC doesn't normally give us.
 * Also adjusts trig functions to use degrees instead of radians.
 *
 * @author Nick, Michael
 */
public class MathX {

    public static double round(double input) {
        double result = MathUtils.round(input);
        return result;
    }

    public static double abs(double input) {
        double result = java.lang.Math.abs(input);
        return result;
    }

    public static double tan(double input) {
        double result = java.lang.Math.tan(
                java.lang.Math.toRadians(input));
        return result;
    }

    public static double atan(double input) {
        double result = java.lang.Math.toDegrees(
                MathUtils.atan(input));
        return result;
    }

    public static double cos(double input) {
        double result = java.lang.Math.cos(
                java.lang.Math.toRadians(input));
        return result;
    }

    public static double acos(double input) {
        double result = java.lang.Math.toDegrees(
                MathUtils.acos(input));
        return result;
    }

    public static double sin(double input) {
        double result = java.lang.Math.sin(
                java.lang.Math.toRadians(input));
        return result;
    }

    public static double asin(double input) {
        double result = java.lang.Math.toDegrees(
                MathUtils.asin(input));
        return result;
    }

    public static double pow(double a, double b) {
        return MathUtils.pow(a, b);
    }

    public static double sqrt(double d) {
        return java.lang.Math.sqrt(d);
    }

    public static double max(double d1, double d2) {
        if (d1 > d2)
            return d1;
        return d2;
    }

    public static double min(double d1, double d2) {
        if (d1 < d2)
            return d1;
        return d2;
    }

    public static double map(double x, double inMin, double inMax, double outMin, double outMax) {
        return (x - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
    }

    public static double clamp(double value, double min, double max) {
        if (value > max)
            return max;
        if (value < min)
            return min;
        return value;
    }

    public static boolean isWithin(double value, double target, double variance) {
        if (abs(value - target) <= variance)
            return true;
        return false;
    }
    
    public static boolean isBetween(double value, double min, double max) {
        if (value >= min || value <= max)
            return true;
        return false;
    }

    public static double sigmoid(double n) {
        return 1 / (1 + MathX.pow(Math.E, -n));
    }

}