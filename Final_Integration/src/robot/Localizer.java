package robot;

import lejos.robotics.SampleProvider;

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
	
	
	public Localizer(Navigator navi, ObjectDetector obDetector){
		
	}
	
	public void convertCoordinates(double[] landmarks){
		
	}
	
	public void localize(){
		
	}
	
}
