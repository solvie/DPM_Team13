package odometerEV3;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;

public class TestClass {
	//Ports
	private static final EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("A"));
	private static final EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("B"));
	
	public static void main(String[] args) {

		final Odometer odo = new Odometer(leftMotor, rightMotor);
		final ScreenDisplay screenDis = new ScreenDisplay(odo);
		SquareDriver driver = new SquareDriver();
		
		while(Button.waitForAnyPress() != Button.ID_ENTER);
		(new Thread(){
			public void run(){
				SquareDriver.drive(leftMotor, rightMotor, 2.07, 2.07, 17.1, odo, screenDis);
			}
		}).start();

		while(Button.waitForAnyPress() != Button.ID_ESCAPE);
		System.exit(0);
	}

}
