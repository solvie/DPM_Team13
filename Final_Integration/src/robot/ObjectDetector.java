package robot;

import lejos.robotics.SampleProvider;
import lejos.utility.TimerListener;
import lejos.utility.Timer;

/**
 * The object detector class is responsible for using the ultrasonic sensor and the light sensor to detect
 * objects in front of the robot. 
 * 
 * @version 0.5
 *
 */

public class ObjectDetector implements TimerListener {
	
	private static final int DEFAULT_INTERVAL = 20, OBJECT_CLOSE = 15, OBJECT_FAR = 90;
	private Navigator navi;
	private Odometer odo;
	private Timer timer;
	private boolean objectDetected, objectClose, colorSeen, flagDetected;
	private SampleProvider usSensor, colorSensor;
	private double distance;
	private float[] usData, colorData;
	
	/**
	 * Default constructor
	 * @param navi the navigator
	 * @param usSensor the SampleProvider for ultrasonic values
	 * @param usValue the ultrasonic values from the SampleProvider
	 * @param colorSensor the SampleProvider for color values
	 * @param colorValue the color values from the SampleProvider.
	 */
	public ObjectDetector(Navigator navi, SampleProvider usSensor, float[] usData, SampleProvider colorSensor, float[] colorData, boolean AUTOSTART){
		this.navi = navi;
		this.odo = navi.getOdo();
		this.timer = new Timer(DEFAULT_INTERVAL, this);
		if (AUTOSTART)
			this.timer.start();
	}
	
	/**
	 * This method checks the sensor data whenever it is called. It also tells the navigator whether an object/flag was seen or not. 
	 */
	public void timedOut(){
	//Check sensor data every time 	the timer times out
		usSensor.fetchSample(usData,0);
		distance = (usData[0] * 100);
		distance = filter(distance);
		colorSensor.fetchSample(colorData, 0);
		//color = something.
	//Tell the navi the information.
		updateNavi(OBJECT_FAR, OBJECT_CLOSE);
		
	}
	
	/**
	 * This is a temporary placeholder method to filter the distance data. 
	 * @param distance data fetched from the ultrasonic. 
	 */
	public double filter(double distance){
		//placeholder filtering method.
		return distance;
	}
	
	/**
	 * This method allows the object detector to tell the navigator the state of the field of vision.
	 * 
	 * @param farDist Distance within which the object detector will say an object has been detected
	 * @param closeDist Distance within which the object detector will say an object is close
	 */
	public void updateNavi(double farDist, double closeDist){
		//notify navi if object 
		if (distance <= farDist)
			objectDetected = true;
		else
			objectDetected = false;
		//notify navi if object is within closeDist away
		if(distance<= closeDist)
			objectClose = true;
		else
			objectClose = false;
		
		navi.setDetectionInfo(new boolean[]{objectDetected, objectClose, false},new boolean[]{true, true, false});
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
