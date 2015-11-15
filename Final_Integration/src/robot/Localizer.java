package robot;

import lejos.robotics.SampleProvider;
import lejos.robotics.geometry.Point2D;

/**
 * This class performs the localization routine that will allow the robot to be aware of where in the
 * field it is, and provide the accurate initial information to the odometer. To be modified and filled in.
 *  
 * @version 0.0
 */
public class Localizer {
	
	public static int WALL_THRESHOLD = 35;
	private Odometer odo;
	private Navigator navi;
	
	/**
	 * Default constructor
	 * @param obDetector the ObjectDetector
	 */
	public Localizer(ObjectDetector obDetector){
		
	}
	
	/**
	 * This method takes the coordinates of the landmarks of the field (enemy base, home base, and starting coordinates)
	 * and converts it so that the robot's coordinates are 0,0 and the other landmark coordinates are relative to that. 
	 * @param landmarks an array of points that represent the coordinates of the different landmarks
	 */
	public void convertCoordinates(Point2D[] landmarks){
		
	}
	
	/**
	 * This method performs the localization routine using an ultrasonic sensor. By the end of it, the robot should have moved to its 0,0 coordinate 
	 * (based on the new coordinate system as defined in the previous method)
	 */
	public void localize(){
		//will likely have many methods within this routine. 
	}
	
}
