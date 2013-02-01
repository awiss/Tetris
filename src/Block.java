import java.awt.Color;
import java.awt.Graphics;

/* One tetris block, which the tetris area is a 2D array of
 * a block knows its color, x and y location, wheter it is a preview, whether it
 * is locks, and whether it has been moved.
 */
public class Block {
	private int x;
	private int y;
	private Color color;
	private boolean isPreview;
	private boolean locked;
	private boolean moved;
	public Block (Color c){
		color = c;
		moved = false;
		locked = false;
		isPreview = false;
		x = 0;
		y = 0;
	
	}
	public void changeMoved(boolean b) {moved = b;}
	public void changeLocked(boolean b) {locked = b;}
	public boolean wasMoved(){return moved;}
	public boolean isLocked(){
		return locked;
	}
	public Color getColor(){
		return color;
	}
	public void setX(int x){
		this.x = x;
	}
	public void setY(int y){
		this.y = y;
	}
	public int X(){
		return x;
	}
	public int Y(){
		return y;
	}
	public void togglePreview(){
		isPreview= !isPreview;
	}
	public boolean isPreview(){
		return isPreview;
	}

}
