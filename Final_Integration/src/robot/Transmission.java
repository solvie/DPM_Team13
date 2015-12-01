/**
* @author Sean Lawlor
* @date November 3, 2011
* @class ECSE 211 - Design Principle and Methods
* 
* Modified by F.P. Ferrie
* February 28, 2014
* Changed parameters for W2014 competition
* 
* Modified by Francois OD
* November 11, 2015
* Changed parameters for F2015 competition
*/
package robot;

/*
 * Skeleton class to hold datatypes needed for final project
 * 
 * Simply all public variables so can be accessed with 
 * Transmission t = new Transmission();
 * int d1 = t.d1;
 * 
 * and so on...
 * 
 */

public class Transmission {
	
	public StartCorner startingCorner;
	public int homeZoneBL_X;
	public int homeZoneBL_Y;
	public int homeZoneTR_X;
	public int homeZoneTR_Y;
	public int opponentHomeZoneBL_X;
	public int opponentHomeZoneBL_Y;
	public int opponentHomeZoneTR_X;
	public int opponentHomeZoneTR_Y;
	public int dropZone_X;
	public int dropZone_Y;
	public int flagType;
	public int opponentFlagType;
	
	//not taking in account the changes in corner
	public void coordinatesTransfo(int gridSize){
		gridSize-=2;
		switch(startingCorner.getId()){
		case 1:
			break;
		case 2:
			int tempHZBLX = this.homeZoneBL_X;
			this.homeZoneBL_X = this.homeZoneBL_Y;
			this.homeZoneBL_Y = gridSize-tempHZBLX;
			int tempHZTRX = this.homeZoneTR_X;
			this.homeZoneTR_X = this.homeZoneTR_Y;
			this.homeZoneTR_Y = gridSize-tempHZTRX;
			int tempOHZBLX = this.opponentHomeZoneBL_X;
			this.opponentHomeZoneBL_X = this.opponentHomeZoneBL_Y;
			this.opponentHomeZoneBL_Y =gridSize-tempOHZBLX;
			int tempOHZTRX = this.opponentHomeZoneTR_X;
			this.opponentHomeZoneTR_X = this.opponentHomeZoneTR_Y;
			this.opponentHomeZoneTR_Y = gridSize-tempOHZTRX;
			int tempDZX = this.dropZone_X;
			this.dropZone_X = this.dropZone_Y;
			this.dropZone_Y = gridSize-tempDZX-1;
			//inversing corners for areas
			int temp = homeZoneBL_Y;
			this.homeZoneBL_Y = homeZoneTR_Y;
			this.homeZoneTR_Y = temp;
			int tempO = this.opponentHomeZoneBL_Y;
			this.opponentHomeZoneBL_Y = opponentHomeZoneTR_Y;
			this.opponentHomeZoneTR_Y = tempO;
			break;
		case 3:
			this.homeZoneBL_X = gridSize-this.homeZoneBL_X;
			this.homeZoneBL_Y = gridSize-this.homeZoneBL_Y;
			this.homeZoneTR_X = gridSize-this.homeZoneTR_X;
			this.homeZoneTR_Y = gridSize-this.homeZoneTR_Y;
			this.opponentHomeZoneBL_X = gridSize-this.opponentHomeZoneBL_X;
			this.opponentHomeZoneBL_Y =gridSize-this.opponentHomeZoneBL_Y;
			this.opponentHomeZoneTR_X = gridSize-this.opponentHomeZoneTR_X;
			this.opponentHomeZoneTR_Y = gridSize-this.opponentHomeZoneTR_Y;
			this.dropZone_X = gridSize-this.dropZone_X-1;
			this.dropZone_Y = gridSize-this.dropZone_Y-1;
			//no inversion needed
			//exchanging BL and TR
			int tempExch1_3 = this.homeZoneBL_X;
			this.homeZoneBL_X = homeZoneTR_X;
			this.homeZoneTR_X = tempExch1_3;
			int tempExch2_3 = this.homeZoneBL_Y;
			this.homeZoneBL_Y = homeZoneTR_Y;
			this.homeZoneTR_Y = tempExch2_3;
			int tempExch3_3 = this.opponentHomeZoneBL_X;
			this.opponentHomeZoneBL_X = opponentHomeZoneTR_X;
			this.opponentHomeZoneTR_X = tempExch3_3;
			int tempExch4_3 = this.opponentHomeZoneBL_Y;
			this.opponentHomeZoneBL_Y = opponentHomeZoneTR_Y;
			this.opponentHomeZoneTR_Y = tempExch4_3;
			break;
		case 4:
			int tempHZBLX4 = this.homeZoneBL_X;
			this.homeZoneBL_X = gridSize-this.homeZoneBL_Y;
			this.homeZoneBL_Y = tempHZBLX4;
			int tempHZTRX4 = this.homeZoneTR_X;
			this.homeZoneTR_X = gridSize-this.homeZoneTR_Y;
			this.homeZoneTR_Y = tempHZTRX4;
			int tempOHZBLX4 = this.opponentHomeZoneBL_X;
			this.opponentHomeZoneBL_X = gridSize-this.opponentHomeZoneBL_Y;
			this.opponentHomeZoneBL_Y = tempOHZBLX4;
			int tempOHZTRX4 = this.opponentHomeZoneTR_X;
			this.opponentHomeZoneTR_X = gridSize-this.opponentHomeZoneTR_Y;
			this.opponentHomeZoneTR_Y = tempOHZTRX4;
			int tempDZX4 = this.dropZone_X;
			this.dropZone_X = gridSize-this.dropZone_Y-1;
			this.dropZone_Y = tempDZX4;
			//inversing corners for areas
			int temp4 = homeZoneBL_Y;
			this.homeZoneBL_Y = homeZoneTR_Y;
			this.homeZoneTR_Y = temp4;
			int tempO4 = this.opponentHomeZoneBL_Y;
			this.opponentHomeZoneBL_Y = opponentHomeZoneTR_Y;
			this.opponentHomeZoneTR_Y = tempO4;
			//exchanging BL and TR
			int tempExch1_4 = this.homeZoneBL_X;
			this.homeZoneBL_X = homeZoneTR_X;
			this.homeZoneTR_X = tempExch1_4;
			int tempExch2_4 = this.homeZoneBL_Y;
			this.homeZoneBL_Y = homeZoneTR_Y;
			this.homeZoneTR_Y = tempExch2_4;
			int tempExch3_4 = this.opponentHomeZoneBL_X;
			this.opponentHomeZoneBL_X = opponentHomeZoneTR_X;
			this.opponentHomeZoneTR_X = tempExch3_4;
			int tempExch4_4 = this.opponentHomeZoneBL_Y;
			this.opponentHomeZoneBL_Y = opponentHomeZoneTR_Y;
			this.opponentHomeZoneTR_Y = tempExch4_4;
			break;
		}
	}
	
	public StartCorner getStartingCorner() {
		return this.startingCorner;
	}
	public int getHomeZoneBL_X() {
		return this.homeZoneBL_X;
	}
	public int getHomeZoneBL_Y() {
		return this.homeZoneBL_Y;
	}
	public int getHomeZoneTR_X() {
		return this.homeZoneTR_X;
	}
	public int getHomeZoneTR_Y() {
		return this.homeZoneTR_Y;
	}
	public int getOpponentHomeZoneBL_X() {
		return this.opponentHomeZoneBL_X;
	}
	public int getOpponentHomeZoneBL_Y() {
		return this.opponentHomeZoneBL_Y;
	}
	public int getOpponentHomeZoneTR_X() {
		return this.opponentHomeZoneTR_X;
	}
	public int getOpponentHomeZoneTR_Y() {
		return this.opponentHomeZoneTR_Y;
	}
	public int getDropZone_X() {
		return this.dropZone_X;
	}
	public int getDropZone_Y() {
		return this.dropZone_Y;
	}
	public int getFlagType() {
		return this.flagType;
	}
	public int getOpponentFlagType() {
		return this.opponentFlagType;
	}
	
}