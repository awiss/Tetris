import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;



import javax.swing.BorderFactory;
import javax.swing.JComponent;

import javax.swing.Timer;


public class TetrisField extends JComponent {
	final int COURTWIDTH  = 300;
	final int TOTALWIDTH = 700;
	final int TOTALHEIGHT = 600;
	final int COURTHEIGHT = 600;
	
	private TetrisArea t;
	private Timer timer;
	private int highscore;
	BufferedReader r;
	BufferedWriter w;
	private boolean instructions;
	Instructions i;
	private int interval = 10;
	private int counter;
	private int lockcounter;
	
	
	/* This is the higher level frame for the game,
	 * It initializes tetris area and handles events, such as keyboard presses
	 * and the timer. It also keeps track of the highscore.
	 */
	public TetrisField() {
	lockcounter = 0;
	i = new Instructions();
	instructions=true;  
	setBorder(BorderFactory.createLineBorder(Color.BLACK));
	setFocusable(true);
	try {
		FileReader fr = new FileReader("highscore.txt");
		r = new BufferedReader(fr);
		highscore = getNextInt(r);
		
	} catch (FileNotFoundException e1) {
		highscore = 0;
		
	}
	

	timer = new Timer(interval, new ActionListener() {
		public void actionPerformed(ActionEvent e) { tick(); }});
	timer.start();
	counter = 0;
	t = new TetrisArea (COURTWIDTH/10,highscore,this);
	addKeyListener(new KeyAdapter() {
		public void keyPressed(KeyEvent e) {
			if(!t.isLost())
			{
			if (e.getKeyCode() == KeyEvent.VK_LEFT)
				t.moveBlocks(2);
			else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
				t.moveBlocks(3);
			else if (e.getKeyCode() == KeyEvent.VK_DOWN)
				{t.moveBlocks(1);
				counter = 0;}		
			else if (e.getKeyCode() == KeyEvent.VK_UP)
				t.turn();
			else if (e.getKeyCode() == KeyEvent.VK_SPACE)
				t.slam();
			else if (e.getKeyCode() == KeyEvent.VK_SHIFT)
				t.saveShape();
			else if (e.getKeyCode() == KeyEvent.VK_ENTER)
				{
				if (instructions)
				{
				instructions = false;
				reset();
				}
				}
			else if (e.getKeyCode() == KeyEvent.VK_ALT)
				t.turnOther();
			
			}}});
	}
	// The reset method, makes an new tetrisarea and starts it with a new shape
	public void reset(){
		if (!instructions){
		timer.start();
		t = new TetrisArea (COURTWIDTH/10,highscore,this);
		t.newShape(null);
		}
		requestFocusInWindow();
				
	}
	// when there is a loss, all it does it stop the timer (so it doesn't keep going while you wait)
	
	public void handleLoss(){	
	timer.stop();
	}
	// This method reads in the highscore when giver its reader
	private int getNextInt(BufferedReader r) {
	   int x = 0;
	      try {
	       x = Integer.parseInt(r.readLine());
	       
	      } catch (Exception ex) {}
		return x;
		}
	        // Was not a number. Ignore and prompt again.
	/* Tick happens every ten milliseconds. I use this to calculate the correct amount
	 * of ticks for each step at each level, and assign it as such. It also updates the highscore
	 * in this function so it happens dynamically.      
	 */
	public void tick() {
		if (lockcounter == 60){
			t.lock();
		}
		if(t.onGround()) lockcounter++;
		else lockcounter = 0;
		
		
		int counterTime = 82;
		if (t.getLevel() == 1) counterTime = 82;
		else if(t.getLevel()==2) counterTime = 75;
		else if(t.getLevel()==3) counterTime = 68;
		else if(t.getLevel()==4) counterTime = 62;
		else if(t.getLevel()==5) counterTime = 55;
		else if(t.getLevel()==6) counterTime = 47;
		else if(t.getLevel()==7) counterTime = 37;
		else if(t.getLevel()==8) counterTime = 28;
		else if(t.getLevel()==9) counterTime = 18;
		else if(t.getLevel()==10) counterTime = 17;
		else if(t.getLevel()==11) counterTime = 15;
		else if(t.getLevel()==12) counterTime = 13;
		else if(t.getLevel()==13) counterTime = 12;
		else if(t.getLevel()==14) counterTime = 10;
		else if(t.getLevel()==15) counterTime = 10;
		else if(t.getLevel()==16) counterTime = 8;
		else if(t.getLevel()==17) counterTime = 8;
		else if(t.getLevel()==18) counterTime = 7;
		else if(t.getLevel()==19) counterTime = 7;
		else if(t.getLevel()==20) counterTime = 5;
		
		
		
		counter++;
		if (counter % counterTime == 0){
			t.step();
		}
		if (t.getScore() > highscore) highscore = t.getScore();
		 try {
			FileWriter fw = new FileWriter("highscore.txt");
			w = new BufferedWriter(fw);
			w.write(""+highscore);
			w.close();
		} catch (IOException e) {e.printStackTrace();} 
		repaint();
		if(t.isLost()) handleLoss();
		
	}
	// These two are just used in tetris area to reset the timer for locking a piece on the ground
	public void zeroLock(){
		lockcounter = 0;
	}
	public void Lock(){
		lockcounter=60;
	}
	/* This is the top level painting code, which handles the instructions
	 * the tetris area itself, and the highscore.
	 */
	   @Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g); // Paint background, border
			if (instructions){
				i.draw(g);
			}
			else{
			t.draw(g);
			g.setColor(Color.white);
			Font f2 = new Font("OtherFont",4,20);
			g.setFont(f2);
			g.drawString(""+highscore,525,550);
			}
		}

	   @Override
		public Dimension getPreferredSize() {
			return new Dimension(TOTALWIDTH, TOTALHEIGHT);
	   }
	   public void zeroCounter(){
		   counter = 0;
	   }

}
