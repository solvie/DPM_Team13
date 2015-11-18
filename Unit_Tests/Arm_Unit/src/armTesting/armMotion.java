package armTesting;

import lejos.hardware.motor.EV3LargeRegulatedMotor;


public class armMotion {
	private EV3LargeRegulatedMotor arm; 
	
	public armMotion(EV3LargeRegulatedMotor armMotor){
		this.arm=armMotor;
		arm.setAcceleration(500);
		arm.setStallThreshold(8, 20);
	}
	
	public void up(){
		arm.rotate(-180,true);
		while(!arm.isStalled()){
			arm.rotate(-6,true);}
		
	}
	
	public void down(){
		arm.rotate(180,true);
		while(!arm.isStalled()){
			arm.rotate(6,true);}
	}
	
}