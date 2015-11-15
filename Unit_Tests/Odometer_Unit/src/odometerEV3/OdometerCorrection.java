package odometerEV3;

import java.util.Arrays;

import lejos.hardware.Sound;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;

/** 
 * Class controlling the correction of the odometer whenever the robot
 * goes past a line
 * The correction is only made if the robot is going along either the x or y axis.
 * The correction is not activated whenever the robot navigates on a coordinates that should represent a continuous line.
 * @version 2.0
 * @author chouttle
 */
public class OdometerCorrection {
	private Odometer odo;

	//LIGHT SENSOR
	private static final Port lightPort = LocalEV3.get().getPort("S3");
	SensorModes lightSensor = new EV3ColorSensor(lightPort);
	SampleProvider lightSample = lightSensor.getMode("Red");
	float[] lightData = new float[lightSensor.sampleSize()];

	//FILTERING
	int filterSize = 5;
	int filterPointer = 0;
	double[] lightArray = new double[filterSize];

	private double distSensorCenter = 11.3;
	private double distBtwLines = 30.48;

	/**
	 * Default constructor
	 * @param odoO Odometer to be corrected
	 */
	public OdometerCorrection(Odometer odoO) {
		this.odo = odoO;
	}

	/**
	 * Function executing the correction of the odometer.
	 */
	public void run(){

		double currentX = odo.getX()-distSensorCenter;
		double currentY = odo.getY()-distSensorCenter;
		double currentAng = odo.getAng();
		int errorAngAllowed = 4;
		int errorMarginBefore = 15;
		while(true){
			currentX = odo.getX()-distSensorCenter;
			currentY = odo.getY()-distSensorCenter;
			currentAng = odo.getAng();
			boolean[] update = {false, false, false};
			double[] position = new double[3];
			if(lineDetected(getFilteredLightData())){
				if((isInRangeAngle(currentAng, errorAngAllowed, 90) || isInRangeAngle(currentAng, errorAngAllowed, 270)) && !isOnALine(currentX, 2)){
					//y axis case
					if(isInRangeCoo(currentY, errorMarginBefore, (int) distBtwLines)){
						System.out.println("Great");
						update[1] = true;
						position[1] = adaptValue(currentY, distBtwLines) + distSensorCenter;
						Sound.beep();
						odo.setPosition(position, update);
						sleep(1000);
					}
				}
				if((isInRangeAngle(currentAng, errorAngAllowed, 180) || isInRangeAngle(currentAng, errorAngAllowed, 0) || isInRangeAngle(currentAng, errorAngAllowed, 360)) && !isOnALine(currentY, 2)){
					//x axis case
					if(isInRangeCoo(currentX, errorMarginBefore, (int) distBtwLines)){
						System.out.println("Freat");
						update[0] = true;
						position[0] = adaptValue(currentX, distBtwLines) + distSensorCenter;
						Sound.beep();
						odo.setPosition(position, update);
						sleep(1000);
					}
				}
			}
		}
	}

	/**
	 * Function checking if a number is within the range of a target number
	 * @param number
	 * @param range
	 * @param target
	 * @return true if the value is within the range, false otherwise
	 */
	private boolean isInRangeAngle(double number, int range, int target){
		if(Math.abs(number-target) <range)
			return true;
		return false;
	}

	/**
	 * Function checking if a number is within the range of a the multiple of a target number
	 * @param number
	 * @param range
	 * @param target
	 * @return
	 */
	private boolean isInRangeCoo(double number, int range, int target){
		if(Math.abs((number%target)-target) <range)
			return true;
		return false;
	}

	/**
	 * Function checking if the robot is navigating on a continuous line.
	 * @param xOrY current x or y coordinates of the robot
	 * @param range small range representing the possible error of the odometer if close to the line
	 * @return
	 */
	private boolean isOnALine(double xOrY, int range){
		if(xOrY%distBtwLines<distBtwLines/2)
			return xOrY%distBtwLines < range;
		return Math.abs(xOrY%distBtwLines - distBtwLines) < range;
	}

	/**
	 * Changes the value of a number to be the closest multiple of a given multiplier
	 * @param number
	 * @param multiplier
	 * @return the closest value
	 */
	private double adaptValue(double number, double multiplier){
		if(number%multiplier<multiplier/2)
			return number - number%multiplier;
		return number + Math.abs(number%multiplier - multiplier);
	}

	/**
	 * Checks if a line is being detected by the light sensor
	 * @param value value of the light sensor
	 * @return true if a line is detected, false otherwise
	 */
	private boolean lineDetected(double value){
		if(value < 25 && value > 10)
			return true;
		return false;
	}

	/**
	 * This function filters out the data received from the light sensor using a median filter
	 * @param light data received from the light sensor
	 * @return the data filtered
	 */
	private double filterLightSensor(double light){
		//MEDIAN FILTER
		lightArray[filterPointer] = light;
		filterPointer++;
		filterPointer %= filterSize;

		double[] sortedArray = lightArray.clone();
		Arrays.sort(sortedArray);
		double median;
		double sum = 0;
		int half = filterSize/2;
		if(filterSize%2==0){
			median = 2*sortedArray[half]+1;
		}
		else{
			median = sortedArray[half];
		}
		for(int i=0; i < filterSize; i++){
			if(sortedArray[i] > median){
				sum += median;
			}
			else{
				sum += sortedArray[i];
			}
		}
		double average = sum/filterSize;
		return average;
	}

	/**
	 * Function to avoid writing a try/catch everywhere for sleeping.
	 * @param length duration of the sleep
	 */
	private void sleep(int length){
		try {
			Thread.sleep(length);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This function fetches the sample and filters the data
	 * @return the light data
	 */
	private double getFilteredLightData(){
		lightSample.fetchSample(lightData, 0);
		double light = lightData[0]*100;
		light = filterLightSensor(light);
		if(light>255){
			light = 255;
		}
		return light;
	}
}
