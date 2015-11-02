package robot;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;
import lejos.utility.Timer;
import lejos.utility.TimerListener;
/**
 * This class will display data such as the robot's position and whether there is an object in front of it.
 * 
 * @version 0.0
 *
 */
public class Display implements TimerListener {

	public static final int LCD_REFRESH = 200;
	private Odometer odo;
	private Timer lcdTimer;
	private TextLCD LCD = LocalEV3.get().getTextLCD();
	private double[] pos;
	private boolean objectDetected;
	private int pt, blockDetected, distance, color;
	
	public Display() {
		this.odo = null;
		this.lcdTimer = new Timer(LCD_REFRESH, this);
		this.objectDetected = false;
		this.blockDetected = -1;
		this.pos = new double[3];
		this.pt = 0;
		lcdTimer.start();
	}

	public void timedOut() {
		// do case switch with this. 
		if (pt ==0){
			displayOne();
		}
		else{
			if (pt==1)
				displayLocalization();
		}
	}
	
	public void setPart(int pt){
		this.pt = pt;
	}
	
	public void displayOne(){//Displays start screen
		LCD.clear();
		LCD.drawString("Press Right", 0, 0);
	}
	public void displayLocalization(){ //Displays information
		//TODO
		
	}
	public void displayNavigation(){ //Displays information
		//TODO
		
	}
	public void displayBlockFinding(){ //Displays information
		//TODO
		
	}
	public void displayBlockCapturing(){ //Displays information
		//TODO
		
	}
	
	
}