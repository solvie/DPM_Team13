package robot;
import lejos.hardware.motor.EV3LargeRegulatedMotor;

/**
 * This class allows us to manipulate the arm
 * @author Scott Conrad, Shawn Lu
 * @version 2.0
 */

public class FlagCapturer {
	private EV3LargeRegulatedMotor arm; 
	
	public FlagCapturer(EV3LargeRegulatedMotor armMotor){
		this.arm=armMotor;
		arm.setStallThreshold(6, 20);
	}
	
	public synchronized void up(){
		arm.setSpeed(100);
		arm.setAcceleration(150);
		
		arm.rotate(-165,false);
		while(!arm.isStalled()){
			arm.rotate(-3,true);}
		arm.rotate(15,false);
		
//		arm.rotateTo(-50);
	}
	
	public synchronized void down(){
		arm.setSpeed(100);
		arm.setAcceleration(150);
		
		arm.rotate(170,false);
		arm.flt();
		/*
		 * 
		 *while(!arm.isStalled()){
		 *arm.rotate(2,false);}
		 *arm.rotate(-25,false);
		 */
//		arm.rotateTo(200);
//		arm.flt(true);
	}
	
	public synchronized void throwaway(){
		arm.setSpeed(300);
		arm.setAcceleration(1500);
		
		arm.rotate(-180,true);
//		arm.setStallThreshold(8, 20);
		while(!arm.isStalled()){
			arm.rotate(-8,true);}
		arm.rotate(15,true);
		
//		arm.rotateTo(-160);
//		arm.flt(true);
	}
	
}
