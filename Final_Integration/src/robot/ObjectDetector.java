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
	private SampleProvider usValue, colorSensor, colorSensor2;
	private int  distance, realdistance, lastcolor2, deltacolor2, filter, filter_out = 10;
	private float[] usData, colorData, colorData2;
	private Timer timer;
	private Navigator navi;
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
			SampleProvider colorSensor, float[] colorData, SampleProvider colorSensor2, float[] colorData2, boolean autostart){
		this.objectDetected = false;
		this.usValue = usValue;
		this.usData = usData;
		this.colorSensor = colorSensor;
		this.colorData = colorData;
		this.colorSensor2 = colorSensor2;
		this.colorData2 = colorData2;
		this.realdistance = getdistance();
		this.navi = navi;
		this.timer = new Timer(DEFAULT_INTERVAL, this);
		if (autostart)
			timer.start();
	}
	
	
	/**
	 * This method checks the sensor data whenever it is called, and puts it through the filter.  It also tells the navigator whether an object/flag was seen or not. 
	 */
	@Override
	public void timedOut(){
		int lastrealdis = realdistance;
		Data.clear();
		for(int i =0; i<46; i++){
			Data.add(this.getdistance());
			Delay.usDelay(500);
		}
		Collections.sort(Data);
		int currentrealdis = Data.get(Data.size()/2);
		synchronized(this){
			int diff = Math.abs(currentrealdis - lastrealdis);
			if (diff>20 && filter<filter_out){
				filter++;
			}else{
				realdistance = currentrealdis;
				filter = 0;
			}
			deltacolor2 = this.getcolor2() - lastcolor2;
			lastcolor2 = this.getcolor2();
			
			//distance = filter(distance);
		
		}
			//Tell the navi the information.
		updateNavi(OBJECT_FAR, OBJECT_CLOSE);
		
	}
	
	
	
	/**
	 * 
	 * @return raw data
	 */
	public int getdistance(){
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
		if (realdistance <= farDist)
			objectDetected = true;
		else
			objectDetected = false;
		//notify navi if object is within closeDist away
		if(realdistance<= closeDist)
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
	 * Color sensor in the front
	 * @return
	 */
	public int getcolor1(){
		synchronized (this){
			colorSensor.fetchSample(colorData, 0);
			int color=(int)(colorData[0]*100);	
			return color;
		}
	}
	
	/**
	 * Color sensor in the back
	 * @return
	 */
	public int getcolor2(){
		synchronized (this){
			colorSensor2.fetchSample(colorData2, 0);
			int color=(int)(colorData2[0]);	
			return color;
		}
	}
	
	/**
	 * Get filtered distance value
	 * @return
	 */
	public int getrealdis(){
		synchronized (this){
			return realdistance;}
	}
	
	/**
	 * Get delta value of second color sensor
	 * @return
	 */
	public int getdeltacolor2(){
		synchronized (this){
			return deltacolor2;}
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
