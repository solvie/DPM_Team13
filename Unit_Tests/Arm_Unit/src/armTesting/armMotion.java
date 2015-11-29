package armTesting;

import lejos.hardware.motor.EV3LargeRegulatedMotor;

/**
 * This class contains all the methods required for using the block grabbing arm. Its purpose is to clearly define
 * the different motions required for grabbing and dropping a block. Each method defines different arm
 * motions used for specific purposes.
 * 
 * @author Scotty Conrad
 *
 */
public class armMotion {
	private EV3LargeRegulatedMotor arm;
	private static final int accel = 400;
	
	/**
	 * Default constructor for the block grabbing control module.
	 * @param armMotor Motor for controlling the arm action
	 */
	public armMotion(EV3LargeRegulatedMotor armMotor){
		this.arm=armMotor;
		arm.setAcceleration(accel);
		arm.setStallThreshold(8, 20);

	}
	
	/**
	 * Method for lifting up the arm. Should be called once the robot has advanced to have the block inside its grasp.
	 */
	public void up(){
		arm.rotate(-180,true);
		while(!arm.isStalled()){
			arm.rotate(-6,true);
		}
		arm.rotate(15,true);
	}
	
	/**
	 * Method for dropping the arm; useful for preparing to pick up a block or putting down a block.
	 */
	public void down(){
		arm.rotate(180,true);
		while(!arm.isStalled()){
			arm.rotate(6,true);
		}
		arm.rotate(-15,true);
	}
	
	/**
	 * Method for folding the ultrasonic sensor pivot, before the robot arm can be lowered.
	 */
	public void tuck(){
		arm.rotate(-90,false);
	}
	
	/**
	 * Method for unfolding the ultrasonic sensor pivot, once the arm has been lifted.
	 */
	public void untuck(){
		arm.rotate(90, false);
	}
	
}