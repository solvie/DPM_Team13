package robot;

import lejos.hardware.motor.EV3LargeRegulatedMotor;

/**
 * This class is responsible for manipulating the arm to pick up the flag once it has been found.
 * 
 * @version 0.0
 */

public class FlagCapturer {

	private EV3LargeRegulatedMotor armMotor;
	
	/**
	 * Default constructor
	 * @param armMotor the motor that controls the arm of the robot. 
	 */
	public FlagCapturer(EV3LargeRegulatedMotor armMotor){
		this.armMotor = armMotor;
	}

	/**
	 * Method to manipulate arm to grab the block that is the flag
	 */
	public void capture() {
		// TODO Auto-generated method stub
		
	}
	
}
