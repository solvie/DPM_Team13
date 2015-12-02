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
//		arm.setStallThreshold(6, 20);
	}
	
	public synchronized void up(){
//		arm.setSpeed(100);
		arm.setAcceleration(200);
		
//		arm.rotate(-145,false);
//		while(!arm.isStalled()){
//			arm.rotate(-3,true);}
//		arm.rotate(15,false);
		
		arm.rotateTo(-160);
	}
	
	public synchronized void down(){
//		arm.setSpeed(100);
		arm.setAcceleration(250);
		
//		arm.rotate(170,false);
//		arm.flt(true);
		
//		while(!arm.isStalled()){
//			arm.rotate(8,true);}
//		arm.rotate(-15,true);
		
		arm.rotateTo(190);
		arm.flt(true);
	}
	
	public synchronized void throwaway(){
//		arm.setSpeed(1600);
		arm.setAcceleration(1200);
		
//		arm.rotate(-180,true);
//		while(!arm.isStalled()){
//			arm.rotate(-10,true);}
//		arm.rotate(15,true);
		
		arm.rotateTo(-180);
		
	}
	
}
