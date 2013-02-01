import java.awt.Color;

/* A shape is an array of blocks. Each block knows its x and y locations, and 
 * the color is set according to the ShapeType. A shape also has a specific block
 * which is designated as the axis of rotation. This is used in turning a shape.
 * It also can copy itself (for makepreview)
 */
public class Shape{
	private boolean locked;
	private Block[] blocks;
	private int axis;
	Color c;
	private ShapeType s;
	public Shape(Color c,ShapeType s){
		locked = false;
		this.s = s;
		this.c = c;
		blocks = new Block[4];
		for (int i = 0; i<4; i++){
			blocks[i] = new Block(c);
		
		if (s.equals(ShapeType.l)||s.equals(ShapeType.t)
		||s.equals(ShapeType.zag)||s.equals(ShapeType.backzag)||s.equals(ShapeType.line)) axis = 2;
		else if(s.equals(ShapeType.backl)) axis = 1;	
		
		}
	}
	public Block get(int x){
		return blocks[x];
	}
	public ShapeType type(){
		return s;
	}
	public void changeLocked(boolean c){
		locked = c;
		for (Block b: blocks){
			b.changeLocked(c);
		}
	}
	public boolean isLocked(){
		return locked;
	}
	public void incX(int x){
		for (int i = 0; i<4; i++){
			blocks[i].setX(blocks[i].X() + x);
		}
	}
	public void incY(int y){
		for (int i = 0; i<4; i++){
			blocks[i].setY(blocks[i].Y() + y);
		}
	}
	public boolean contains(Block b){
		boolean x = false;
		for (Block bl : blocks) x = x || b==bl;
		return x;
	}
	public int axis(){
		return axis;
	}
	public Shape makePreview(){
		Shape prev = new Shape(c,s);
		for (int i=0; i<4; i++){
			prev.get(i).setX(blocks[i].X());
			prev.get(i).setY(blocks[i].Y());
			prev.get(i).togglePreview();
		}
		return prev;
		
	}
	public void setBlock(int i,Block b){
		blocks[i] = b;
	}
	
}