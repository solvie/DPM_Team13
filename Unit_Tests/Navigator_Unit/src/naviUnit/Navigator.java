package naviUnit;

import lejos.hardware.Sound;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.robotics.geometry.Point2D;


/*
 * The Navigator class provides all the methods to allow the robot to move and navigate through the map.
 * 
 */

public class Navigator {
	private static final int SLOWER_ROTATE = 30;
	public static int DEFAULT_TIMEOUT_PERIOD = 20, FORWARD_SPEED = 200, HALF_SPEED = 100, ROTATE_SPEED = 50, DEG_ERR = 2, D = 15;
	private Odometer odo;
	private EV3LargeRegulatedMotor leftMotor, rightMotor;
	private Point2D[] knownObstacles;
	private boolean thereIsObject, thereIsObjectWithinD, objectColorSeen;
	
	/*
	 * Default constructor
	 */
	public Navigator(Odometer odo){
		this.odo = odo;
		this.leftMotor = odo.getMotors()[0];
		this.rightMotor = odo.getMotors()[1];
		this.thereIsObject = false;
		this.thereIsObjectWithinD = false;
		this.objectColorSeen = false;
	}
	
	
	/*
	 * The conventional method of travelling to a point; calculates and turns to the heading it must face and drives the distance
	 * that it must go to arrive at the destination described by input points. May be deprecated.
	 */
	
	public void travelTo(double x, double y){
		double currX, currY, trajTheta, distance;
		// calculate the amount of theta to turn, and turn by that amount
		currX = odo.getX();
		currY = odo.getY();
		trajTheta = (Math.atan2(y - odo.getY(), x - odo.getX())) * (180.0 / Math.PI);
		
		turnTo(trajTheta, true);
		// calculate the distance that needs to be traveled, and go that distance
		distance = Math.sqrt(Math.pow((y - currY), 2) + Math.pow((x - currX), 2));
		leftMotor.setSpeed(FORWARD_SPEED);
		rightMotor.setSpeed(FORWARD_SPEED);
		// while the distance has not been fully reached
		while (Math.sqrt(Math.pow(odo.getX() - currX, 2) + Math.pow(odo.getY() - currY, 2)) < distance) {
				leftMotor.forward();
				rightMotor.forward();
		}
		leftMotor.stop(true);
		rightMotor.stop(true);
	}
	
	/*
	 * Travels to the destination point in a similar manner as the travelTo method. However, it will slow down when it sees an obstacle, 
	 * and stop when it is a certain distance away from it. (TODO: define that distance, maybe pass it in as a parameter)
	 *
	 */
	public boolean travelToWithAvoidance(double x, double y){
		double currX, currY, trajTheta, distance;

		// calculate the amount of theta to turn, and turn by that amount
		currX = odo.getX();
		currY = odo.getY();
		trajTheta = (Math.atan2(y - odo.getY(), x - odo.getX())) * (180.0 / Math.PI);
		turnTo(trajTheta, true);
		
		if (thereIsObjectWithinD) //if there is an object right at the start.
			return true;
		
		// calculate the distance that needs to be traveled, and go that distance
		distance = Math.sqrt(Math.pow((y - currY), 2) + Math.pow((x - currX), 2));
		leftMotor.setSpeed(FORWARD_SPEED);
		rightMotor.setSpeed(FORWARD_SPEED);
		// while the distance has not been fully reached, go forward until object is noticed.
		while ((Math.sqrt(Math.pow(odo.getX() - currX, 2) + Math.pow(odo.getY() - currY, 2)) < distance)) {
				leftMotor.forward();
				rightMotor.forward();
				if (thereIsObject){
					leftMotor.setSpeed(HALF_SPEED);
					rightMotor.setSpeed(HALF_SPEED);
					leftMotor.forward();
					rightMotor.forward();
					if (thereIsObjectWithinD){
						leftMotor.stop(true);
						rightMotor.stop(true);
						return true;
					}
				}
		}
		leftMotor.stop(true);
		rightMotor.stop(true);
		
		return false;
	}
	
	/*
	 * This method turns the robot in place to face the specified destination heading.
	 */
	public void turnTo(double theta, boolean stop) {
		double error = theta - this.odo.getAng();
		while (Math.abs(error)> DEG_ERR){
			error = theta - this.odo.getAng();
			
			if (error < -180.0) {
				this.setSpeeds(-HALF_SPEED, HALF_SPEED);
			} else if (error < 0.0) {
				this.setSpeeds(HALF_SPEED, -HALF_SPEED);
			} else if (error > 180.0) {
				this.setSpeeds(HALF_SPEED, -HALF_SPEED);
			} else {
				this.setSpeeds(-HALF_SPEED, HALF_SPEED);
			}
		}

		if (stop) {
			this.setSpeeds(0, 0);
		}

	}
	
	public void setSpeeds(float lSpd, float rSpd) {
		this.leftMotor.setSpeed(lSpd);
		this.rightMotor.setSpeed(rSpd);
		if (lSpd < 0)
			this.leftMotor.backward();
		else
			this.leftMotor.forward();
		if (rSpd < 0)
			this.rightMotor.backward();
		else
			this.rightMotor.forward();
	}

	public void setSpeeds(int lSpd, int rSpd) {
		this.leftMotor.setSpeed(lSpd);
		this.rightMotor.setSpeed(rSpd);
		if (lSpd < 0)
			this.leftMotor.backward();
		else
			this.leftMotor.forward();
		if (rSpd < 0)
			this.rightMotor.backward();
		else
			this.rightMotor.forward();
	}
	
	/*
	 * Stops the motors
	 */
	public void stopMotors(){
		leftMotor.stop(true);
		rightMotor.stop(true);
	}
	
	/*
	 * Allows outside classes to set the boolean values of whether there is an object in front of the robot, whether the object is within D cm
	 * and whether the object's color was seen.
	 */
	public void setDetectionInfo(boolean[] info, boolean[] update){
		if (update[0])
			this.thereIsObject =info[0];
		if (update[1])
			this.thereIsObjectWithinD = info[1];
		if (update[2])
			this.objectColorSeen = info[2];
	}
	
	/*
	 * Allows outside classes to access the navigator's odometer
	 */
	public Odometer getOdo(){
		return odo;
	}
}
