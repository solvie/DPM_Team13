package naviUnit;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;
import lejos.utility.Timer;
import lejos.utility.TimerListener;

/*
 * This class displays data such as the robot's position and whether there is an object in front of it.
 * 
 */
public class Display implements TimerListener {

	public static final int LCD_REFRESH = 200;
	private Odometer odo;
	private ObjectDetector obDetector;
	private Timer lcdTimer;
	private TextLCD LCD = LocalEV3.get().getTextLCD();
	private double[] pos;
	private boolean objectDetected, objectClose;
	private int objectDetectedInt, objectCloseInt;
	private int pt, distance;

	/*
	 * Default constructor
	 */
	public Display() {
		this.odo = null;
		this.lcdTimer = new Timer(LCD_REFRESH, this);
		this.objectDetected = false;
		this.pos = new double[3];
		this.pt = 0;
		lcdTimer.start();
	}

	// Sets the
	public void setParameters(ObjectDetector bdetector, int pt) {
		this.obDetector = bdetector;
		this.odo = obDetector.getNavi().getOdo();
		this.pt = pt;

	}

	/*
	 * Displays the screen
	 */
	public void timedOut() {
		if (pt == 0) {
			displayOne();
		}
		else {
			displayTwo();

		}
	}

	public void displayOne() {
		LCD.clear();
		LCD.drawString("NAVIGATION TEST", 0, 0);
		LCD.drawString("Press right to begin", 0, 1);
	}

	public void displayTwo() {
		
		objectDetected = obDetector.getVisionStatus()[0];
		objectDetectedInt = objectDetected ? 1:0;
		objectClose = obDetector.getVisionStatus()[1];
		objectCloseInt = objectClose ? 1:0;
		distance = obDetector.getDistanceAndColor()[0];
		
		String flag = obDetector.getNavi().getOdo().getFlag();

		odo.getPosition(pos);

		LCD.clear();
		LCD.drawString("D:", 0, 0); // display distance
		LCD.drawInt(distance, 3, 0);
		LCD.drawString("H: ", 0, 1); // display heading
		LCD.drawInt((int) pos[2], 3, 1); 
		LCD.drawString("Object:", 0, 2); // display objectDetected
		LCD.drawInt(objectDetectedInt, 7, 2);
		LCD.drawString("Close:", 0, 3); // display objectClose
		LCD.drawInt(objectCloseInt, 7, 3);
		
		LCD.drawString("x:", 0, 4); // display x position
		LCD.drawInt((int)odo.getX(), 3, 4);
		LCD.drawString("y:", 0, 5); // display y position
		LCD.drawInt((int)odo.getY(), 3, 5);
		LCD.drawString(flag, 0, 6); // display flag.

	}

}
