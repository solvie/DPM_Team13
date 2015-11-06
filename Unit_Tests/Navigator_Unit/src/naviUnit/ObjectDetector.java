package naviUnit;

import java.util.ArrayList;
import java.util.Collections;

import lejos.robotics.SampleProvider;
import lejos.utility.Delay;
import lejos.utility.Timer;
import lejos.utility.TimerListener;

public class ObjectDetector implements TimerListener{
	
	private static final int DEFAULT_INTERVAL = 25, OBJECT_CLOSE = 15, OBJECT_FAR = 90;
	private Navigator navi;
	private Odometer odo;
	private Timer timer;
	private boolean objectDetected, objectClose, objectColorSeen, flagDetected;
	private SampleProvider usSensor, colorSensor;
	private int distance, filter;
	private int filter_out=10;
	private float[] usData, colorData;
	private ArrayList<Integer> Data=new ArrayList<Integer>();
	
	public ObjectDetector(Navigator navi, SampleProvider usValue, float[] usData,
			SampleProvider colorValue, float[] colorData, boolean autostart) {
		this.usData = usData;
		this.colorData = colorData;
		this.navi = navi;
		this.odo = null;
		this.timer = new Timer(DEFAULT_INTERVAL, this);
		if (autostart)
			timer.start();
	}
	
	public void timedOut(){
		//Check sensor data every time 	the timer times out
		synchronized(this){
			distance = filter(distance);
		}
		updateNavi(OBJECT_FAR, OBJECT_CLOSE);
		}
	
	public int filter(int distance){
		int lastdis = distance;
		Data.clear();
		for(int i = 0; i<46; i++){
			Data.add(this.getrawdistance());
			Delay.usDelay(500);
		}
		Collections.sort(Data);
		int currentdis = Data.get(Data.size()/2);
		int diff = Math.abs(currentdis-lastdis);
		if(diff>20 && filter<filter_out){
			filter++;
		}
		else{
			distance = currentdis;
			filter = 0;
		}
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
		distanceNColor[1] = getcolor();
		return distanceNColor;
	}
	
	public int getrawdistance(){
		synchronized(this){
			usSensor.fetchSample(usData, 0);
			int rawdistance = (int)(usData[0]*100.0);
			if (rawdistance==Integer.MAX_VALUE){
				rawdistance = 255;}
			return rawdistance;
			}
	}
	
	private int getcolor(){
		synchronized(this){
			colorSensor.fetchSample(colorData,0);
			int color = (int)(colorData[0]*100);
			return color;
		}
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
