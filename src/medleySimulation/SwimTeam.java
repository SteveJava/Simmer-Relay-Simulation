//M. M. Kuttel 2024 mkuttel@gmail.com
//Class to represent a swim team - which has four swimmers
package medleySimulation;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

import medleySimulation.Swimmer.SwimStroke;

public class SwimTeam extends Thread {
	
	private CountDownLatch[] latches;
	private static CyclicBarrier barrierStart;

	public static StadiumGrid stadium; //shared 
	private Swimmer [] swimmers;
	private int teamNo; //team number 

	public int getID() {
		return teamNo;
	}

	public static final int sizeOfTeam=4;
	
	SwimTeam( int ID, FinishCounter finish,PeopleLocation [] locArr, CyclicBarrier barrier) {
		this.teamNo=ID;
		barrierStart=barrier;

		latches = new CountDownLatch[sizeOfTeam];
		for(int i = 0; i < sizeOfTeam; i++){
			latches[i]= new CountDownLatch(1);
		}

		swimmers= new Swimmer[sizeOfTeam];
	    SwimStroke[] strokes = SwimStroke.values();  // Get all enum constants
		stadium.returnStartingBlock(ID);

		for(int i=teamNo*sizeOfTeam,s=0;i<((teamNo+1)*sizeOfTeam); i++,s++) { //initialise swimmers in team
			locArr[i]= new PeopleLocation(i,strokes[s].getColour());
	      	int speed=(int)(Math.random() * (3)+30); //range of speeds 

			if(strokes[s].getOrder() == 1) {
				swimmers[s] = new Swimmer(i,this, locArr[i],finish,speed,strokes[s], null, latches[s], barrierStart); //hardcoded speed for now
			} else {
				swimmers[s] = new Swimmer(i,this,locArr[i],finish,speed,strokes[s], latches[s-1], latches[s], barrierStart); //hardcoded speed for now
			}
		}
	}
	
	
	public void run() {
		try {	
			for(int s=0;s<sizeOfTeam; s++) { //start swimmer threads
				swimmers[s].start();
				Thread.sleep(3000);
			}
			
			for(int s=0;s<sizeOfTeam; s++) swimmers[s].join();			//don't really need to do this;
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
	

