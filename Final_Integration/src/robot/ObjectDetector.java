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
	
	/**
	 * Default constructor
	 * @param navi the navigator
	 * @param usSensor the SampleProvider for ultrasonic values
	 * @param usValue the ultrasonic values from the SampleProvider
	 * @param colorSensor the SampleProvider for color values
	 * @param colorValue the color values from the SampleProvider.
	 */
	public ObjectDetector(Navigator navi, SampleProvider usSensor, float[] usValue, SampleProvider colorSensor, float[] colorValue, boolean AUTOSTART){
		this.navi = navi;
		this.odo = navi.getOdo();
		this.timer = new Timer(DEFAULT_INTERVAL, this);
		if (AUTOSTART)
			this.timer.start();
	}
	
	/**
	 * This method checks the sensor data whenever it is called.
	 */
	public void timedOut(){
	//check sensor data every time 	the timer times out
		
	}
	
	/**
	 * This method allows outside classes to get all the information from the objectDetector, whether an object was seen, whether its color was seen, etc.
	 * @param status the array of boolean values to be filled with the information requested. 
	 */
	public boolean[] getVisionStatus(boolean[] status){
		status[0] = objectDetected;
		status[1] = colorSeen;
		status[2] = flagDetected;
		return status;
	}
	
	/**
	 * This method allows outside classes to access the ObjectDetector's navigator.
	 * @return Navigator
	 */
	public Navigator getNavi(){
		return navi;
	}

}
