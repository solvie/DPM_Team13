package odometerEV3;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;

public class TestClass {
	//Ports
	private static final EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("A"));
	private static final EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("D"));

	public static void main(String[] args) {

		final Odometer odo = new Odometer(leftMotor, rightMotor);
		final ScreenDisplay screenDis = new ScreenDisplay(odo);
		SquareDriver driver = new SquareDriver();
		final Navigator navi = new Navigator(odo);

		while(Button.waitForAnyPress() != Button.ID_ENTER);
		(new Thread(){
			public void run(){
//				SquareDriver.drive(leftMotor, rightMotor, 2.05, 2.05, 12.5, odo, screenDis);
				navi.travelTo(150, 0);
//				navi.travelTo(150, 150);
//				navi.travelTo(0,  150);
//				navi.travelTo(0, 0);
			}
		}).start();

		while(Button.waitForAnyPress() != Button.ID_ESCAPE);
		System.exit(0);
	}

}
