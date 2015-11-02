package robot;

/**
 * This class contains all the methods to allow for searching the enemy base for the flag. Once the flag is found and recognized
 * using the object detection information from the ObstacleDetector the FlagCapturer class is responsible for manipulating the arm of the robot to grab the flag. 
 * 
 * @version 0.0
 * 
 */
public class FlagFinder {

	private Navigator navi;
	private Odometer odo;
	private ObjectDetector obDetector;
	private boolean objectdetected, flagDetected;
	
	public FlagFinder(ObjectDetector obDetector){
		this.obDetector = obDetector;
		this.navi = obDetector.getNavi();
		this.odo = obDetector.getNavi().getOdo();
		
	}
	
	public void search(){
		//execute a search sequence. Will likely have many sub methods inside. 
	}
	

	
}
