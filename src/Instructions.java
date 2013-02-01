import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

// Paints the instruction screen
public class Instructions {
	public Instructions(){}
	public void draw(Graphics g){
		g.setColor(Color.black);
		g.fillRect(0, 0, 700, 600);
		g.setColor(Color.white);
		Font f1 = new Font("instructions",4,68);
		g.setFont(f1);
		g.drawString("Welcome to Tetris", 50, 100);
		Font f2 = new Font("",4,50);
		g.setFont(f2);
		g.drawString("How To Play:", 185, 220);
		Font f3 = new Font("",4,30);
		g.setFont(f3);
		g.drawString("Left Arrow: Move Piece Left",125,260);
		g.drawString("Right Arrow: Move Piece Right",125,300);
		g.drawString("Up Arrow: Rotate Piece",125,340);
		g.drawString("Down Arrow: Soft Drop",125,380);
		g.drawString("Spacebar: Hard Drop", 125, 420);
		g.drawString("Shift: Save/Swap", 125,460);
		g.drawString("Press Enter To Play", 125, 580);
	
	}
}
