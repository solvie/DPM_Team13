package naviUnit;

import lejos.robotics.SampleProvider;
import lejos.utility.Timer;
import lejos.utility.TimerListener;

public class ObjectDetector implements TimerListener{
	
	public static final int errorMargin = 2, DELAY = 10, offFactor = 20;
	private static final double OBJECT_CLOSE = 15, OBJECT_FAR = 90, MAX_RANGE = 120;
	private boolean objectDetected, objectClose, objectColorSeen, flagDetected;
	private int i =0;
	private SampleProvider usValue, colorValue;
	private int color, blockDetected;
	private float[] usData, colorData;
	private Timer timer;
	private Navigator navi;
	private Odometer odo;
	private double distance;
	
	public ObjectDetector(Navigator navi, SampleProvider usValue, float[] usData,
			SampleProvider colorValue, float[] colorData, boolean autostart) {
		this.objectDetected = false;
		this.blockDetected = -1;
		this.distance = MAX_RANGE; // initialize distance to farthest.
		this.usValue = usValue;
		this.colorValue = colorValue;
		this.usData = usData;
		this.colorData = colorData;
		this.navi = navi;
		this.odo = null;
		this.timer = new Timer(DELAY, this);
		if (autostart)
			timer.start();
	}
	
	public void timedOut(){
		//Check sensor data every time 	the timer times out
		usValue.fetchSample(usData,0);
		distance = (usData[0] * 100);
		//distance = filter(distance);
		colorValue.fetchSample(colorData, 0);
		//color = something.
		//Tell the navi the information.
		updateNavi(OBJECT_FAR, OBJECT_CLOSE);
		}
	
	public double filter(double distance){
		//placeholder filtering method.
		return distance;
	}
	
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
	
	public boolean[] getVisionStatus(){
		boolean[] status = new boolean[4];
		status[0] = objectDetected;
		status[1] = objectClose;
		status[2] = objectColorSeen;
		status[3] = flagDetected;
		return status;
	}
	
	public int[] getDistanceAndColor(){
		int[] distanceNColor = new int[2];
		distanceNColor[0] = (int) distance;
		distanceNColor[1] = color;
		return distanceNColor;
	}
	
	public Navigator getNavi(){
		return navi;
	}
	
	public void start(){
		timer.start();
	}

	public void stop(){
		timer.stop();
	}
}
