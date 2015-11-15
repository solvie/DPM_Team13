
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

/**
 * An autonomous robot capable of finding and manipulating Styrofoam blocks, 
 * while navigating within an enclosed area populated with known obstacles randomly placed with a 12 x 12 enclosure.
 * 
 * This is the main robot class from which the program will be executed and all the operations will be called.
 * 
 * @version 1.0
 * @author Solvie Lee
 *
 */
public class Robot {
	private static final EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("A"));
	private static final EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("D"));
	private static final EV3LargeRegulatedMotor armMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("B"));
	private static final Port usPort = LocalEV3.get().getPort("S1");		
	private static final Port colorPort = LocalEV3.get().getPort("S2");
	private static final Port colorPort2 = LocalEV3.get().getPort("S3");
	private static Display display;
	private static Navigator navi;
	private static Odometer odo;
	private static ObjectDetector obDetector;
	private static Localizer loca;
	private static PathFinder pathFinder;
	private static FlagFinder flagFinder;
	private static FlagCapturer flagCapturer;
	private static Point2D[] obstacles, landmarks;
	private static final int NUM_OBSTACLES = 20;
	

	public static void main(String[] args){
		//Set up the ultrasonic sensor
		SensorModes usSensor = new EV3UltrasonicSensor(usPort);
		SampleProvider usValue = usSensor.getMode("Distance");
		float[] usData = new float[usValue.sampleSize()];
		
		//Set up the color sensor for object detection
		SensorModes colorSensor = new EV3ColorSensor(colorPort);
		SampleProvider colorValue = colorSensor.getMode("ColorID");//TODO: change mode?
		float[] colorData = new float[colorValue.sampleSize()];
		
		//Set up light sensor for odo
		SensorModes colorSensor2 = new EV3ColorSensor(colorPort);
		SampleProvider colorValue2 = colorSensor.getMode("Red");//TODO: change mode?
		float[] colorData2 = new float[colorValue.sampleSize()];
	
		//Set up display
		display = new Display();
		display.setPart(0);
		
		odo = new Odometer(leftMotor, rightMotor);
		navi = new Navigator(odo);
		obDetector = new ObjectDetector(navi, usValue, usData, colorValue, colorData, true);
		
		obstacles = new Point2D[NUM_OBSTACLES];
		
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
		landmarks = null; //SET enemy base, home base, starting position, etc.
		//(package for Wifi communications not available yet on mycourses.) 
		
		//TODO: wait for information from computer about its coordinates and enemy base, etc. 
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
	public static void localize(Point2D[] landmarks){
		display.setPart(1);
		//instantiate localizer
		loca = new Localizer(obDetector);
		//convert coordinates so that they are relative to the robot's 0,0.
		loca.convertCoordinates(landmarks);
		//perform localization routine.
		loca.localize();
		
	}
	/**
	 * This method uses the navigator to go to the enemy base while avoiding obstacles along the way
	 */
	public static void findEnemyBase(){
		display.setPart(2);
		double x=0, y=0;
		//TODO set x and y to the coordinates of the enemy base 
		
		// instantiate pathfinder and empty obstacles array
		pathFinder = new PathFinder(obDetector);
		//go to the path. 
		pathFinder.findPathTo(x, y, obstacles);
		
	}
	/**
	 * This method allows the robot to search the blocks in the enemy zone to find the flag
	 */
	public static void findFlag(){
		display.setPart(3);
		// search enemy zone for the flag
		Point2D[] enemyZone= null; //TODO set this to enemy zone coordinates
		flagFinder = new FlagFinder(obDetector);
		flagFinder.search(enemyZone);
	}
	
	/**
	 * This method uses the robot's arm to capture the flag
	 */
	public static void captureFlag(){
		display.setPart(4);
		//capture the flag with the arm
		flagCapturer = new FlagCapturer(armMotor);
		flagCapturer.capture();
	}
	
	/**
	 * This method allows the robot to return to the home base and drop the block
	 */
	public static void returnHomeBase(){
		display.setPart(2);
		//return the flag to the destination
		double x=0, y=0;
		//TODO set x and y to the coordinates of the home base
		pathFinder.findPathTo(x,y, obstacles);
		
	}
	
}
