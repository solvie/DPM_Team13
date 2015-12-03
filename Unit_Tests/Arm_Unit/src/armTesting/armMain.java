package armTesting;

//import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.ev3.LocalEV3;
//import lejos.hardware.port.Port;
import lejos.hardware.Button;

/**
 * This is the class that contains the main method required to run the robot while testing the arm mechanism.
 * In order to verify the arm's effectiveness, this class contains the sequence code which allows the robot to
 * test the different facets of the arm. This includes its ability to lift the block once detected, its ability
 * to place the block down once it is in the endzone, and its ability to retain the block in its grip while the
 * robot is in motion.
 * 
 * @author Scotty Conrad
 *
 */
public class armMain{

	private static final EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("A"));
	private static final EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("D"));
	private static final EV3LargeRegulatedMotor usMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("B"));
	private static final EV3LargeRegulatedMotor armMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("C"));
	private static final double leftRadius = 2.04;
	private static final double rightRadius = 2.04;
	private static final double width = 15.5;
	private static armMotion blockArm;
	private static armMotion usPivot;
	private static Display screenInfo;
	final static int FAST = 400, SLOW = 100, ACCELERATION = 1200;
	private static final double reverseDist = -10.00;
	private static final double forwardDist = 15.00;
	
	/**
	 * Method which from which the arm unit project runs. Can be summarized as a while loop which allows the user
	 * to give different commands in sequence through the brick's control pad. The while loop terminates when 
	 * the user hits the escape button on the robot.
	 * 
	 * @param args for running the Java project.
	 */
	public static void main(String[] args){		
	
		/*Use display to show arm command options. Displays the following options:
		*-Up is for lifting the arm.
		*-Down is for dropping the arm.
		*-Left makes the robot pivot.
		*-Right makes the robot drive in a square.
		*/
		screenInfo = new Display();
		screenInfo.displayOne();
		
		/*Buttons on the robot each give a separate command. While loop allows for many commands to be given
		 * in sequence. Sequential button hits only registered if previous routine is done. Escape button
		 * terminates the loop and exits the program.
		 */
		//Instantiate ev3motors as armMotions to have access to design specific motions.
		blockArm = new armMotion(armMotor);
		usPivot = new armMotion(usMotor);
		int buttonChoice;
		buttonChoice = Button.ID_ENTER;
		leftMotor.setAcceleration(ACCELERATION);
		rightMotor.setAcceleration(ACCELERATION);
		/*This loop allows the continuous input of new action commands by the tester. Not necessary for
		 *final integration. Robot exits the program when button press is ID_ESCAPE.
		 */
		while(buttonChoice !=Button.ID_ESCAPE){
			buttonChoice=Button.waitForAnyPress();
			
			//Runs the motion sequence for lifting a block. Includes repositioning of Ultrasonic Sensor.
			if(buttonChoice==Button.ID_UP){
				leftMotor.rotate(convertDistance(leftRadius, forwardDist), true);
				rightMotor.rotate(convertDistance(rightRadius, forwardDist), false);
				blockArm.up();
				usPivot.untuck();
			}
			
			//Runs motion sequence for placing block. Includes the hiding of US to make room for descending arm.
			if(buttonChoice==Button.ID_DOWN){
				leftMotor.rotate(convertDistance(leftRadius, reverseDist), true);
				rightMotor.rotate(convertDistance(rightRadius, reverseDist), false);
				usPivot.tuck();
				blockArm.down();
			}
			
			//Has the robot spin on one spot to test if it retains block while turning at high speeds.
			if(buttonChoice==Button.ID_LEFT){
				int curSpeed = FAST;
				int count = 0;
				
				//Forces the robot to shimmy as if it was listening to "Twist and Shout".
				while (buttonChoice!=Button.ID_ESCAPE&&count<=12) {
	
					leftMotor.setSpeed(curSpeed);
					rightMotor.setSpeed(-curSpeed);

					leftMotor.rotate(convertAngle(leftRadius, width, 30.0), true);
					rightMotor.rotate(-convertAngle(rightRadius, width, 30.0), false);
					
					leftMotor.rotate(-convertAngle(leftRadius, width, 30.0), true);
					rightMotor.rotate(convertAngle(rightRadius, width, 30.0), false);
					
					//update to change the speed and eventually exit
					count ++;
					buttonChoice = Button.waitForAnyPress(500);
				}
				break;
			}
			
			/*Has the robot drive in a small square to test if it retains the block while accelerating to high
			 * speeds.
			 */
			if(buttonChoice==Button.ID_RIGHT){
				int curSpeed = FAST;
				int count = 0;
				while (buttonChoice!=Button.ID_ESCAPE&&count<=5) {
					
					// drive forward roughly 40cm
					leftMotor.setSpeed(curSpeed);
					rightMotor.setSpeed(curSpeed);

					leftMotor.rotate(convertDistance(leftRadius, 40), true);
					rightMotor.rotate(convertDistance(rightRadius, 40), false);

					leftMotor.setSpeed(FAST);
					rightMotor.setSpeed(FAST);

					// turn 90 degrees clockwise
					leftMotor.rotate(convertAngle(leftRadius, width, 90.0), true);
					rightMotor.rotate(-convertAngle(rightRadius, width, 90.0), false);
					
					//update to change the speed and eventually exit
					curSpeed = (int) (curSpeed*1.3);
					count ++;
					buttonChoice = Button.waitForAnyPress(500);
				}
				break;
			}
			
			/*TODO: Create one last case for the center button that has the robot move really fast forward,
			 * then really fast backward. Will allow to test block retention in reverse motion.
			 */
			else{
				continue;
			}
		}
	}
	/**
	 * Takes the radius of the front wheels on a two wheel robot device, as well as a program defined drive
	 * distance, then feeds that data into the robot in preferred units to have it drive the 'distance'.
	 * @param radius Radius of the robot's wheels, in cm
	 * @param distance Distance to drive the wheel, in cm
	 * @return The angle that the robot wants, specifically for the "rotate()" and "rotateTo()" methods.
	 */
	private static int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}
	/**
	 * 
	 * @param radius Radius of the robot's wheels, in cm
	 * @param width Distance between the center of the robot's two front wheels.
	 * @param angle The turn angle you want the robot to undertake.
	 * @return	The robot wheel rotation angle necessary to achieve the turn angle desired by the robot.
	 */
	private static int convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, Math.PI * width * angle / 360.0);
	}
}