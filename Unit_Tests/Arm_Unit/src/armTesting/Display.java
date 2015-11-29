package armTesting;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;
import lejos.utility.Timer;
import lejos.utility.TimerListener;

/**
 * This is a typical helper class which allows the user to see his options while testing the Arm Motions.
 * @author Scotty Conrad
 */
public class Display implements TimerListener {

	public static final int LCD_REFRESH = 2000;
	private Timer lcdTimer;
	private TextLCD LCD = LocalEV3.get().getTextLCD();
	private int statusNum;
	
	/**
	 *Default constructor
	 */
	public Display() {
		statusNum = 0;
		this.lcdTimer = new Timer(LCD_REFRESH, this);
		lcdTimer.start();
	}
	/**
	 * Defines the display used on the timer's action.
	 */
	public void timedOut() {
		if (statusNum == 0){
			displayOne();
		}
		else{
			if (statusNum == 1)
				displayTwo();
		}
	}
	
	/***
	 * Setter method for setting the display status. Allows for quick implementation of new displays.
	 * @param statusNum The integer which refers to particular display set up required.
	 */
	public void setStatusNum(int statusNum){
		this.statusNum = statusNum;
	}
	
	/**
	 * This display is that used for showing the testing options at the start of the program. Helps guide the user through testing.
	 */
	public void displayOne(){//Displays start screen
		LCD.clear();
		LCD.drawString("Up button: Move arm up", 0, 0);
		LCD.drawString("Down button: Move arm down", 0, 1);
		LCD.drawString("Left button: Dance", 1, 2);
		LCD.drawString("Right button: Take a walk", 0, 3);
		LCD.drawString("Escape button: Exit arm test", 0, 4);
	}
	
	/**
	 * Second display setup. Is currently unused.
	 */
	public void displayTwo(){ //Displays information
		//TODO include another display that could be useful
		LCD.clear();
		LCD.drawString("Not what I wanted", 0, 0);
		
	}
	
	
}