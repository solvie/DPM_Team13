package naviUnit;

import java.util.ArrayList;
import java.util.Collections;

import lejos.robotics.SampleProvider;
import lejos.utility.Delay;
import lejos.utility.Timer;
import lejos.utility.TimerListener;

public class ObjectDetector implements TimerListener{
	
	public static final int errorMargin = 2, DEFAULT_INTERVAL = 25, offFactor = 20;
	private static final int OBJECT_FAR = 50, OBJECT_CLOSE = 5, OBJECT_CLOSEISH = 15, MAX_RANGE = 120;
	private boolean objectDetected, objectCloseish, objectClose, objectColorSeen, flagDetected;
	private int i =0;
	private SampleProvider usValue, colorValue;
	private int color, blockDetected, distance, filter, filter_out = 10;
	private float[] usData, colorData;
	private Timer timer;
	private Navigator navi;
	private Odometer odo;
	private ArrayList<Integer> Data = new ArrayList<Integer>();
	
	public ObjectDetector(Navigator navi, SampleProvider usValue, float[] usData,
			SampleProvider colorValue, float[] colorData, boolean autostart) {
		this.objectDetected = false;
		this.objectCloseish = false;
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
	
	public void timedOut(){
		synchronized(this){
			distance = filter(distance);
		}
		//Tell the navi the information.
		updateNavi(OBJECT_FAR, OBJECT_CLOSEISH, OBJECT_CLOSE);
		}
	
	
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
	
	private int getcolor(){
		synchronized(this){
			colorValue.fetchSample(colorData, 0);
			int color = (int) (colorData[0]*100);
			return color;
		}
	}
	
	public void updateNavi(double farDist, double closeishDist, double closeDist){
		//notify navi if object 
		if (distance <= farDist)
			objectDetected = true;
		else
			objectDetected = false;
		//notify navi if object closeish 
		if (distance <= closeishDist)
			objectCloseish = true;
		else
			objectCloseish = false;
		//notify navi if object is within closeDist away
		if(distance<= closeDist)
			objectClose = true;
		else
			objectClose = false;
		
		navi.setDetectionInfo(new boolean[]{objectDetected, objectCloseish, objectClose, false},new boolean[]{true, true, true, false});
		navi.setObjectDist(distance);
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
