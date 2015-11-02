/**
 * An autonomous robot capable of finding and manipulating Styrofoam blocks, 
 * while navigating within an enclosed area populated with known obstacles randomly placed with a 12Õ x 12Õ enclosure.
 * 
 * This is the main robot class from which the program will be executed and all the operations will be called.
 * 
 * @author Solvie Lee
 * @version 1.0
 * 
 */


package robot;

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
	private static final EV3LargeRegulatedMotor armMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("B"));
	private static final Port usPort = LocalEV3.get().getPort("S1");		
	private static final Port colorPort = LocalEV3.get().getPort("S2");
	private static Navigator navi;
	private static Odometer odo;
	private static ObjectDetector obDetector;
	private static final int NUM_OBSTACLES = 20;

	public static void main(String[] args){
		//Set up the ultrasonic sensor
		SensorModes usSensor = new EV3UltrasonicSensor(usPort);
		SampleProvider usValue = usSensor.getMode("Distance");
		float[] usData = new float[usValue.sampleSize()];
		
		//Set up the color sensor
		SensorModes colorSensor = new EV3ColorSensor(colorPort);
		SampleProvider colorValue = colorSensor.getMode("ColorID");//TODO: change mode?
		float[] colorData = new float[colorValue.sampleSize()];
	
		//Set up display
		Display display = new Display();
		
		odo = new Odometer(leftMotor, rightMotor, 20, true);
		navi = new Navigator(odo);
		obDetector = new ObjectDetector(navi, usValue, usData, colorValue, colorData);
		
		int option = 0;
		while (option == 0)
			option = Button.waitForAnyPress(); // ID Right executes
		
		switch (option) {
		case Button.ID_RIGHT:
			execute();
			break;
		default:
			System.out.println("Error - invalid button");
			System.exit(-1);
			break;
		}
	}
	/**
	 * This method executes the main program. The robot will wait until it is given its coordinates and its 
	 * Destination, and once it determines a coordinate system relative to its being in a 0,0 position,
	 * it will perform the localization and block finding routines in sequence.
	 */
	public static void execute(){
		double[] landmarks = null; //enemy base, home base, starting position, etc.
		//TODO: package for Wifi communications not available yet on mycourses. 
		//wait for information from computer about its coordinates and enemy base, etc. 
		
		localize(landmarks);
		findEnemyBase();
		findFlag();
		captureFlag();
		returnHomeBase();
		//execute the rest of the program.
	}
	
	/**
	 * Localizes to the coordinates given
	 */
	public static void localize(double[] landmarks){
		//instantiate localizer
		Localizer loca = new Localizer(navi, obDetector);
		//convert coordinates so that they are relative to the robot's 0,0.
		loca.convertCoordinates(landmarks);
		//perform localization routine.
		loca.localize();
		
	}
	/**
	 * This method uses the navigator to go to the enemy base while avoiding obstacles along the way
	 */
	public static void findEnemyBase(){
		double x=0, y=0;
		//TODO set x and y to the coordinates of the enemy base 
		
		// instantiate pathfinder and empty obstacles array
		Point2D[] obstacles = new Point2D[NUM_OBSTACLES];
		PathFinder pathFinder = new PathFinder(navi);
		//go to the path. 
		pathFinder.findPathTo(x, y, obstacles);
		
	}
	/**
	 * This method allows the robot to search the blocks in the enemy zone to find the flag
	 */
	public static void findFlag(){
		// search enemy zone for the flag
	}
	
	/**
	 * This method uses the robot's arm to capture the flag
	 */
	public static void captureFlag(){
		//capture the flag with the arm
	}
	
	/**
	 * This method allows the robot to return to the home base and drop the block
	 */
	public static void returnHomeBase(){
		//return the flag to the destination
	}
	
}
