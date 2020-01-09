import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import acm.util.RandomGenerator;
import acm.graphics.GLabel;
import acm.graphics.GLine;
import acm.graphics.GObject;
import acm.graphics.GPoint;
import acm.program.GraphicsProgram;

public class bSim extends GraphicsProgram implements ChangeListener, ActionListener {
	
	public void init() {
		addMouseListeners();
		addActionListeners();
	}
	
	
	//all necessary parameters
	private static final int WIDTH = 1200; 
	private static final int HEIGHT = 600; 
	private static final int OFFSET = 200; 
	static final int NUMBALLS = 15; // # balls to sim.
	private static final double MINSIZE = 1; // Min ball size
	private static final double MAXSIZE = 8; //Max ball size
	private static final double XMIN = 10; //Min X start loc
	private static final double XMAX = 50; //Max X start loc
	private static final double YMIN = 50; //Min Y start loc
	private static final double YMAX = 100; //Max Y start loc
	private static final double EMIN = 0.4; //Min E loss
	private static final double EMAX = 0.9; //Max E loss
	private static final double VMIN = 0.5; //Min x-velocity
	private static final double VMAX = 3.0; //Max x-velocity
	
	bTree myTree = new bTree();
	
	public static void main(String[] args) {
		new bSim().start(args);
	}
	
	void clear_balls(bTree.bNode root) { //function to clear balls off screen
		if (root.left != null) clear_balls(root.left);
		if (root != null) remove(root.iBall.myBall);
		if (root.right != null) clear_balls(root.right);
	}
	
	void stop_balls(bTree.bNode root) { //function to stop the balls currently in sim
		if (root.left != null) stop_balls(root.left);
		root.iBall.stop();
		if (root.right != null) stop_balls(root.right);
	}
	GLabel mouse_click = new GLabel("Click mouse to continue!", 200, 200);
	
	RandomGenerator rg = new RandomGenerator(); //set up randomizer
	
	
	//fields for simulation and JPanels
	sliderBox numballsSlider;
	sliderBox minSize;
	sliderBox maxSize;
	sliderBox xMin;
	sliderBox xMax;
	sliderBox yMin;
	sliderBox yMax;
	sliderBox lossMin;
	sliderBox lossMax;
	sliderBox xVelMin;
	sliderBox xVelMax;
	sliderBox b_size;
	sliderBox e_loss;
	sliderBox x_vel;
	sliderBox color;
	float f_val;
	private int PS_NumBalls;
	private Float PS_minSize;
	private Float PS_maxSize;
	private Float PS_xMax;
	private Float PS_xMin;
	private Float PS_yMin;
	private Float PS_yMax;
	private Float PS_lossMin;
	private Float PS_lossMax;
	private Float PS_xVelMin;
	private Float PS_xVelMax;
	private Float PS_b_size;
	private Float PS_e_loss;
	private Float PS_x_vel;
	private Integer PS_color_index;
	private Color PS_new_color;
	
	JComboBox<String> bSimC;
	private GPoint last;
	private GObject gobj;
	
	private boolean simRunning = true;
	
	
	public void stateChanged(ChangeEvent e) { //Function to change the state of the sliders
		JSlider source = (JSlider)e.getSource();

		if (source==numballsSlider.mySlider) {
			PS_NumBalls=numballsSlider.getISlider();
			numballsSlider.setISlider(PS_NumBalls);
		}
		else if(source == minSize.mySlider) {
			PS_minSize = minSize.getFSlider();
			minSize.setFSlider(PS_minSize);
		}
		else if(source == maxSize.mySlider) {
			PS_maxSize = maxSize.getFSlider();
			maxSize.setFSlider(PS_maxSize);
		}
		else if(source == xMin.mySlider) {
			PS_xMin = xMin.getFSlider();
			xMin.setFSlider(PS_xMin);
		}
		else if(source == xMax.mySlider) {
			PS_xMax = xMax.getFSlider();
			xMax.setFSlider(PS_xMax);
		}
		else if(source == yMin.mySlider) {
			PS_yMin = yMin.getFSlider();
			yMin.setFSlider(PS_yMin);
		}
		else if(source == yMax.mySlider) {
			PS_yMax = yMax.getFSlider();
			yMax.setFSlider(PS_yMax);
		}
		else if(source == lossMin.mySlider) {
			PS_lossMin = (float) (lossMin.getFSlider());
			lossMin.setFSlider((float) (PS_lossMin));
		}
		else if(source == lossMax.mySlider) {
			PS_lossMax = lossMax.getFSlider();
			lossMax.setFSlider((float) (PS_lossMax));
		}
		else if(source == xVelMin.mySlider) {
			PS_xVelMin = xVelMin.getFSlider();
			xVelMin.setFSlider(PS_xVelMin);
		}
		else if(source == xVelMax.mySlider) {
			PS_xVelMax = xVelMax.getFSlider();
			xVelMax.setFSlider(PS_xVelMax);
		}
		else if(source == color.mySlider) {
			PS_color_index = color.getISlider();
			PS_new_color = colors[PS_color_index];
			color.sReadout.setForeground(PS_new_color);
		}
		else if(source == b_size.mySlider) {
			PS_b_size = b_size.getFSlider();
			b_size.setFSlider(PS_b_size);
		}
		else if(source == e_loss.mySlider) {
			PS_e_loss = e_loss.getFSlider();
			e_loss.setFSlider(PS_e_loss);
		}
		else if(source == x_vel.mySlider) {
			PS_x_vel = x_vel.getFSlider();
			x_vel.setFSlider(PS_x_vel);
		}
		
	}
	//List of colors for the single ball simulation
	Color[] colors = new Color[8];
	{
	colors[0] = Color.RED;
	colors[1] = Color.BLUE;
	colors[2] = Color.CYAN;
	colors[3] = Color.GREEN;
	colors[4] = Color.YELLOW;
	colors[5] = Color.PINK;
	colors[6] = Color.MAGENTA;
	colors[7] = Color.ORANGE;
	}
	
	
	void setChoosers() { //initialize selection box for simulation
		bSimC = new JComboBox<String>();
		bSimC.addItem("bSim");
		bSimC.addItem("Run");
		bSimC.addItem("Clear");
		bSimC.addItem("Stop");
		bSimC.addItem("Quit");
		add(bSimC,NORTH);
		addJComboListeners();
	}
	
	void addJComboListeners() { //necessary function
		bSimC.addActionListener((ActionListener)this);
		
	}

	public void run() {
		
	//Set Up East Panel Sliders
		JPanel eastPanel = new JPanel();
		eastPanel.setLayout(new GridLayout(20,2));
		
		JLabel gen_sim = new JLabel("General Simulation Parameters");
		eastPanel.add(gen_sim);
		
		numballsSlider = new sliderBox("NUMBALLS", 1, 15, 25);
		eastPanel.add(numballsSlider.myPanel, "EAST");
		numballsSlider.mySlider.addChangeListener((ChangeListener) this);
		
		minSize = new sliderBox("MIN SIZE", 1.0, 3.0, 25.0);
		eastPanel.add(minSize.myPanel, "EAST");
		minSize.mySlider.addChangeListener((ChangeListener) this);
		
		maxSize = new sliderBox("MAX SIZE", 1.0, 22.0, 25.0);
		eastPanel.add(maxSize.myPanel, "EAST");
		maxSize.mySlider.addChangeListener((ChangeListener) this);
		
		xMin = new sliderBox("X MIN", 1.0, 10.0, 200.0);
		eastPanel.add(xMin.myPanel, "EAST");
		xMin.mySlider.addChangeListener((ChangeListener) this);
		
		xMax = new sliderBox("X MAX", 1.0, 50.0, 200.0);
		eastPanel.add(xMax.myPanel, "EAST");
		xMax.mySlider.addChangeListener((ChangeListener) this);
		
		yMin = new sliderBox("Y MIN", 1.0, 25.0, 100.0);
		eastPanel.add(yMin.myPanel, "EAST");
		yMin.mySlider.addChangeListener((ChangeListener) this);
		
		yMax = new sliderBox("Y MAX", 1.0, 75.0, 100.0);
		eastPanel.add(yMax.myPanel, "EAST");
		yMax.mySlider.addChangeListener((ChangeListener) this);
		
		lossMin = new sliderBox("LOSS MIN", 0.0, 0.2, 1.0);
		eastPanel.add(lossMin.myPanel, "EAST");
		lossMin.mySlider.addChangeListener((ChangeListener) this);
		
		lossMax = new sliderBox("LOSS MAX", 0.0, 0.0, 1.0);
		eastPanel.add(lossMax.myPanel, "EAST");
		lossMax.mySlider.addChangeListener((ChangeListener) this);
		
		xVelMin = new sliderBox("X VEL MIN", 0.0, 5.0, 10.0);
		eastPanel.add(xVelMin.myPanel, "EAST");
		xVelMin.mySlider.addChangeListener((ChangeListener) this);
		
		xVelMax = new sliderBox("X VEL MAX", 0.0, 5.0, 10.0);
		eastPanel.add(xVelMax.myPanel, "EAST");
		xVelMax.mySlider.addChangeListener((ChangeListener) this);
		
		//Single Ball
		JLabel single_instance = new JLabel("Single Ball Instance Parameters");
		eastPanel.add(single_instance);
		
		color = new sliderBox("Ball Color", "Color");
		eastPanel.add(color.myPanel, "EAST");
		color.mySlider.addChangeListener((ChangeListener) this);
		
		b_size = new sliderBox("Ball Size", 0.0, 4.0, 8.0);
		eastPanel.add(b_size.myPanel, "EAST");
		b_size.mySlider.addChangeListener((ChangeListener) this);
		
		e_loss = new sliderBox("E Loss", 0.0, 0.0, 1.0);
		eastPanel.add(e_loss.myPanel, "EAST");
		e_loss.mySlider.addChangeListener((ChangeListener) this);
		
		x_vel = new sliderBox("X Vel", 1.0, 4.0, 5.0);
		eastPanel.add(x_vel.myPanel, "EAST");
		x_vel.mySlider.addChangeListener((ChangeListener) this);
		
		setChoosers();
		add(eastPanel);
		
	//Initial parameters for simulation sliders
		PS_NumBalls = numballsSlider.getISlider();
		PS_minSize = minSize.getFSlider();
		PS_maxSize = maxSize.getFSlider();
		PS_xMax = xMax.getFSlider();
		PS_xMin = xMin.getFSlider();
		PS_yMin = yMin.getFSlider();
		PS_yMax = yMax.getFSlider();
		PS_lossMin = lossMin.getFSlider();
		PS_lossMax = lossMax.getFSlider();
		PS_xVelMin = xVelMin.getFSlider();
		PS_xVelMax = xVelMax.getFSlider();
		PS_new_color = colors[color.getISlider()];
		PS_b_size = b_size.getFSlider();
		PS_e_loss = e_loss.getFSlider();
		PS_x_vel = x_vel.getFSlider();
		
		this.resize(WIDTH, HEIGHT+OFFSET); //initialize window
		add(new GLine(0, HEIGHT, WIDTH, HEIGHT)); //initialize ground represented by line
		
		
	//intialize parameters
		double Xi;
		double Yi;
		double bSize;
		double bLoss;
		double bVel;
		Color bColor;
		
		
		for (int i = 0; i < NUMBALLS; i++) { //"for" loop to fill array with 100 balls
			Xi = rg.nextDouble(XMIN, XMAX);
			Yi = rg.nextDouble(YMIN, YMAX);
			bSize = rg.nextDouble(MINSIZE, MAXSIZE);
			bLoss = rg.nextDouble(EMIN, EMAX);
			bVel = rg.nextDouble(VMIN, VMAX);
			bColor = rg.nextColor();
			
			gBall iBall = new gBall(Xi*10, Yi*10, bSize*10, bColor, bLoss, bVel*10); //create object
			add(iBall.myBall);
			myTree.addNode(iBall);
			iBall.start();
		}
		while(myTree.isRunning() && simRunning);
		add(mouse_click);
		waitForClick();
		myTree.moveSort();
		mouse_click.setLabel("All Sorted!");
	}
	@Override
	public void actionPerformed(ActionEvent e) { //ActionListener to check for selection in JComboBox
		// TODO Auto-generated method stub
		JComboBox source = (JComboBox)e.getSource();
		if (source==bSimC) {
			if (bSimC.getSelectedIndex()==1) { //Handles "Run" Selection
				System.out.println("Starting simulation");
				for (int i = 0; i < PS_NumBalls; i++) { //"for" loop to fill array with 100 balls
					RandomGenerator rg = new RandomGenerator();
				//NOTE: dividing by 10 is used because of how the slider values are set up to display and handle floats
					double Xi = rg.nextDouble(PS_xMin/10, PS_xMax/10);
					double Yi = rg.nextDouble(PS_yMin/10, PS_yMax/10);
					double bSize = rg.nextDouble(PS_minSize/10, PS_maxSize/10);
					double bLoss = rg.nextDouble(PS_lossMin/10, PS_lossMax/10);
					double bVel = rg.nextDouble(PS_xVelMin/10, PS_xVelMax/10);
					Color bColor = rg.nextColor();
					
					gBall iBall = new gBall(Xi*10, Yi*10, bSize*10, bColor, bLoss, bVel*10); //create object
					add(iBall.myBall);
					myTree.addNode(iBall);
					iBall.start();
				}
					
			}
			else if (bSimC.getSelectedIndex() == 2) { //Handles "Clear" Selection 
				clear_balls(myTree.root);
				remove(mouse_click);
			}
				
			else if (bSimC.getSelectedIndex() == 3) { //Handles "Stop" Selection
				stop_balls(myTree.root);
				simRunning = false;
				
			}
			else if (bSimC.getSelectedIndex() == 4) { //Handles "Quit" Selection
				System.exit(0);
			}
		}
		}
	
	/*
	 * BONUS
	 */
	
	 public void mousePressed(MouseEvent e) { //Handles selection of gBall
		 last = new GPoint(e.getPoint());
		 gobj = getElementAt(last);
		 myTree.traverse_stop(myTree.root, gobj);
		 
		 
	 }
	 public void mouseDragged(MouseEvent e) { //Handles displacement of gBall object
		 if (gobj != null) {
			 gobj.move(e.getX() - last.getX(), e.getY() - last.getY());
			 last = new GPoint(e.getPoint());
		 }
	 }
	 public void mouseReleased(MouseEvent e) { //Handles setting up a new gBall in old spot
		 last = new GPoint(e.getPoint());
		 if (getElementAt(last) instanceof GObject) {
		 gBall single_ball = new gBall((double) last.getX() + PS_b_size, (double) HEIGHT - last.getY(), (double) PS_b_size, PS_new_color, (double) PS_e_loss/10, (double) PS_x_vel);
		 remove(gobj);
		 myTree = myTree.re_sort(gobj);
		 myTree.addNode(single_ball);
		 add(single_ball.myBall);
		 single_ball.start();
		 }		 
	 }
}

	
	
