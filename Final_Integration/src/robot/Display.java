package robot;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;
import lejos.utility.Timer;
import lejos.utility.TimerListener;
/**
 * This class will display data such as the robot's position and whether there is an object in front of it.
 * 
 * @version 1.0
 * @author Solvie Lee
 */
public class Display implements TimerListener {

	public static final int LCD_REFRESH = 200;
	private ObjectDetector obDetector;
	private Odometer odo;
	private Timer lcdTimer;
	private TextLCD LCD = LocalEV3.get().getTextLCD();
	private double[] pos;
	private boolean objectDetected, objectClose;
	private int pt, blockDetected, distance, color;
	
	/**
	 * Default constructor
	 */
	public Display(ObjectDetector obdetector) {
		this.obDetector = obdetector;
		this.odo = obdetector.getNavi().getOdo();
		this.lcdTimer = new Timer(LCD_REFRESH, this);
		this.objectDetected = false;
		this.blockDetected = -1;
		this.pos = new double[3];
		this.pt = 0;
		lcdTimer.start();
	}

	/**
	 * Displays the proper screen for each part.
	 */
	public void timedOut() {
		// do case switch with this. 
	switch(pt){	
		case (0):
			displayOne();
			break;	
		case (1):
			displayLocalization();
			break;
		case (2):
			displayNavigation();
			break;
		case (3):
			displayBlockFinding();
			break;
		case (4):
			displayBlockCapturing();
			break;
		default:
			System.exit(-1);
			break;
		}
	}
	
	/**
	 * Method that sets what part of the game the robot is in, so that it can display the relevant information.
	 * @param pt 0 for main screen, 1 for localization, 2 for navigation, 3 for block finding, 4 for capture
	 */
	public void setPart(int pt){
		this.pt = pt;
	}
	
	/**
	 * Displays the main screen before the robot has begun executing the block finding sequence
	 */
	public void displayOne(){
		LCD.clear();
		LCD.drawString("Ready to begin", 0, 0);
	}
	
	/**
	 * Displays the odometry values for localization
	 */
	public void displayLocalization(){ //Displays information
		//TODO
		LCD.drawString("Localizing", 0, 0);
		
	}
	
	/**
	 * Displays the odometry values
	 */
	public void displayNavigation(){ //Displays information
		LCD.drawString("Navigating", 0, 0);
		
		objectDetected = obDetector.getVisionStatus()[0];
		int objectDetectedInt = objectDetected ? 1:0;
		objectClose = obDetector.getVisionStatus()[1];
		int objectCloseInt = objectClose ? 1:0;
		double distance = obDetector.getrealdis();

		odo.getPosition(pos);

		LCD.clear();
		LCD.drawString("D:", 0, 1); // display distance
		LCD.drawString(String.valueOf(distance), 3, 1);
		/*LCD.drawString("H: ", 0, 1); // display heading
		LCD.drawInt((int) pos[2], 3, 1); */
		LCD.drawString("Object:", 0, 2); // display objectDetected
		LCD.drawInt(objectDetectedInt, 7, 2);
		LCD.drawString("Close:", 0, 3); // display objectClose
		LCD.drawInt(objectCloseInt, 7, 3);
		LCD.drawString("x:", 0, 4); // display x position
		LCD.drawString(String.valueOf(odo.getX()), 3, 4);
		LCD.drawString("y:", 0, 5); // display y position
		LCD.drawString(String.valueOf(odo.getY()), 3, 5);
		
	}
	
	/**
	 * Displays whether there is an object in front of it and what color it is.
	 */
	public void displayBlockFinding(){ //Displays information
		odo.getPosition(pos);
		LCD.clear();
		LCD.drawString("X: ", 0, 0);
		LCD.drawString("Y: ", 0, 1);
		LCD.drawString("H: ", 0, 2);
		LCD.drawInt(((int)(pos[0] * 10))/10, 3, 0);
		LCD.drawInt(((int)(pos[1] * 10))/10, 3, 1);
		LCD.drawInt((int)pos[2], 3, 2);
		LCD.drawString("D: "+obDetector.getdistance(), 0, 3);
		LCD.drawString("C: "+obDetector.getcolornumber(), 0, 4);
		
	}
	
	/**
	 * Display screen for when block is being captured.
	 */
	public void displayBlockCapturing(){ //Displays information
		//TODO
		
	}
	
	
}