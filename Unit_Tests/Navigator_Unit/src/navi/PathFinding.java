package navi;

import navi.Display;
import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.robotics.geometry.Point2D;

public class PathFinding {
/* A unit test to determine whether the robot can find a path from point A to point B given new obstacle information along the way, 
 * travelling only along lines parallel to the X and Y axes, (for the purposes of having more accurate odometry correction).
 * 
 * Therefore, it does not actually use ultrasonic data (that will be a different test) to detect blocks, but will be given new block
 * information at various intervals, from an outside class.
 */
	private static final EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("A"));
	private static final EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("D"));
	
	public static void main(String[] args){
		/* Static Resources:
		 * Left Motor to output A, Right Motor to output D
		 * Ultrasonic Sensor port to input S1, color sensor port to input S2
		 */
		
		Display display = new Display();
		
		int option = 0;
		while (option == 0)
			option = Button.waitForAnyPress(); // ID (option) 
		
		switch (option) {
		case Button.ID_UP:
			execute();
			break;
		default:
			System.out.println("Error - invalid button");
			System.exit(-1);
			break;
		}
	}
	
	public static void execute(){
		
		Odometer odo = new Odometer(leftMotor, rightMotor, 20, true);
		Navigator navi = new Navigator(odo);
		
		Point2D[] obstacles = new Point2D[10];
		
		navi.squareTravel(60, 50, obstacles);
		
	}
	
}
