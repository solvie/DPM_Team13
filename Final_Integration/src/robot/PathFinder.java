package robot;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.robotics.geometry.Point2D;
/**
 * This class is used to find the enemy base without colliding into any of the obstacles, and to return to the home base.
 * 
 * @version 1.0
 * @author Solvie Lee
 *
 */
public class PathFinder {

	private Navigator navi;
	private Odometer odo;
	private ObjectDetector obDetector;
	private EV3LargeRegulatedMotor leftMotor, rightMotor;
	/**
	 * Default constructor
	 * @param navi the Navigator
	 */
	public PathFinder(Navigator navi){
		this.navi = navi;
		this.odo = navi.getOdo();
		this.leftMotor = odo.getMotors()[0];
		this.rightMotor = odo.getMotors()[1];
		this.obDetector = obDetector;
	}

	/**
	 * This method will make the robot go from its current point to the destination point travelling entirely along lines 
	 * parallel to the x and y axis. At its base, it should travel from start to end the two tangent lines of the shortest distance vector
	 * unless a block gets in its way. It should also keep track of where all the blocks were detected.
	 * @param x X coordinate of destination
	 * @param y Y coordinate of destination
	 * @param obstacles coordinates of the centers of all the detected obstacles
	 * @return Point2D[]
	 */
	public Point2D[] findPathTo(double x, double y, Point2D[] obstacles){
		/*double currX, currY, currTheta, horizl, vertl;
		boolean longerDistanceAppropriate;
		Point2D[] wayPoints = new Point2D[2];
		currX = odo.getX();
		currY = odo.getY();
		currTheta = odo.getAng();
		
		horizl = Math.abs(x - currX);
		vertl = Math.abs(y - currY);
		//set up wayPoints to travel the longer way first
		
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
		
		if (!(odo.getX()==x && odo.getY()==y)){ //While we've not reached the destination, this happens. //TODO: modify so that there's more room for error.
			//travel to the first point. If we try to travel there and don't reach the correct place, the same method is called again. 
			pathBlocked1 = navi.travelToWithAvoidance(wayPoints[0].getX(), wayPoints[0].getY());
			pathBlocked2 = navi.travelToWithAvoidance(wayPoints[1].getX(), wayPoints[1].getY());
			if (pathBlocked1&&!pathBlocked2) // if it reaches the end of the path and the first one had been blocked, try to travel the first path again. 
				pathBlocked1 = navi.travelToWithAvoidance(wayPoints[0].getX(), wayPoints[0].getY());
			if (pathBlocked1 &&pathBlocked2){
				//TODO: try to move around the block. 
			}
		}
		else{ //base case
			leftMotor.stop(true);
			rightMotor.stop(true);
		}
		
		*/
		return obstacles;
	}
	
}
