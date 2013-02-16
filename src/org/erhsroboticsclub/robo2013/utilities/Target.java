package org.erhsroboticsclub.robo2013.utilities;

public class Target {
    public double x, distance, height;
    
    public Target() {
        
    }
    
    // Made the constructor in case we need to instantiate targets
    // at some point, rather than generate them from the json stream
    public Target(double x, double distance, double height) {
        this.x = x;
        this.distance = distance;
        this.height = height;
    }
    
}
