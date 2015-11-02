package robot;

import lejos.robotics.SampleProvider;
import lejos.utility.TimerListener;
import lejos.utility.Timer;

/**
 * The object detector class is responsible for using the ultrasonic sensor and the light sensor to detect
 * objects in front of the robot. 
 * 
 * @version 0.0
 *
 */

public class ObjectDetector implements TimerListener {
	
	private static final int DEFAULT_INTERVAL = 20;
	private Navigator navi;
	private Odometer odo;
	private Timer timer;
	private boolean objectDetected, colorSeen, flagDetected;
	private SampleProvider usSensor, colorSensor;
	private float[] usValue, colorValue;
	
	
	public ObjectDetector(Navigator navi, SampleProvider usSensor, float[] usValue, SampleProvider colorSensor, float[] colorValue){
		this.navi = navi;
		this.odo = navi.getOdo();
		this.timer = new Timer(DEFAULT_INTERVAL, this);
		
	}
	
	public void timedOut(){
	//check sensor data every time 	the timer times out
		
	}
	
	/**
	 * This method gets all the information from the objectDetector, whether an object was seen, whether its color was seen, etc.
	 * @param status
	 */
	public boolean[] getVisionStatus(boolean[] status){
		status[0] = objectDetected;
		status[1] = colorSeen;
		status[2] = flagDetected;
		return status;
	}
	
	public Navigator getNavi(){
		return navi;
	}

}
