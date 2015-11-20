import java.util.ArrayList;
import lejos.hardware.Sound;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.robotics.geometry.Point2D;
import lejos.utility.Delay;


public class Search {
	private static final int limit=32;
	private static final int speed=150;
	private static final double error=1.0;
	private EV3LargeRegulatedMotor sensorMotor;
	private Flagcapturer arm;
	private Odometer odo;
	private Navigation navigate;
	private ObjectDetector detector;
//	private ArrayList<Point2D> data=new ArrayList<Point2D>();
//	private ArrayList<Point2D> point=new ArrayList<Point2D>();
	
	public Search(Odometer odo,ObjectDetector detector,Flagcapturer arm,EV3LargeRegulatedMotor sensorMotor){
		this.odo=odo;
		this.navigate=new Navigation(odo);
		this.detector=detector;
		this.arm=arm;
		this.sensorMotor=sensorMotor;
		sensorMotor.setAcceleration(500);
	}
	
	public void searching(Point2D point1,Point2D point2,Point2D point3,int num){
		odo.setPosition(new double [] {0, 90, 90}, new boolean [] {true,true,true});
		boolean left=point1.getX()>odo.getX();
		int deg=left ? 96 : -96;
		int degrotate=deg>0 ? 90 : -90;
		navigate.turnTo(90,true);
		sensorMotor.rotate(-deg);
		scan(point1,point3,-deg,-degrotate);

		sensorMotor.rotate(deg);
		navigate.travelTo(point2.getX()+15,odo.getY());
		navigate.travelTo(point2.getX()+15,point2.getY()-15);
		sensorMotor.rotate(deg);
		scan(point1,point3,deg,degrotate);
		
		/*
		int last=0;
		for(int i=0;i<data.size()-1;i++){
			if(data.get(i+1).getY()-data.get(i).getY()>1){
				point.add(data.get((last+i)/2));
				last=i+1;}
		}
		
		for(int i=point.size()-1;i>=0;i--){
			navigate.travelTo(point.get(i).getX(),point.get(i).getY());
			if(detector.getrealdis()<limit){
				sensorMotor.rotate(deg);
				navigate.turnTo(0,true);
				found=check(point.get(i));
			}
			if(found){
				navigate.travelTo(point1.getX()-15,point2.getY());
				// Pathfinder.findPathTo();putdown();Pathfinder.findPathTo();
			}
			sensorMotor.rotate(-deg);
		}
		navigate.travelTo(point1.getX()-15,point2.getY()-15);
		*/
		
	}
	
	public void scan(Point2D point1,Point2D point3,int deg,int degrotate){
		boolean found=false;
		while(odo.getY()>point1.getY()-15){
			navigate.setSpeeds(-speed,-speed);
			Delay.msDelay(25);
			if(detector.getrealdis()<limit){
				navigate.travelForwards(2);
				navigate.turnTo(odo.getAng()+degrotate,true);
				sensorMotor.rotate(-deg);
				found=this.check(odo.getX(),odo.getY());
				if(found){
					double x=odo.getX();
					double y=odo.getY();
					navigate.travelTo(odo.getX(),point1.getY()-15);
					// Pathfinder.findPathTo(point3.getX(),point3.getY());putdown();Pathfinder.findPathTo(x,y);
					navigate.travelTo(x,y);
				}
				sensorMotor.rotate(deg);
			}
		}
		navigate.setSpeeds(0, 0);
	}
	
	public boolean check(double x,double y){
		boolean found=false;
		boolean checked=false;
		while(detector.getrealdis()>4 && detector.getdeltarealdis()<10){
			navigate.setSpeeds(speed,speed);Delay.msDelay(25);}
		navigate.setSpeeds(0,0);
		if(detector.getrealdis()<=4){
			checked=true;
			if(!blockdetect(detector.getcolor1()[0],detector.getcolor1()[1],detector.getcolor1()[2])){
				Sound.beep();grab();found=true;}
		}
		double dis=Math.sqrt(Math.pow(odo.getX()-x,2)+Math.pow(odo.getY()-y,2));
		navigate.travelBackwards(dis);
		navigate.turnTo(90,true);
		if(!checked)
			navigate.travelBackwards(5);
		else
			navigate.travelBackwards(14);
		return found;
	}
	
	public boolean blockdetect(int r,int g,int b){
		boolean lightblue=false;
		if(g>b && g>r){
			lightblue=true;
			if(r<10 && g<10 && b<10)
				lightblue=false;
			if(r-b>10)
				lightblue=false;
		}
		return lightblue;
	}
	
	public void grab(){
		navigate.travelBackwards(10);
		sensorMotor.rotate(-96);
		arm.down();
		navigate.travelForwards(12);
		arm.up();
		sensorMotor.rotate(96);
	}
	
	public void putdown(){
		sensorMotor.rotate(-96);
		arm.down();
		navigate.travelBackwards(10);
		arm.up();
		sensorMotor.rotate(96);
	}
}
