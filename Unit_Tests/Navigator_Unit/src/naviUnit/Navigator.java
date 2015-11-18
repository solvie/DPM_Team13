package naviUnit;

import lejos.hardware.Sound;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.robotics.geometry.Point2D;


/*
 * The Navigator class provides all the methods to allow the robot to move and navigate through the map.
 * 
 */

public class Navigator {
	private static final int SLOWER_ROTATE = 30, BUFFER_TIME = 500, ESCAPE_DIST = 30;
	public static int DEFAULT_TIMEOUT_PERIOD = 20, FORWARD_SPEED = 200, HALF_SPEED = 100, ROTATE_SPEED = 50, DEG_ERR = 2, D = 15;
	private Odometer odo;
	private EV3LargeRegulatedMotor leftMotor, rightMotor, sensorMotor;
	private Point2D[] knownObstacles;
	private boolean thereIsObject, thereIsObjectWithinD, objectColorSeen;
	
	/*
	 * Default constructor
	 */
	public Navigator(Odometer odo, EV3LargeRegulatedMotor sensorMotor){
		this.odo = odo;
		this.leftMotor = odo.getMotors()[0];
		this.rightMotor = odo.getMotors()[1];
		this.sensorMotor = sensorMotor;
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
	public boolean [] travelToWithAvoidance(double x, double y){
		double currX, currY, trajTheta, distance;
		boolean[] result= new boolean[2];

		// calculate the amount of theta to turn, and turn by that amount
		currX = odo.getX();
		currY = odo.getY();
		trajTheta = (Math.atan2(y - odo.getY(), x - odo.getX())) * (180.0 / Math.PI);
		turnTo(trajTheta, true);
		
		if (thereIsObjectWithinD) { //if there is an object right at the start.
			result[0] = true; // object at start
			result[1] = true;// object in path.
			return result;
		}
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
						result[0] = false;
						result[1] = true;
						return result;
					}
				}
		}
		leftMotor.stop(true);
		rightMotor.stop(true);
		result[0] = false;
		result[1] = false;
		return result;
	}
	
	/*
	 * This method will have the robot travelling forward with the sensor facing sideways facing an obstacle.
	 * Once the robot no longer sees the obstacle, it will stop.
	 */
	public void travelUntilNoObstacle(boolean left){ //the boolean defines the empty path direction.
		Sound.beepSequenceUp();
		double currTheta = odo.getAng(), destAngle;
		sensorMotor.setAcceleration(200);
		//turn the sensormotor and the robot to the right direction
		if (left){
			Sound.beep();
			Sound.beep();
			sensorMotor.setAcceleration(200);
			sensorMotor.rotate(-90);
			destAngle = (currTheta+90.0)%360;
			turnTo(destAngle, true);
		}
		else{
			Sound.beep();
			Sound.beep();
			sensorMotor.setAcceleration(200);
			sensorMotor.rotate(90);
			destAngle = (currTheta-90.0);
			if (destAngle<0)
				destAngle = destAngle + 360;
			turnTo(destAngle, true);	
		}
		
		//go forward until there is no obstacle detected.
		setSpeeds(HALF_SPEED, HALF_SPEED);
		leftMotor.forward();
		rightMotor.forward();
		try {Thread.sleep(BUFFER_TIME);
		} catch (InterruptedException e) {e.printStackTrace();}
		while (true){
			leftMotor.forward();
			rightMotor.forward();
			if (!thereIsObject)
				break;
		}
		Sound.beepSequenceUp();
		//let the robot's body move past the obstacle before stopping.
		try{ Thread.sleep(BUFFER_TIME*10);
		} catch(InterruptedException e) {e.printStackTrace();}
		stopMotors();	
		Sound.beepSequence();
		

		currTheta = odo.getAng();
		if (left){
			destAngle = (currTheta-90.0);
			if (destAngle<0)
				destAngle = destAngle + 360;
			turnTo(destAngle, true);
		}
		
		else{
			destAngle = (currTheta+90.0)%360;
			turnTo(destAngle, true);	
		}
		
        //Travel forwards some distance
		setSpeeds(HALF_SPEED, HALF_SPEED);
		leftMotor.forward();
		rightMotor.forward();
		try {Thread.sleep(BUFFER_TIME*6);
		} catch (InterruptedException e) {e.printStackTrace();}
		while (true){
			leftMotor.forward();
			rightMotor.forward();
			if (!thereIsObject)
				break;
		}
		Sound.beepSequenceUp();
		//let the robot's body move past the obstacle before stopping.
		try{ Thread.sleep(BUFFER_TIME*10);
		} catch(InterruptedException e) {e.printStackTrace();}
		stopMotors();	
		Sound.beepSequence();
		
		
		// put sensors back
		if (left){
			sensorMotor.rotate(90);
		}
		else{
			sensorMotor.rotate(-90);	
		}
>>>>>>> e481271e679bcb41eb5998b390d10167a47ed5c5
		Sound.beep();
	}
	
	/*
	 * This method turns the robot in place to face the specified destination heading.
	 */
	public void turnTo(double theta, boolean stop) {
		double error = theta - odo.getAng(), abserror;
		boolean turn = false;
		if (Math.abs(error)>DEG_ERR)
			turn = true;
		
		while (turn){//changed from while Math.abs(error)> DEG_ERR
			error = theta - this.odo.getAng();
			abserror = Math.abs(error);
			
			if (error < -180.0) {
				this.setSpeeds(-HALF_SPEED, HALF_SPEED);
				if (abserror < DEG_ERR)
					break;
			} else if (error < 0.0) {
				this.setSpeeds(HALF_SPEED, -HALF_SPEED);
				if (abserror < DEG_ERR)
					break;
			} else if (error > 180.0) {
				this.setSpeeds(HALF_SPEED, -HALF_SPEED);
				if (abserror < DEG_ERR)
					break;
			} else {
				this.setSpeeds(-HALF_SPEED, HALF_SPEED);
				if (abserror < DEG_ERR)
					break;
			}
		}

		if (stop) {
			this.setSpeeds(0, 0);
		}

	}
	
	public boolean[] scan(){
		boolean[] info = new boolean[2]; // first is whether its empty, second is direction (left is true)
		boolean left = false;
		sensorMotor.setAcceleration(200);
		sensorMotor.rotate(90); // look left
		try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}//(wait a second)
		if (thereIsObject){
			Sound.buzz();
			sensorMotor.rotate(-180); //look right
			try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}//(wait a second)
			if (thereIsObject){
				Sound.buzz();
				info[0] = false;
				info[1] = false;
			}
			else{
				info[0] = true;
				info[1] = false;
			}
		}
		else{
			left = true;
			info[0] = true;
			info[1] = true;
		}
		//place sensor back in starting position
		
		sensorMotor.setAcceleration(200);
		if (left)
			sensorMotor.rotate(-90);
		else
			sensorMotor.rotate(90);
		return info;
	}
	
	public void travelBackwards(int distance){
		double currX, currY;
		currX = odo.getX();
		currY = odo.getY();
		while(Math.sqrt(Math.pow(odo.getX() - currX, 2) + Math.pow(odo.getY() - currY, 2)) < distance){
			leftMotor.setSpeed(HALF_SPEED);
			rightMotor.setSpeed(HALF_SPEED);
			leftMotor.backward();
			rightMotor.backward();
		}
		leftMotor.stop(true);
		rightMotor.stop(true);
		
	}
	
	
	public void travelForwards(int distance){
		double currX, currY;
		currX = odo.getX();
		currY = odo.getY();
		while(Math.sqrt(Math.pow(odo.getX() - currX, 2) + Math.pow(odo.getY() - currY, 2)) < distance){
			leftMotor.setSpeed(HALF_SPEED);
			rightMotor.setSpeed(HALF_SPEED);
			leftMotor.forward();
			rightMotor.forward();
			if (thereIsObject)
				break;
		}
		leftMotor.stop(true);
		rightMotor.stop(true);
		
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
