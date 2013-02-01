import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;

/* This is where most of the logic of the game is
 * basically, a 2D array of blocks is manipulated by a bunch of different methods.
 */

public class TetrisArea {
	private Block[][] blocks;
	private boolean slammed;
	private int side;
	private Shape currentShape;
	private Shape preview;
	private Random r;
	private int score;
	private int currentLevel;
	private int clearedLines;
	private boolean lost;
	private ShapeType nextShape;
	private ShapeType savedShape;
	private boolean hasSaved;
	private TetrisField tf;
	private boolean isSlamming;
	private ArrayList<ShapeType> allshapes;
	private ArrayList<ShapeType> currentShapes;
;

	public TetrisArea(int side, int highscore, TetrisField tf){
	slammed = false;
	isSlamming = false;
		allshapes = new ArrayList<ShapeType>();
		allshapes.add(ShapeType.t);
		allshapes.add(ShapeType.l);
		allshapes.add(ShapeType.backl);
		allshapes.add(ShapeType.line);
		allshapes.add(ShapeType.square);
		allshapes.add(ShapeType.zag);
		allshapes.add(ShapeType.backzag);
		currentShapes = new ArrayList<ShapeType>();
		currentShapes.addAll(allshapes);
		blocks = new Block[10][22];
		this.side = side;
		r = new Random();
		slammed = false;
		currentShape = new Shape(Color.black, ShapeType.t);
		preview = new Shape(Color.black, ShapeType.t);
		score = 0;
		currentLevel = 1;
		clearedLines = 0;
		lost = false;
		newNext();
		savedShape = null;
		hasSaved = false;
		this.tf = tf;
	}
	/* This searches through each row of the 2D array, if the array is full, 
	 * (no null or preview blocks), then if clears it and shifts everything down.
	 * This is also where the score and the level of the game are adjusted.
	 * 
	 */
	public void clearLines(){
		int cleared = 0;
		boolean full;
		for (int i=2; i<22; i++){
			full=true;
			for (int j=0; j<10; j++){
				full = full && blocks[j][i]!=null && !blocks[j][i].isPreview();
			}
			
			if (full){
				
				for (int x=0;x<10;x++){
					for(int y=i;y>0;y--){
						blocks[x][y]=blocks[x][y-1];
					}
					blocks[x][0] = null;
					
				}
				cleared++;
				i--;
			} 
		}
		if (cleared==1) score += 40 + 40*currentLevel;
		else if (cleared ==2) score += 100 + 100*currentLevel;
		else if (cleared ==3) score += 300 + 300*currentLevel;
		else if (cleared ==4) score += 1200 + 1200*currentLevel;
		clearedLines+=cleared;
		currentLevel = clearedLines/10 + 1;
		if (currentLevel>20) currentLevel =20;
	}
	/* This the where the code for saving a shape on the left side is
	 * this update the field savedShape to be the currentShape, and calls newShape
	 * with they type of savedShape. If there is no savedShape, it just 
	 * gives you the next one.
	 */
	public void saveShape(){
		if(!hasSaved){
		ShapeType save = savedShape;
		
			for (int i=0;i<4;i++){
				blocks[currentShape.get(i).X()][currentShape.get(i).Y()]=null;
				blocks[preview.get(i).X()][preview.get(i).Y()]=null;
			    
			}
			savedShape = currentShape.type();
			newShape(save);
			updatePreview();
			if (save!=null)
			hasSaved = true;
		}
	}
	// 1 is down, 2 is left, 3 is right
	
	/* This method contains all of the code for moving left, right, and doing
	 * as soft drop. This first does not allow it to go past any of the edges (boolean c).
	 * Then, it makes sure that there are no blocks to the side of the blocks in the shape
	 * (excluding preview blocks and blocks in the shape itself) (boolean b) if both of
	 * these conditions holds, it moves the piece and updates x,y of the blocks.
	 * Move down also locks a piece if it is on the bottom of the board.
	 */
	public void moveBlocks(int x){
		if (!currentShape.isLocked() && x==1){
			boolean b = false;
			boolean c = false;
			for (int i=0; i<4; i++){
				if (currentShape.get(i).Y()==21){c=true; break;}
				
				
				b = b || ((blocks[currentShape.get(i).X()][currentShape.get(i).Y()+1] != null) 
						
				&& !currentShape.contains(blocks[currentShape.get(i).X()][currentShape.get(i).Y()+1])
				
				&& !preview.contains(blocks[currentShape.get(i).X()][currentShape.get(i).Y()+1]));
				
			}
			if(!b && !c){
				for (int i=3; i>-1; i--){
					
					blocks[currentShape.get(i).X()][currentShape.get(i).Y()] = null;
				}
				currentShape.incY(1);
				updatePreview();
				placeShape();
				
			}
			
			}
		if (!currentShape.isLocked() && x==2){
			boolean b = false;
			boolean c = false;
			for (int i=0; i<4; i++){
				if (currentShape.get(i).X()==0) {c=true;break;}
				b = b || ((blocks[currentShape.get(i).X()-1][currentShape.get(i).Y()] != null) 
						&& !currentShape.contains(blocks[currentShape.get(i).X()-1][currentShape.get(i).Y()])
						&& !preview.contains(blocks[currentShape.get(i).X()-1][currentShape.get(i).Y()]));
			}
			if(!b && !c){
				tf.zeroLock();
				for (int i=0; i<4; i++){
					blocks[currentShape.get(i).X()][currentShape.get(i).Y()] = null;
				}
				currentShape.incX(-1);
				updatePreview();
				placeShape();
				
			}
			}
	
		if (!currentShape.isLocked() && x==3){
			boolean b = false;
			boolean c = false;
			for (int i=3; i>-1; i--){
				if (currentShape.get(i).X()==9) {c=true;break;}
				b = b || ((blocks[currentShape.get(i).X()+1][currentShape.get(i).Y()] != null) 
						&& !currentShape.contains(blocks[currentShape.get(i).X()+1][currentShape.get(i).Y()])
						&& !preview.contains(blocks[currentShape.get(i).X()+1][currentShape.get(i).Y()]));
			}
			if(!b && !c){
				tf.zeroLock();
				for (int i=3; i>-1; i--){
					blocks[currentShape.get(i).X()][currentShape.get(i).Y()] = null;
				}
				currentShape.incX(1);
				updatePreview();
				placeShape();
				
			}
			}
	}
	/* This Method rotates a piece. It takes a pivot point which is designated in the shape class,
	 * then in find the differences in x and y values and uses them to find a new position.
	 * The algorithm is basically (newX = pivotX - Ydiff, newY = pivotY + Xdiff)
	 * It also tests whether these new positions would hit a wall or blocks below it,
	 * and kicks away appropriately. It also sets the x and y instance variables of the blocks
	 * to match.
	 */

	public void turn(){
		if (!currentShape.type().equals(ShapeType.square)){
			if(!currentShape.isLocked()){
				tf.zeroLock();
				int a;
				int b;
				int[] orgY = new int[]{currentShape.get(0).Y(),
						currentShape.get(1).Y(),
						currentShape.get(2).Y(),currentShape.get(3).Y()};
				int[] orgX = new int[]{currentShape.get(0).X(),
						currentShape.get(1).X(),
						currentShape.get(2).X(),currentShape.get(3).X()};
				int atest;
				int btest;
				int axisy = currentShape.get(currentShape.axis()).Y();
				int axisx = currentShape.get(currentShape.axis()).X();
				for (int i = 0; i<4;i++){
					axisy = currentShape.get(currentShape.axis()).Y();
					axisx = currentShape.get(currentShape.axis()).X();
					atest = currentShape.get(i).X()-axisx;
					btest = currentShape.get(i).Y()-axisy;
				
					
					if (axisx-btest<0) {currentShape.incX(1);i=0; continue;}
					if (axisx-btest >9) {currentShape.incX(-1);i=0; continue;}
					if (axisy+atest<0) {currentShape.incY(1);i=0; continue;}
					if (axisy+atest>21 || ((blocks[axisx-btest][axisy+atest]) != null
							&& !currentShape.contains(blocks[axisx-btest][axisy+atest])
							&& !preview.contains(blocks[axisx-btest][axisy+atest]))) {currentShape.incY(-1); i=0;}
				
				}
				axisy = currentShape.get(currentShape.axis()).Y();
				axisx = currentShape.get(currentShape.axis()).X();
				for (int i = 0; i<4;i++){
					a = currentShape.get(i).X()-axisx;
					b = currentShape.get(i).Y()-axisy;
					blocks[orgX[i]][orgY[i]] = null;
					currentShape.get(i).setX(axisx-b);
					currentShape.get(i).setY(axisy+a);
					
				}
				updatePreview();
				placeShape();
				
			}
		}
		
	}
	// This turns pieces, but the other direction.
	public void turnOther(){
		if (!currentShape.type().equals(ShapeType.square)){
			if(!currentShape.isLocked()){
				tf.zeroLock();
				int a;
				int b;
				int[] orgY = new int[]{currentShape.get(0).Y(),currentShape.get(1).Y(),currentShape.get(2).Y(),currentShape.get(3).Y()};
				int[] orgX = new int[]{currentShape.get(0).X(),currentShape.get(1).X(),currentShape.get(2).X(),currentShape.get(3).X()};
				int atest;
				int btest;
				int axisy = currentShape.get(currentShape.axis()).Y();
				int axisx = currentShape.get(currentShape.axis()).X();
				for (int i = 0; i<4;i++){
					axisy = currentShape.get(currentShape.axis()).Y();
					axisx = currentShape.get(currentShape.axis()).X();
					atest = currentShape.get(i).X()-axisx;
					btest = currentShape.get(i).Y()-axisy;
					
					
					if (axisx+btest<0) {currentShape.incX(1); i--; continue;}
					if (axisx+btest >9) {currentShape.incX(-1); i--; continue;}
					if (axisy-atest<0) {currentShape.incY(1); i--; continue;}
					if (axisy-atest>21 || ((blocks[axisx+btest][axisy-atest]) != null
							&& !currentShape.contains(blocks[axisx+btest][axisy-atest])
							&& !preview.contains(blocks[axisx+btest][axisy-atest]))) currentShape.incY(-1);
				}
				axisy = currentShape.get(currentShape.axis()).Y();
				axisx = currentShape.get(currentShape.axis()).X();
				for (int i = 0; i<4;i++){
					a = currentShape.get(i).X()-axisx;
					b = currentShape.get(i).Y()-axisy;
					blocks[orgX[i]][orgY[i]] = null;
					currentShape.get(i).setX(axisx+b);
					currentShape.get(i).setY(axisy-a);
					
				}
				updatePreview();
				placeShape();
				
			}
		}
		
	}
	/* This method finds the bottom, basically where the block would fall to
	 * and moves the preview there.
	 */
	
	public void previewStepper(){
		boolean atBottom = false;
		while(!atBottom){
		boolean b=true;
		for (int i=0; i<4; i++){
			
			if (preview.get(i).Y()==21){atBottom=true; b=false; break;} 	
			b= b && (blocks[preview.get(i).X()][preview.get(i).Y()+1]==null || currentShape.contains(blocks[preview.get(i).X()][preview.get(i).Y()+1]) 
					|| preview.contains(blocks[preview.get(i).X()][preview.get(i).Y()+1]));
				
				
			}
		if(b)preview.incY(1);
		else atBottom = true;
		}
			
		
		
		}
	//This updates the preview blocks after each move
	
	public void updatePreview(){
		
		for (int i=0; i<4; i++){
		 blocks[preview.get(i).X()][preview.get(i).Y()] = null;
		}
		preview = currentShape.makePreview();
		previewStepper();
		for(int i=0;i<4;i++){
			placeBlock(preview.get(i));
		}
		
	}
	// This creates a new preview shape
	public void newPreview(){
		preview = currentShape.makePreview();
		previewStepper();
		for(int i=0;i<4;i++){
			placeBlock(preview.get(i));
		}
	}
	// This finds the bottom and locks the current piece there, (Hard Drop)
	public void slam(){
		isSlamming = true;
		slammed = false;
		while (!slammed){
			step();
		}
		isSlamming=false;
		lock();
		
	}
	/* This is the code for the gravity of the tetris pieces. The timer 
	 * executes step on an interval determined by the level.,
	 * The method checks for blocks beneath it (excluding preview and blocks in the shape itself)
	 * and moves it (or not) accordingly.
	 */

	public void step(){
		
		if (!currentShape.isLocked()){
			boolean b = false;
			boolean c = false;
			for (int i=0; i<4; i++){
				if (currentShape.get(i).Y()==21){c=true; break;}
				b = b || ((blocks[currentShape.get(i).X()][currentShape.get(i).Y()+1] != null) 
						&& !currentShape.contains(blocks[currentShape.get(i).X()][currentShape.get(i).Y()+1]) 
						&& !preview.contains(blocks[currentShape.get(i).X()][currentShape.get(i).Y()+1]));
				
			}
			if(!b && !c){
				for (int i=3; i>-1; i--){
					blocks[currentShape.get(i).X()][currentShape.get(i).Y()] = null;
				}
				currentShape.incY(1);
				updatePreview();
				placeShape();
				tf.zeroLock();
				b = false;
				c = false;
				for (int i=0; i<4; i++){
					if (currentShape.get(i).Y()==21){c=true; break;}
					b = b || ((blocks[currentShape.get(i).X()][currentShape.get(i).Y()+1] != null) 
							&& !currentShape.contains(blocks[currentShape.get(i).X()][currentShape.get(i).Y()+1]) 
							&& !preview.contains(blocks[currentShape.get(i).X()][currentShape.get(i).Y()+1]));
					
				}
				
				
			}
			
			else{
				if (isSlamming) slammed = true;
			}
		}
		else {
			slammed = true;
			clearLines();
			newShape(null);
		}
		

	}
	// Helper method for putting a shape in the grid
	public void placeShape(){
		for(int i=0;i<4;i++){
			placeBlock(currentShape.get(i));
		}
	}
	// Yeah, you get the idea
	public void placeBlock(Block b){
		blocks[b.X()][b.Y()] = b;
	}
	// Helper for tetrisfield
	public boolean isLost(){
		return lost;
	}
	/* This updates the next block- how it works is in the same way as normal tetris
	 * It makes a list of all 7 blocks in random order, and goes through each one, 
	 * then makes another list.
	 */
	
	public void newNext(){
		if (currentShapes.size() == 0){
			currentShapes.addAll(allshapes);
			
		}
		int x = r.nextInt(currentShapes.size());
		nextShape = currentShapes.remove(x);
	}
	/* This does a lot of the busywork and resetting needed when making a new shape at the top
	 * It checks whether the player has lost, and creates a new shape either based off of an entry (
	 * this is for using Save) or null, in which case it just gets the next one.
	 */
	public void newShape(ShapeType st){
		
		tf.zeroCounter();
		boolean b=false;
		for (int i=0;i<10;i++){
			b= b || blocks[i][1]!=null;
		}
		if (b) lost = true;
		hasSaved=false;
		ShapeType thisShape;
		if (st!=null) thisShape = st;
		else{
			thisShape= nextShape;
			newNext();	
		}

		if (thisShape == ShapeType.t){
			currentShape = new Shape(Color.magenta,ShapeType.t);
			blocks[3][2] = currentShape.get(0);
		    currentShape.get(0).setX(3);
		    currentShape.get(0).setY(2);
			blocks[4][1] = currentShape.get(1);
		    currentShape.get(1).setX(4);
		    currentShape.get(1).setY(1);
		    blocks[4][2] = currentShape.get(2);
		    currentShape.get(2).setX(4);
		    currentShape.get(2).setY(2);
		    blocks[5][2] = currentShape.get(3);
		    currentShape.get(3).setX(5);
		    currentShape.get(3).setY(2);
		}
		else if (thisShape == ShapeType.line){
			currentShape = new Shape(Color.cyan,ShapeType.line);
			blocks[3][2] = currentShape.get(0);
		    currentShape.get(0).setX(3);
		    currentShape.get(0).setY(2);
			blocks[4][2] = currentShape.get(1);
		    currentShape.get(1).setX(4);
		    currentShape.get(1).setY(2);
		    blocks[5][2] = currentShape.get(2);
		    currentShape.get(2).setX(5);
		    currentShape.get(2).setY(2);
		    blocks[6][2] = currentShape.get(3);
		    currentShape.get(3).setX(6);
		    currentShape.get(3).setY(2);
		}
		else if (thisShape == ShapeType.zag){
			currentShape = new Shape(Color.red,ShapeType.zag);
			blocks[3][1] = currentShape.get(0);
		    currentShape.get(0).setX(3);
		    currentShape.get(0).setY(1);
			blocks[4][1] = currentShape.get(1);
		    currentShape.get(1).setX(4);
		    currentShape.get(1).setY(1);
		    blocks[4][2] = currentShape.get(2);
		    currentShape.get(2).setX(4);
		    currentShape.get(2).setY(2);
		    blocks[5][2] = currentShape.get(3);
		    currentShape.get(3).setX(5);
		    currentShape.get(3).setY(2);
		}
		else if (thisShape == ShapeType.backzag){
			currentShape = new Shape(Color.green,ShapeType.backzag);
			blocks[3][2] = currentShape.get(0);
		    currentShape.get(0).setX(3);
		    currentShape.get(0).setY(2);
			blocks[4][1] = currentShape.get(1);
		    currentShape.get(1).setX(4);
		    currentShape.get(1).setY(1);
		    blocks[4][2] = currentShape.get(2);
		    currentShape.get(2).setX(4);
		    currentShape.get(2).setY(2);
		    blocks[5][1] = currentShape.get(3);
		    currentShape.get(3).setX(5);
		    currentShape.get(3).setY(1);
		}
		else if (thisShape == ShapeType.l){
			currentShape = new Shape(Color.blue,ShapeType.l);
			blocks[3][1] = currentShape.get(0);
		    currentShape.get(0).setX(3);
		    currentShape.get(0).setY(1);
			blocks[3][2] = currentShape.get(1);
		    currentShape.get(1).setX(3);
		    currentShape.get(1).setY(2);
		    blocks[4][2] = currentShape.get(2);
		    currentShape.get(2).setX(4);
		    currentShape.get(2).setY(2);
		    blocks[5][2] = currentShape.get(3);
		    currentShape.get(3).setX(5);
		    currentShape.get(3).setY(2);
		}
		else if (thisShape == ShapeType.backl){
			currentShape = new Shape(Color.orange,ShapeType.backl);
			blocks[3][2] = currentShape.get(0);
		    currentShape.get(0).setX(3);
		    currentShape.get(0).setY(2);
			blocks[4][2] = currentShape.get(1);
		    currentShape.get(1).setX(4);
		    currentShape.get(1).setY(2);
		    blocks[5][1] = currentShape.get(2);
		    currentShape.get(2).setX(5);
		    currentShape.get(2).setY(1);
		    blocks[5][2] = currentShape.get(3);
		    currentShape.get(3).setX(5);
		    currentShape.get(3).setY(2);
		}
		else if (thisShape == ShapeType.square){
			currentShape = new Shape(Color.yellow,ShapeType.square);
			blocks[4][1] = currentShape.get(0);
		    currentShape.get(0).setX(4);
		    currentShape.get(0).setY(1);
			blocks[4][2] = currentShape.get(1);
		    currentShape.get(1).setX(4);
		    currentShape.get(1).setY(2);
		    blocks[5][1] = currentShape.get(2);
		    currentShape.get(2).setX(5);
		    currentShape.get(2).setY(1);
		    blocks[5][2] = currentShape.get(3);
		    currentShape.get(3).setX(5);
		    currentShape.get(3).setY(2);
		}
		newPreview();	
		}
		public int getScore(){
		return score;
	}
	public int getLevel(){
		return currentLevel;
	}
	public boolean onGround(){
		boolean b = false;
		boolean c = false;
		for (int i=0; i<4; i++){
			if (currentShape.get(i).Y()==21){c=true; break;}
			b = b || ((blocks[currentShape.get(i).X()][currentShape.get(i).Y()+1] != null) 
					&& !currentShape.contains(blocks[currentShape.get(i).X()][currentShape.get(i).Y()+1]) 
					&& !preview.contains(blocks[currentShape.get(i).X()][currentShape.get(i).Y()+1]));
			
		}
		return b || c;
	}
	public boolean isLocked(){
		return currentShape.isLocked();
	}
	public void lock(){
		clearLines();
		currentShape.changeLocked(true);
		newShape(null);
	}
	public void draw(Graphics g){
		// Draw the two sides
		g.setColor(Color.blue.darker());
		g.fillRect(500, 0, 200, 600);
		g.fillRect(0, 0, 200, 600);
		Font f = new Font("MyFont",4,28);
		Font f2 = new Font("OtherFont",4,20);
		g.setFont(f);
		g.setColor(Color.blue.darker().darker().darker());
		
		g.fillRect(512, 205, 175, 60);
		g.fillRect(512, 305, 175, 60);
		g.fillRect(512,405,175,60);
		g.fillRect(512, 505, 175, 60);
		g.setColor(Color.blue.darker());
		g.fillRect(517,210,165,50);
		g.fillRect(517,310,165,50);
		g.fillRect(517,410,165,50);
		g.fillRect(517,510,165,50);
		g.setColor(Color.white);
		g.drawString("Level: " + currentLevel,525,245);
		g.drawString("Lines: " + clearedLines,525,345);
		g.setFont(f2);
		g.drawString("Score: ",525,430);
		g.drawString(""+ score,525,450);
		g.drawString("High Score: ",525,530);
		
		g.setColor(Color.black);
		
		
		//Draw the grid (all of the null blocks)
		for (int x = 0; x<10; x++){
			for (int y = 2; y<22; y++){
				if (blocks[x][y] != null){
				
				}
				else{
					g.setColor(Color.LIGHT_GRAY);
					g.drawRect(x*side+200, (y-2)*side, side, side);
				}
				}
		}
		// Draw all blocks and preview blocks
		for (int x = 0; x<10; x++){
			for (int y = 2; y<22; y++){
				
				if (blocks[x][y] != null){
					if (blocks[x][y].isPreview()){
						g.setColor(Color.GRAY);
						g.fillRect(x*side+200, (y-2)*side, side, side);
						g.setColor(Color.lightGray);
						g.drawLine(x*side+200, (y-2)*side, (x+1)*side+200, (y-1)*side);
					}
					else{
						g.setColor(blocks[x][y].getColor());
						g.fillRect(x*side+200, (y-2)*side, side, side);
						g.setColor(Color.black);
						g.drawRect(x*side+200, (y-2)*side, side, side);
						
					}
				}}}
			
	
		// Draw the preview for the next shape
		
		g.setColor(Color.lightGray);
		g.fillRect(530,30,140,140);
		if (nextShape==ShapeType.t){
			g.setColor(Color.magenta);
			g.fillRect(585,70,30,30);
			g.fillRect(585, 100, 30, 30);
			g.fillRect(555, 100, 30, 30);
			g.fillRect(615, 100, 30, 30);
			g.setColor(Color.black);
			g.drawRect(585,70,30,30);
			g.drawRect(585, 100, 30, 30);
			g.drawRect(555, 100, 30, 30);
			g.drawRect(615, 100, 30, 30);
	
		}
		else if(nextShape == ShapeType.line){
			g.setColor(Color.cyan);
			g.fillRect(585,70,30,30);
			g.fillRect(585, 100, 30, 30);
			g.fillRect(585, 130, 30, 30);
			g.fillRect(585, 40, 30, 30);
			g.setColor(Color.black);
			g.drawRect(585,70,30,30);
			g.drawRect(585, 100, 30, 30);
			g.drawRect(585, 130, 30, 30);
			g.drawRect(585, 40, 30, 30);
		}
		else if (nextShape == ShapeType.backzag){
			g.setColor(Color.green);
			g.fillRect(585,70,30,30);
			g.fillRect(585, 100, 30, 30);
			g.fillRect(555, 100, 30, 30);
			g.fillRect(615, 70, 30, 30);
			g.setColor(Color.black);
			g.drawRect(585,70,30,30);
			g.drawRect(585, 100, 30, 30);
			g.drawRect(555, 100, 30, 30);
			g.drawRect(615, 70, 30, 30);
		}
		else if (nextShape == ShapeType.zag){
			g.setColor(Color.red);
			g.fillRect(585,70,30,30);
			g.fillRect(585, 100, 30, 30);
			g.fillRect(555, 70, 30, 30);
			g.fillRect(615, 100, 30, 30);
			g.setColor(Color.black);
			g.drawRect(585,70,30,30);
			g.drawRect(585, 100, 30, 30);
			g.drawRect(555, 70, 30, 30);
			g.drawRect(615, 100, 30, 30);
		}
		else if (nextShape == ShapeType.l){
			g.setColor(Color.blue);
			g.fillRect(555,100,30,30);
			g.fillRect(585, 100, 30, 30);
			g.fillRect(615, 100, 30, 30);
			g.fillRect(555, 70, 30, 30);
			g.setColor(Color.black);
			g.drawRect(555,100,30,30);
			g.drawRect(585, 100, 30, 30);
			g.drawRect(615, 100, 30, 30);
			g.drawRect(555, 70, 30, 30);
		}
		else if (nextShape == ShapeType.backl){
			g.setColor(Color.orange);
			g.fillRect(555,100,30,30);
			g.fillRect(585, 100, 30, 30);
			g.fillRect(615, 100, 30, 30);
			g.fillRect(615, 70, 30, 30);
			g.setColor(Color.black);
			g.drawRect(555,100,30,30);
			g.drawRect(585, 100, 30, 30);
			g.drawRect(615, 100, 30, 30);
			g.drawRect(615, 70, 30, 30);
		}
		else if (nextShape == ShapeType.square){
			g.setColor(Color.yellow);
			g.fillRect(600,70,30,30);
			g.fillRect(600, 100, 30, 30);
			g.fillRect(570, 70, 30, 30);
			g.fillRect(570, 100, 30, 30);
			g.setColor(Color.black);
			g.drawRect(600,70,30,30);
			g.drawRect(600, 100, 30, 30);
			g.drawRect(570, 70, 30, 30);
			g.drawRect(570, 100, 30, 30);
		}
		
		// Draw the saved shape
		
		g.setColor(Color.lightGray);
		g.fillRect(30,30,140,140);
		if (savedShape==ShapeType.t){
			g.setColor(Color.magenta);
			g.fillRect(85,70,30,30);
			g.fillRect(85, 100, 30, 30);
			g.fillRect(55, 100, 30, 30);
			g.fillRect(115, 100, 30, 30);
			g.setColor(Color.black);
			g.drawRect(85,70,30,30);
			g.drawRect(85, 100, 30, 30);
			g.drawRect(55, 100, 30, 30);
			g.drawRect(115, 100, 30, 30);
	
		}
		else if(savedShape == ShapeType.line){
			g.setColor(Color.cyan);
			g.fillRect(85,70,30,30);
			g.fillRect(85, 100, 30, 30);
			g.fillRect(85, 130, 30, 30);
			g.fillRect(85, 40, 30, 30);
			g.setColor(Color.black);
			g.drawRect(85,70,30,30);
			g.drawRect(85, 100, 30, 30);
			g.drawRect(85, 130, 30, 30);
			g.drawRect(85, 40, 30, 30);
		}
		else if (savedShape == ShapeType.backzag){
			g.setColor(Color.green);
			g.fillRect(85,70,30,30);
			g.fillRect(85, 100, 30, 30);
			g.fillRect(55, 100, 30, 30);
			g.fillRect(115, 70, 30, 30);
			g.setColor(Color.black);
			g.drawRect(85,70,30,30);
			g.drawRect(85, 100, 30, 30);
			g.drawRect(55, 100, 30, 30);
			g.drawRect(115, 70, 30, 30);
		}
		else if (savedShape == ShapeType.zag){
			g.setColor(Color.red);
			g.fillRect(85,70,30,30);
			g.fillRect(85, 100, 30, 30);
			g.fillRect(55, 70, 30, 30);
			g.fillRect(115, 100, 30, 30);
			g.setColor(Color.black);
			g.drawRect(85,70,30,30);
			g.drawRect(85, 100, 30, 30);
			g.drawRect(55, 70, 30, 30);
			g.drawRect(115, 100, 30, 30);
		}
		else if (savedShape == ShapeType.l){
			g.setColor(Color.blue);
			g.fillRect(55,100,30,30);
			g.fillRect(85, 100, 30, 30);
			g.fillRect(115, 100, 30, 30);
			g.fillRect(55, 70, 30, 30);
			g.setColor(Color.black);
			g.drawRect(55,100,30,30);
			g.drawRect(85, 100, 30, 30);
			g.drawRect(115, 100, 30, 30);
			g.drawRect(55, 70, 30, 30);
		}
		else if (savedShape == ShapeType.backl){
			g.setColor(Color.orange);
			g.fillRect(55,100,30,30);
			g.fillRect(85, 100, 30, 30);
			g.fillRect(115, 100, 30, 30);
			g.fillRect(115, 70, 30, 30);
			g.setColor(Color.black);
			g.drawRect(55,100,30,30);
			g.drawRect(85, 100, 30, 30);
			g.drawRect(115, 100, 30, 30);
			g.drawRect(115, 70, 30, 30);
		}
		else if (savedShape == ShapeType.square){
			g.setColor(Color.yellow);
			g.fillRect(100,70,30,30);
			g.fillRect(100, 100, 30, 30);
			g.fillRect(70, 70, 30, 30);
			g.fillRect(70, 100, 30, 30);
			g.setColor(Color.black);
			g.drawRect(100,70,30,30);
			g.drawRect(100, 100, 30, 30);
			g.drawRect(70, 70, 30, 30);
			g.drawRect(70, 100, 30, 30);
		}
		if (lost){
			g.setColor(Color.black);
			g.fillRect(0, 0, 700, 600);
			Font loser = new Font("loser",4,68);
			g.setColor(Color.black);
			g.fillRect(200, 240, 300, 70);
			g.setColor(Color.red);
			g.setFont(loser);
			g.drawString("Cheryl is butter", 100, 300);
		}
	}
	


}
