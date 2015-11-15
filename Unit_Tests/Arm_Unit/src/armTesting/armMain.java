package armTesting;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.Port;
//import lejos.hardware.lcd.TextLCD;
//import lejos.utility.Timer;
//import lejos.utility.TimerListener;
import lejos.hardware.Button;

public class armMain{

	private static Port armPort = LocalEV3.get().getPort("C");
	private static final EV3LargeRegulatedMotor armMotor = new EV3LargeRegulatedMotor(armPort);
	//private static Display display;
	private static armMotion blocArm;
	
	public static void main(String[] args){		
	
		//Use display to show arm options
		
		
		//Use buttons to call armMotion methods
		blocArm = new armMotion(armMotor);
		int buttonChoice;
		buttonChoice = Button.ID_ENTER;
		while(buttonChoice !=Button.ID_ESCAPE){
			buttonChoice=Button.waitForAnyPress();
			if(buttonChoice==Button.ID_UP){
				blocArm.up();
			}
			if(buttonChoice==Button.ID_DOWN){
				blocArm.down();
			}
			else{
				continue;
			}
		}
	}
}