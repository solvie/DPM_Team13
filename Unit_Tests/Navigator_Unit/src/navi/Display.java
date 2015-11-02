package navi;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;
import lejos.utility.Timer;
import lejos.utility.TimerListener;

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
		if (pt ==0){
			displayOne();
		}
		else{
			if (pt==1)
				displayTwo();
		}
	}
	
	public void setPart(int pt){
		this.pt = pt;
	}
	
	public void displayOne(){//Displays start screen
		LCD.clear();
		LCD.drawString("Press Up", 0, 0);
	}
	public void displayTwo(){ //Displays information
		//TODO
		
	}
	
	
}
