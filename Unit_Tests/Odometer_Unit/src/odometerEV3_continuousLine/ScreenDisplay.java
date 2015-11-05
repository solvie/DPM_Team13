package odometerEV3_continuousLine;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.lcd.TextLCD;
import lejos.utility.Timer;
import lejos.utility.TimerListener;

public class ScreenDisplay implements TimerListener {
	
	public static final int LCD_REFRESH = 100;
	private Odometer odo;
	private Timer screenTimer;
	private TextLCD textScreen = LocalEV3.get().getTextLCD();
	
	//array used to display data
	private double[] pos;
	private double[] addInfo;
	private String[] nameInfo;
	private int numInfos = 10;
	
	public ScreenDisplay(Odometer odoO) {
		this.odo = odoO;
		this.screenTimer = new Timer(LCD_REFRESH, this);
		
		//initialize the array for x, y and theta (angle)
		pos = new double[3];
		
		//initialize the arrays for any other info
		
		addInfo = new double[numInfos];
		nameInfo = new String [numInfos];
		
		//start the timer
		screenTimer.start();
	}
	
	public void addInfo(double info, String description){
		for(int i = 3; i<numInfos; i++){
			if(nameInfo[i] == null){
				nameInfo[i] = description;
				addInfo[i] = info;
				break;
			}
			else if(nameInfo[i] == description){
				addInfo[i] = info;
				break;
			}
		}
	}
	
	
	public void timedOut(){
		odo.getPosition(addInfo);
		LCD.clear();
		nameInfo[0] = "X: ";
		nameInfo[1] = "Y: ";
		nameInfo[2] = "Ang: ";
		nameInfo[3] = "V*10:";
		addInfo[3] = LocalEV3.get().getPower().getVoltage()*10;
		
		for(int i = 0; i<numInfos; i++){
			if(nameInfo[i]!=null){
				LCD.drawString(nameInfo[i], 4, i);
				LCD.drawInt((int) addInfo[i], 9, i);
			}
		}
		
		
	}

}
