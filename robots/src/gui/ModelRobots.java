package gui;

import java.awt.Point;
import java.util.Observable;

public class ModelRobots extends Observable{
    private volatile double robotPositionX = 100;
    private volatile double robotPositionY = 100; 
    private volatile double robotDirection = 0; 

    private volatile int targetPositionX = 150;
    private volatile int targetPositionY = 100;
    
    private static final double MAX_VELOCITY = 0.1; 
    private static final double MAX_ANGULAR_VELOCITY = 0.001; 
    
    
    protected void setTargetPosition(Point p) {
        targetPositionX = p.x;
        targetPositionY = p.y;
    }
    
    private void setrobotPosition(double x, double y) {
    	this.robotPositionX = x;
    	this.robotPositionY = y;
    	setChanged();

        notifyObservers(new double[] {x,y});
    }
    
    private static double distance(double x1, double y1, double x2, double y2) {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }
    
    private static double angleTo(double fromX, double fromY, double toX, double toY) {
        double diffX = toX - fromX;
        double diffY = toY - fromY;
        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }
    private static double applyLimits(double value, double min, double max) {
        if (value < min) {
            return min;
        }
        if (value > max) {
            return max;
        }
        return value;
    }
    
    private double getNewCoordinates(double velocity, double angularVelocity, double duration, boolean x) {
    	if (x) {
    		double newX = robotPositionX + velocity / angularVelocity * 
	                (Math.sin(robotDirection  + angularVelocity * duration) -
	                    Math.sin(robotDirection));
	            if (!Double.isFinite(newX)) {
	                newX = robotPositionX + velocity * duration * Math.cos(robotDirection);
	            }
	            return newX;
    	}
        double newY = robotPositionY - velocity / angularVelocity * 
            (Math.cos(robotDirection  + angularVelocity * duration) -
                Math.cos(robotDirection));
        if (!Double.isFinite(newY)) {
            newY = robotPositionY + velocity * duration * Math.sin(robotDirection);
        }
        return newY;
    }
    
    private void moveRobot(double velocity, double angularVelocity, double duration) {
        velocity = applyLimits(velocity, 0, MAX_VELOCITY);
        angularVelocity = applyLimits(angularVelocity, -MAX_ANGULAR_VELOCITY, MAX_ANGULAR_VELOCITY);
        
        double newX = getNewCoordinates(velocity, angularVelocity, duration, true);
        double newY = getNewCoordinates(velocity, angularVelocity, duration, false);
        if (newX> 395 || newX < 5 || newY > 370 || newY < 5) {
        		double wallAngle = 0;
        		if (newX > 395 || newX < 5) {
        			wallAngle = Math.PI / 2;
        		}
        		robotDirection = wallAngle*2 - robotDirection;
        		newX = getNewCoordinates(velocity, angularVelocity, duration, true);
                newY = getNewCoordinates(velocity, angularVelocity, duration, false);
        } else {
        	double newDirection = asNormalizedRadians(robotDirection + angularVelocity * duration); 
            robotDirection = newDirection;
        }
        setrobotPosition(newX, newY);
    }

    private static double asNormalizedRadians(double angle) {
        while (angle < 0) {
            angle += 2*Math.PI;
        }
        while (angle >= 2*Math.PI) {
            angle -= 2*Math.PI;
        }
        return angle;
    }
    
    protected void onModelUpdateEvent() {
        double distance = distance(targetPositionX, targetPositionY, robotPositionX, robotPositionY);
        if (distance < 0.5) {
            return;
        }
        double velocity = MAX_VELOCITY;
        double angleToTarget = angleTo(robotPositionX, robotPositionY, targetPositionX, targetPositionY);
        double angularVelocity = 0;
        if (angleToTarget > robotDirection) {
            angularVelocity = MAX_ANGULAR_VELOCITY;
        }
        if (angleToTarget < robotDirection) {
            angularVelocity = -MAX_ANGULAR_VELOCITY;
        }
        
        moveRobot(velocity, angularVelocity, 10);
    }
    public int getRobotPositionX() {
    	return round(this.robotPositionX);
    }
    
    public int getRobotPositionY() {
    	return round(this.robotPositionY);
    }
    
    public double getDRobotPositionX() {
    	return this.robotPositionX;
    }
    
    public double getDRobotPositionY() {
    	return this.robotPositionY;
    }
    
    public int getRobotDirection() {
    	return round(this.robotDirection);
    }
    
    public int getTargetPositionX() {
    	return this.targetPositionX;
    }
    
    public int getTargetPositionY() {
    	return this.targetPositionY;
    }
    
    private static int round(double value) {
        return (int)(value + 0.5);
    }
}
