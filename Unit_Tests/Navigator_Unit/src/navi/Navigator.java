package navi;

import navi.Odometer;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.robotics.geometry.Point2D;
import lejos.utility.Timer;
import lejos.utility.TimerListener;

/* The Navigator class provides all the methods to allow the robot to move and navigate through the map.
 * 
 * This version of the class was written for the purposes of a unit test to determine whether the robot
 * can find a path from point A to point B given new obstacle information along the way, travelling only along lines parallel
 * to the X and Y axes, (for the purposes of having more accurate odometry correction).
 * 
 * Therefore, it does not  actually use ultrasonic data (that will be a different test) to detect blocks, but will be given new block
 * information at various intervals, from an outside class. 
 *  
 */

public class Navigator implements TimerListener{
	public static final int DEFAULT_TIMEOUT_PERIOD = 20, FORWARD_SPEED = 200, HALF_SPEED = 100, ROTATE_SPEED = 50, D = 15, SLOWER_ROTATE = 30;
	public static final double  DEG_ERR = 0.5;
	private Timer timer;
	private Odometer odo;
	private EV3LargeRegulatedMotor leftMotor, rightMotor;
	private Point2D[] knownObstacles;
	private boolean thereIsObject, thereIsObjectWithinD, objectColorSeen;
	
	public Navigator(Odometer odo){
		this.odo = odo;
		this.timer = new Timer(DEFAULT_TIMEOUT_PERIOD, this);
		this.leftMotor = odo.getLeftMotor();
		this.rightMotor = odo.getRightMotor();
		this.thereIsObject = false;
		this.thereIsObjectWithinD = false;
		this.objectColorSeen = false;
	}
	
	public void timedOut(){
		//check ultrasonic data
	}
	
	/* This method will make the robot go from its current point to the destination point travelling entirely along lines 
	 * parallel to the x and y axis. At its base, it should travel from start to end the two tangent lines of the shortest distance vector
	 * unless a block gets in its way. It should also keep track of where all the blocks were detected, so that its return journey is easier. 
	 */
	public Point2D[] squareTravel(double x, double y, Point2D[] obstacles){
		double currX, currY, currTheta, horizl, vertl;
		boolean longerDistanceAppropriate;
		Point2D[] wayPoints = new Point2D[2];
		currX = odo.getX();
		currY = odo.getY();
		currTheta = odo.getAng();
		
		horizl = Math.abs(x - currX);
		vertl = Math.abs(y - currY);
		//set up wayPoints to travel the longer way first, IF there is no obstacle immediately in front, in which case, travel to where there is no obstacle.
		
		if (horizl-vertl>=0){
			wayPoints[0] = new Point2D.Double(x, currY);
			wayPoints[1] = new Point2D.Double(x, y);
		}
		else{
			wayPoints[0] = new Point2D.Double(currX, y);
			wayPoints[1] = new Point2D.Double(x, y);
		}
		
		//TODO: modify travel to method so that if the destination it wants to travel to is within a centimeter of the current angle and whatever
		//you stay put.
		boolean pathBlocked1, pathBlocked2;
		
		if (!(((odo.getX()<x+DEG_ERR)&&(odo.getX()>x-DEG_ERR)) && ((odo.getY()<y+DEG_ERR)&&(odo.getY()>y-DEG_ERR)))){ //While we've not reached the destination, this happens. //TODO: modify so that there's more room for error.
			//travel to the first point. If we try to travel there and don't reach the correct place, the same method is called again. 
			pathBlocked1 = travelToWithAvoidance(wayPoints[0].getX(), wayPoints[0].getY());
			if (pathBlocked1){
				//pathBlocked2 = travelToWithAvoidance(wayPoints[1].getX(), wayPoints[1].getY());
				obstacles = squareTravel(x, y, obstacles);
				//TODO: deal with hard case where there are two immediate blocks. 
			}
			else{
				pathBlocked2 =travelToWithAvoidance(wayPoints[1].getX(), wayPoints[1].getY());
				if (pathBlocked2){
					obstacles = squareTravel(x, y, obstacles);
				}
				else{
					leftMotor.stop(true);
					rightMotor.stop(true);
					return obstacles;
				}
			}	
		}
		return obstacles;
	}
	
	//The regular travelTo method - will likely be deprecated
	public void travelTo(double x, double y){
		double currX, currY, trajTheta, distance;
		// calculate the amount of theta to turn, and turn by that amount
		currX = odo.getX();
		currY = odo.getY();
		trajTheta = getArcTan(x, y, currX, currY);
		
		turnTo(trajTheta);
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
	
	//The travelTo method that stops when an obstacle is detected. 
	public boolean travelToWithAvoidance(double x, double y){
		double currX, currY, trajTheta, distance;
		if (thereIsObjectWithinD) //if there is an object right at the start.
			return true;
		// calculate the amount of theta to turn, and turn by that amount
		currX = odo.getX();
		currY = odo.getY();
		trajTheta = getArcTan(x, y, currX, currY);
		turnTo(trajTheta);
		// calculate the distance that needs to be traveled, and go that distance
		distance = Math.sqrt(Math.pow((y - currY), 2) + Math.pow((x - currX), 2));
		leftMotor.setSpeed(FORWARD_SPEED);
		rightMotor.setSpeed(FORWARD_SPEED);
		// while the distance has not been fully reached, and while there is no object in its path
		while ((Math.sqrt(Math.pow(odo.getX() - currX, 2) + Math.pow(odo.getY() - currY, 2)) < distance)) {
				leftMotor.forward();
				rightMotor.forward();
				if (thereIsObject){
					leftMotor.setSpeed(HALF_SPEED);
					rightMotor.setSpeed(HALF_SPEED);
					leftMotor.forward();
					rightMotor.forward();
					if (thereIsObjectWithinD){
						return true;
					}
				}
		}
		leftMotor.stop(true);
		rightMotor.stop(true);
		
		return false;
	}
	
	public void turnTo(double theta) {
		leftMotor.setSpeed(ROTATE_SPEED);
		rightMotor.setSpeed(ROTATE_SPEED);
		if (theta< 0) {
			theta = Math.abs(theta);
			while ((odo.getAng() < (theta - DEG_ERR))
					|| odo.getAng() > (theta + DEG_ERR)) {
				//turn clockwise
				leftMotor.forward();
				rightMotor.backward();
			}
		} else {
			//turn counter clockwise
			if (theta == 0 ||theta == 360){// handling the extremity situation 
				// If theta =0 or 360, the error margin needs to be modified as the wrap-around will eliminate any negative theta possibilities. 
				while (odo.getAng()<(360-2*DEG_ERR) && odo.getAng()> DEG_ERR){ 
					leftMotor.backward();
					rightMotor.forward();
				}
			}
			else{ //normal situation
			while (odo.getAng() < (Math.abs(theta) - DEG_ERR)
					|| odo.getAng() > (Math.abs(theta) + DEG_ERR)) {
				leftMotor.backward();
				rightMotor.forward();
			}
			}
		}
	}
	
	public double getArcTan(double x, double y, double currX, double currY) {
		double value, xdiff, ydiff;
		xdiff = x - currX;
		ydiff = y - currY;
		if (ydiff > 0) {
			if (xdiff > 0)
				value = 90 - Math.toDegrees(Math.atan(ydiff / xdiff));
			else
				value = Math.toDegrees(Math.atan(Math.abs(ydiff / xdiff))) - 90;
		} else {
			if (xdiff > 0)
				value = 90 + Math.toDegrees(Math.atan(Math.abs((ydiff / xdiff))));
			else
				value = -90 - Math.toDegrees(Math.atan(ydiff / xdiff));
		}

		return value;
	}
	
	//
	
	public void turnClockwise(){
		leftMotor.setSpeed(SLOWER_ROTATE);
		rightMotor.setSpeed(SLOWER_ROTATE);
		leftMotor.forward();
		rightMotor.backward();
	}
	
	public void turnCounterClockwise(){
		leftMotor.setSpeed(SLOWER_ROTATE);
		rightMotor.setSpeed(SLOWER_ROTATE);
		leftMotor.backward();
		rightMotor.forward();
	}
	
	public void stopMotors(){
		leftMotor.stop();
		rightMotor.stop();
	}
	
	public void setDetectionInfo(boolean[] info, boolean[] update){
		if (update[0])
			this.thereIsObject =info[0];
		if (update[1])
			this.thereIsObjectWithinD = info[1];
		if (update[2])
			this.objectColorSeen = info[2];
	}
}
