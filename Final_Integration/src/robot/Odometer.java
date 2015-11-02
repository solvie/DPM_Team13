package robot;

/**
 * Class which controls the odometer for the robot
 * 
 * Odometer defines cooridinate system as such...
 * 
 * 					90Deg:pos y-axis
 * 							|
 * 							|
 * 							|
 * 							|
 * 180Deg:neg x-axis------------------0Deg:pos x-axis
 * 							|
 * 							|
 * 							|
 * 							|
 * 					270Deg:neg y-axis
 * 
 * The odometer is initalized to 90 degrees, assuming the robot is facing up the positive y-axis
 * 
 *  File: Odometer.java
 * @version 1.0
 * @author Sean Lawlor, ECSE 211 - Design Principles and Methods, Head TA Fall 2011
 * Ported to EV3 by: Francois Ouellet Delorme Fall 2015
 * 
 */

import lejos.utility.Timer;
import lejos.utility.TimerListener;
import lejos.hardware.Button;
import lejos.hardware.motor.EV3LargeRegulatedMotor;

public class Odometer implements TimerListener {

	private Timer timer;
	private EV3LargeRegulatedMotor leftMotor, rightMotor;
	private final int DEFAULT_TIMEOUT_PERIOD = 20;
	private double leftRadius, rightRadius, width;
	private double x, y, theta;
	private double[] oldDH, dDH;
	private int flag;
	
	/**
	 * Default constructor
	 * @param leftMotor the left motor
	 * @param rightMotor the right motor
	 * @param INTERVAL the interval of time we want the timer to time out at.
	 * @param autostart whether the timer should autostart or not.
	 */
	public Odometer (EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor, int INTERVAL, boolean autostart) {
		
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
		this.rightRadius = 2.1;
		this.leftRadius = 2.1;
		this.width = 11.35; //at 7.2V
		
		this.x = 0.0;
		this.y = 0.0;
		this.theta = 90.0;
		this.oldDH = new double[2];
		this.dDH = new double[2];

		if (autostart) {
			// if the timeout interval is given as <= 0, default to 20ms timeout 
			this.timer = new Timer((INTERVAL <= 0) ? INTERVAL : DEFAULT_TIMEOUT_PERIOD, this);
			this.timer.start();
		} else
			this.timer = null;
	}
	
	/**
	 * Function to start the timerlistener
	 */
	public void stop() {
		if (this.timer != null)
			this.timer.stop();
	}
	/**
	 * Function to stop the timerlistener
	 */
	public void start() {
		if (this.timer != null)
			this.timer.start();
	}
	
	/**
	 * Calculates the displacement and heading of the robot
	 * @param data displacement and heading
	 */
	private void getDisplacementAndHeading(double[] data) {
		int leftTacho, rightTacho;
		leftTacho = leftMotor.getTachoCount();
		rightTacho = rightMotor.getTachoCount();

		data[0] = (leftTacho * leftRadius + rightTacho * rightRadius) * Math.PI / 360.0;
		data[1] = (rightTacho * rightRadius - leftTacho * leftRadius) / width;
	}
	
	/**
	 * Recomputes the odometer values using the displacement and heading changes
	 */
	public void timedOut() {
		
		this.getDisplacementAndHeading(dDH);
		dDH[0] -= oldDH[0];
		dDH[1] -= oldDH[1];

		// update the position in a critical region
		synchronized (this) {
			theta += dDH[1];
			theta = fixDegAngle(theta);

			x += dDH[0] * Math.cos(Math.toRadians(theta));
			y += dDH[0] * Math.sin(Math.toRadians(theta));
		}

		oldDH[0] += dDH[0];
		oldDH[1] += dDH[1];
	}

	/**
	 * Allows outside classes to access the robot's X coordinate
	 * @return x coordinate
	 */
	public double getX() {
		synchronized (this) {
			return x;
		}
	}

	/**
	 * Allows outside classes to access the robot's Y coordinate
	 * @return y coordinate
	 */
	public double getY() {
		synchronized (this) {
			return y;
		}
	}

	/** Allows outside classes to access the robot's heading
	 * @return heading theta
	 */
	public double getAng() {
		synchronized (this) {
			return theta;
		}
	}

	/**
	 * Sets the position of the robot as requested by the update array of booleans.
	 * @param position x, y, theta position values
	 * @param update whether we should update the x, y, and or theta position values.
	 */
	public void setPosition(double[] position, boolean[] update) {
		synchronized (this) {
			if (update[0])
				x = position[0];
			if (update[1])
				y = position[1];
			if (update[2])
				theta = position[2];
		}
	}

	/**
	 * Getter method for the position of the robot
	 * @param position array of doubles to be filled with the relevant info
	 */
	public void getPosition(double[] position) {
		synchronized (this) {
			position[0] = x;
			position[1] = y;
			position[2] = theta;
		}
	}

	/**
	 * Another getter method for the robot
	 * @return an array of doubles filled with the relevant info
	 */
	public double[] getPosition() {
		synchronized (this) {
			return new double[] { x, y, theta };
		}
	}
	
	/**
	 * Accessor to motors
	 * @return Left motor in index 0, right motor in index 1.
	 */
	public EV3LargeRegulatedMotor [] getMotors() {
		return new EV3LargeRegulatedMotor[] {this.leftMotor, this.rightMotor};
	}
	
	/**
	 * Accessor for the odometer's timer
	 * @return timer
	 */
	
	public Timer getOdoTimer(){
		return timer;
	}

	/**
	 * This method corrects the angle such that it will not be a negative value
	 * @param angle angle to fix
	 * @return corrected angle
	 */
	public static double fixDegAngle(double angle) {
		if (angle < 0.0)
			angle = 360.0 + (angle % 360.0);

		return angle % 360.0;
	}

	/**
	 * This helper method calculates the minimum angle from a to b
	 * @param a angle a
	 * @param b angle b
	 * @return the minimum angle from a to b
	 */
	public static double minimumAngleFromTo(double a, double b) {
		double d = fixDegAngle(b - a);

		if (d < 180.0)
			return d;
		else
			return d - 360.0;
	}
	
	/**
	 * This method allows the user to set the value representing the width of the wheelbase
	 * @param width the width of the wheelbase
	 */
	public void setWidth(double width){
		this.width = width;
	}
}
