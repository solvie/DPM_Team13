package naviUnit;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;
import lejos.robotics.geometry.Point2D;

public class Robot {

	private static final EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("A"));
	private static final EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("D"));
	private static final Port usPort = LocalEV3.get().getPort("S1");		
	private static final Port colorPort = LocalEV3.get().getPort("S2");
	private static final int NUM_OBSTACLES = 20;
	private static SensorModes usSensor, colorSensor;
	private static SampleProvider usValue, colorValue;
	private static float[] usData, colorData;
	private static Odometer odo;
	private static Navigator navi;
	private static ObjectDetector obDetector;
	private static Display display;
	
	public static void main(String[] args){
		//Set up the ultrasonic sensor
		usSensor = new EV3UltrasonicSensor(usPort);
		usValue = usSensor.getMode("Distance");
		usData = new float[usValue.sampleSize()];
		
		//Set up the color sensor
		colorSensor = new EV3ColorSensor(colorPort);
		colorValue = colorSensor.getMode("ColorID");//TODO: change mode?
		colorData = new float[colorValue.sampleSize()];
		
		//Set up block detector
		odo = new Odometer(leftMotor, rightMotor, 20, true);
		navi = new Navigator(odo);
		obDetector = new ObjectDetector(navi, usValue, usData, colorValue, colorData, false);
		
		//Set up display
		display = new Display();
		display.setParameters(obDetector, 0);
		
		
		int option = 0;
		while (option == 0)
			option = Button.waitForAnyPress(); // ID (option) determines what part of the lab to run.
		
		
		switch (option) {
		case Button.ID_RIGHT: // Pt 2 selected
			runTest();
			break;
		default:
			System.out.println("Error - invalid button");
			System.exit(-1);
			break;
		}
	}
	
	public static void runTest(){
		
		display.setParameters(obDetector, 1);
		PathFinder pathFinder = new PathFinder(obDetector);
		Point2D[] obstacles = new Point2D[NUM_OBSTACLES];
		obDetector.start();
		pathFinder.findPathTo(90,90, obstacles);
	
		int option = 0;
		while (option == 0)
			option = Button.waitForAnyPress();
		
		if (option == Button.ID_LEFT)
			System.exit(0);
	}
	
	
	
}
