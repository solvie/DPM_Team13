package robot;

import java.util.ArrayList;
import java.util.Collections;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;
import lejos.utility.TimerListener;
import lejos.utility.Timer;

/**
 * The object detector class is responsible for using the ultrasonic sensor and the light sensor to detect
 * objects in front of the robot. 
 * 
 * @version 1.0
 * @author Solvie Lee, Shawn Lu
 *
 */

public class ObjectDetector implements TimerListener {
	
	public static final int errorMargin = 2, DEFAULT_INTERVAL = 25, offFactor = 20;
	private static final int OBJECT_CLOSE = 7, OBJECT_FAR = 15, MAX_RANGE = 120;
	private boolean objectDetected, objectClose, objectColorSeen, flagDetected;
	private int i =0;
	private SampleProvider usValue, colorValue;
	private int color, blockDetected, distance, filter, filter_out = 10;
	private float[] usData, colorData;
	private Timer timer;
	private Navigator navi;
	private Odometer odo;
	private ArrayList<Integer> Data = new ArrayList<Integer>();
	
	/**
	 * Default constructor
	 * @param navi the navigator
	 * @param usValue the SampleProvider for ultrasonic values
	 * @param usData the ultrasonic values from SampleProvider
	 * @param colorValue the SampleProvider for color values
	 * @param colorData the color values from SampleProvider
	 * @param autostart whether the timer should autostart
	 */
	public ObjectDetector(Navigator navi, SampleProvider usValue, float[] usData,
			SampleProvider colorValue, float[] colorData, boolean autostart){
		this.objectDetected = false;
		this.blockDetected = -1;
		this.distance = MAX_RANGE; // initialize distance to farthest.
		this.usValue = usValue;
		this.colorValue = colorValue;
		this.usData = usData;
		this.colorData = colorData;
		this.navi = navi;
		this.odo = null;
		this.timer = new Timer(DEFAULT_INTERVAL, this);
		if (autostart)
			timer.start();
	}
	
	
	/**
	 * This method checks the sensor data whenever it is called, and puts it through the filter.  It also tells the navigator whether an object/flag was seen or not. 
	 */
	public void timedOut(){
		synchronized(this){
		distance = filter(distance);
		}
		//Tell the navi the information.
		updateNavi(OBJECT_FAR, OBJECT_CLOSE);
		
	}
	
	/**
	 * This method filters the values from the sensor
	 * @param distance
	 * @return distance value
	 */
	private int filter(int distance){
		int lastdis=distance;
				Data.clear();
				for(int i=0;i<46;i++){
					Data.add(this.getrawdistance());
					Delay.usDelay(500);
				}
				Collections.sort(Data);
				int currentdis=Data.get(Data.size()/2);
				int diff=Math.abs(currentdis-lastdis);
				if(diff>20 && filter<filter_out){
					filter++;
				}else{
					distance=currentdis;
					filter=0;
				}
				return distance;
	}
	
	/**
	 * This is a helper method for the filter method
	 * @return raw data
	 */
	public int getrawdistance(){
		synchronized (this){
						usValue.fetchSample(usData, 0);
						int rawdistance = (int)(usData[0]*100.0);
						// filter infinity
						if(rawdistance==Integer.MAX_VALUE){
							rawdistance=255;}
						return rawdistance;
					}
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
	public boolean[] getVisionStatus(){
		boolean[] status = new boolean[4];
		status[0] = objectDetected;
		status[1] = objectClose;
		status[2] = objectColorSeen;
		status[3] = flagDetected;
		return status;
	}
	
	/**
	 * This method allows outside classes to access the ObjectDetector's navigator.
	 * @return Navigator
	 */
	public Navigator getNavi(){
		return navi;
	}
	
	/**
	 * Method to start the timer
	 */
	public void start(){
		timer.start();
	}

	/**
	 * Method to stop the timer
	 */
	public void stop(){
		timer.stop();
	}

}
