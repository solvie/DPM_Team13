package naviUnit;

import lejos.hardware.Sound;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.robotics.geometry.Point2D;
/*
 * This class is used to find the enemy base without colliding into any of the obstacles, and to return to the home base.
 *
 */
public class PathFinder {

	private static final double DEG_ERR = 0.5;
	private Navigator navi;
	private Odometer odo;
	private ObjectDetector obDetector;
	private String flag;
	private Point2D[]wayPoints;

	/*
	 * Default constructor
	 */
	public PathFinder(ObjectDetector obDetector){
		this.navi = obDetector.getNavi();
		this.odo = navi.getOdo();
		this.obDetector = obDetector;
		this.flag = "";
		
	}

	/*
	 * This method will make the robot go from its current point to the destination point travelling entirely along lines 
	 * parallel to the x and y axis. At its base, it should travel from start to end the two tangent lines of the shortest distance vector
	 * unless a block gets in its way. It should also keep track of where all the blocks were detected.
	 */
	public Point2D[] findPathTo(double x, double y, Point2D[] obstacles){
		double currX, currY, currTheta, horizl, vertl;
		wayPoints = new Point2D[2];
		currX = odo.getX();
		currY = odo.getY();
		currTheta = odo.getAng();
		
		horizl = Math.abs(x - currX);
		vertl = Math.abs(y - currY);
		
		//set up wayPoints to travel the longer way first, UNLESS there is no obstacle immediately in front, in which case, travel to where there is no obstacle.
		if (horizl-vertl>0){
			wayPoints[0] = new Point2D.Double(x, currY);
			wayPoints[1] = new Point2D.Double(x, y);
		
		}
		else{
			wayPoints[0] = new Point2D.Double(currX, y);
			wayPoints[1] = new Point2D.Double(x, y);
		
		}
		
		//TODO: modify travel to method so that if the destination it wants to travel to is within a centimeter of the current angle and whatever
		//you stay put.
		
		boolean [] pathBlocked1, pathBlocked2;
		if (!(((odo.getX()<x+DEG_ERR)&&(odo.getX()>x-DEG_ERR)) && ((odo.getY()<y+DEG_ERR)&&(odo.getY()>y-DEG_ERR)))){ //While we've not reached the destination, this happens. //TODO: modify so that there's more room for error.
			//travel to the first point. If we try to travel there and don't reach the correct place, the same method is called again. 
			setOdoFlag(0);
			pathBlocked1 = navi.travelToWithAvoidance(wayPoints[0].getX(), wayPoints[0].getY());
			if (pathBlocked1[0]){ // if there is an obstacle immediately in front, flip the waypoints
				if (wayPoints[0].getX() ==x && wayPoints[0].getY()==currY)
					wayPoints[0] = new Point2D.Double(currX, y);
				else
					wayPoints[0] = new Point2D.Double(x, currY);
			}
			
			if (pathBlocked1[1]){
				Sound.beep();
				setOdoFlag(1);
				obstacles = findPathTo(x, y, obstacles);
				
			}
			else{
				setOdoFlag(1);
				pathBlocked2 =navi.travelToWithAvoidance(wayPoints[1].getX(), wayPoints[1].getY());
				if (pathBlocked2[1]){
					Sound.beep();
					obstacles = findPathTo(x, y, obstacles);
				}
				else{
					navi.stopMotors();
					return obstacles;
				}
			}	
		}
		return obstacles;
	}
	
	private void setOdoFlag(int i){
		flag = "to (" + wayPoints[i].getX() + ", " + wayPoints[i].getY() + ")"; 
		odo.setFlag(flag);
	}
	
	
}
