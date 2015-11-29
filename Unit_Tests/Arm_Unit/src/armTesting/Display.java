package armTesting;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;
import lejos.utility.Timer;
import lejos.utility.TimerListener;

public class Display implements TimerListener {

	public static final int LCD_REFRESH = 2000;
	private Timer lcdTimer;
	private TextLCD LCD = LocalEV3.get().getTextLCD();
	private int statusNum;
	
	public Display() {
		statusNum = 0;
		this.lcdTimer = new Timer(LCD_REFRESH, this);
		lcdTimer.start();
	}

	public void timedOut() {
		if (statusNum == 0){
			displayOne();
		}
		else{
			if (statusNum == 1)
				displayTwo();
		}
	}
	
	public void setStatusNum(int statusNum){
		this.statusNum = statusNum;
	}
	
	public void displayOne(){//Displays start screen
		LCD.clear();
		LCD.drawString("Up button: Move arm up", 0, 0);
		LCD.drawString("Down button: Move arm down", 0, 1);
		LCD.drawString("Left button: Dance", 1, 2);
		LCD.drawString("Right button: Take a walk", 0, 3);
		LCD.drawString("Escape button: Exit arm test", 0, 4);
	}
	public void displayTwo(){ //Displays information
		//TODO include another display that could be useful
		LCD.clear();
		LCD.drawString("Not what I wanted", 0, 0);
		
	}
	
	
}