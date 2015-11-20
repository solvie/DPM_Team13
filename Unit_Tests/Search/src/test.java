import lejos.utility.Delay;


public class test {
	private Odometer odo;
	private ObjectDetector detector;
	private Navigation navigate;
	
	public test( Odometer odo,ObjectDetector detector){
		this.odo=odo;
		this.detector=detector;
		this.navigate=new Navigation(odo);
	} 
	
	public void testing(){
		navigate.travelTo(0, 30);
		navigate.travelTo(30, 30);
		navigate.travelTo(30, 0);
		navigate.travelTo(0, 0);
//		while(odo.getY()>-90){
//			navigate.setSpeeds(-120, -120);
//			System.out.println(detector.getrealdis());
//			Delay.msDelay(25);
//		}
//		navigate.setSpeeds(0, 0);
		
	}
	
}
