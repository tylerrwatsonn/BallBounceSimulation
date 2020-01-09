import java.awt.Color;

import acm.graphics.*;

public class gBall extends Thread {

		//initialize all necessary fields
		double Xi; //initial x pos of center of ball in simulation units
		double Yi;//initial y pos of center of ball in meters
		double bSize; //radius of ball in sim units
		double bLoss; //energy loss coeff
		Color bColor; //color of ball
		double bVel; //x-velocity of ball in m/s
		public GOval myBall; //object
		boolean isFinished;
		
		//Constructor method
		public gBall(double Xi, double Yi, double bSize, Color bColor, double bLoss, double bVel) {
			//associate each field to the object
			this.Xi = Xi;
			this.Yi = Yi;
			this.bSize = bSize;
			this.bColor = bColor;
			this.bLoss = bLoss;
			this.bVel = bVel;
			this.isFinished = false;
			
			myBall = new GOval(Xi - bSize/2, 600 - Yi - bSize/2, bSize, bSize); //initialize acm graphics representation of ball
			myBall.setFilled(true);
			myBall.setColor(bColor);
		}
		
		public void moveTo(double x, double y) {
			myBall.setLocation(x,y);
		}
		
		public void run() { //executed as a thread
			final double G = 9.8; //m/s^2
			final double TIME_OUT = 50; //seconds
			final double INTERVAL_TIME = .1; //seconds
			double T = 0; //initialize total time
			double time = 0; //initialize time
			double xPos = Xi - bSize; // x-position variable
			
			double el = 1 - bLoss; //initialize energy loss variable
			double initialUpPosition = 0; //initialize variable meters
			
			double h = Yi/10 - bSize/20; 	//initialize h of myBall in meters
			double h0 = Yi/10 - bSize/20;
			boolean directionUp = false;
			double vt = Math.sqrt(2*G*(h0));  //compute terminal velocity
			
			while (vt > 0.1) { //The ball has essentially stopped bouncing when vt < 0.1
				
				xPos = bVel*T + Xi - bSize; //Update x pos based on constant x-velocity bVel
				
				if (!directionUp) { //myBall moving downwards
					h = (h0 - 0.5*G*Math.pow(time, 2)); //update height
					if (h >= 0) { //set new myBall location and make sure myBall does not display below the line
						myBall.setLocation(xPos, 600 - h*10 - bSize);
					} 
					else {
						myBall.setLocation(xPos, 600 - bSize);
					}
					if (h <= 0) { //myBall hits ground
						 h = 0.0;
						 h0 = (double) h;
						 initialUpPosition = h; //change initial position going up
						 directionUp = true;
						 time = 0;
						 vt = vt*Math.sqrt(el);
					 } 
				}
				else {
					if (h >= 0) { //set new myBall location and make sure myBall does not display below the line
						myBall.setLocation(xPos, 600 - h*10 - bSize);
					} 
					else {
						myBall.setLocation(xPos, 600 - bSize);
					}
					h = initialUpPosition + vt*time - 0.5*G*Math.pow(time, 2); //set up new height
					if (h > h0) { //sets new h0
						h0 = h ;
					}
					else { //changes to myBall going downwards again
						directionUp = false;
						time = 0; //reset time
					}		
				}
				//System.out.println("Time: " + T + "X: " + xPos/10 + "Y: " + ((h > 0) ? h: 0.0)); //prints total time as well as position of the bottom and leftmost point of the myBall
				time += INTERVAL_TIME; //every iteration add to time by 0.1 s
				T += INTERVAL_TIME; //every iteration add to total time T by 0.1s
				
				
				
				try {
					// pause for 50 milliseconds
					Thread.sleep(50);
					} catch (InterruptedException e) {
					e.printStackTrace(); }
			}
		
			//when while loop is finished, this balls simulation is finished
			this.isFinished = true;
		}
		
}
		
