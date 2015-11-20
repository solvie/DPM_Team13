import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;
import lejos.utility.Timer;
import lejos.utility.TimerListener;

public class LCDInfo implements TimerListener{
	public static final int LCD_REFRESH = 100;
	private Odometer odo;
	private ObjectDetector detector;
	private Timer lcdTimer;
	private TextLCD LCD = LocalEV3.get().getTextLCD();;
	
	// arrays for displaying data
	private double [] pos;
	
	public LCDInfo(Odometer odo ,ObjectDetector detector) {
		this.odo = odo;
		this.detector=detector;
		this.lcdTimer = new Timer(LCD_REFRESH, this);
		
		// initialise the arrays for displaying data
		pos = new double [3];
		
		// start the timer
		lcdTimer.start();
	}
	
	public void timedOut() { 
		odo.getPosition(pos);
		LCD.clear();
		LCD.drawString("X: ", 0, 0);
		LCD.drawString("Y: ", 0, 1);
		LCD.drawString("H: ", 0, 2);
		LCD.drawInt(((int)(pos[0] * 10))/10, 3, 0);
		LCD.drawInt(((int)(pos[1] * 10))/10, 3, 1);
		LCD.drawInt((int)pos[2], 3, 2);
		LCD.drawString("D: "+detector.getdistance(), 0, 3);
		LCD.drawString("R: "+detector.getcolor1()[0], 0, 4);
		LCD.drawString("G: "+detector.getcolor1()[1], 0, 5);
		LCD.drawString("B: "+detector.getcolor1()[2], 0, 6);
	}
}
