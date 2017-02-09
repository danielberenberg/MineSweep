import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Cell represents one box in the MineSweepWindow.
 * @author danberenberg
 *
 */
public class Cell{
	private boolean boundary = false;		//denotes whether or not cell is on the boundary.
	
	private boolean flagged = false;		//denotes whether or not cell is flagged.
	
	private Text flag;						//text representation of flag for user to denote a cell as a bomb
	
	private int cellRow;                    //the row where the cell lives in the cellMatrix
	
	private boolean bombStatus = false;		//whether the cell is a bomb or not
	
	private int numberAdjacent;				//number of bombs adjacent to a cell.
	
	private int cellIndex;					//index of the row in which the cell lives
	
	private Cell[] adjBox;					//the 8 cells surrounding the cell
	
	private static final int HEIGHT = 10;	//the size of every cell

	//location fields
	private Point2D origin;					//these are the coordinates
	private Point2D topRight;				//of the vertices of a cell
	private Point2D bottomLeft;
	private Point2D bottomRight;
	

	//adjacency fields
	private Point2D[] vertices = new Point2D[4];
	
	//colorTangle- color of the cell
	protected Rectangle colorTangle;
	
	//visible representation of cell
	Line side0;
	Line side1;
	Line side2;
	Line side3;

	
	/**
	 * Constructor - creates a square with a height of 10 and various attributes 
	 * that denote it's location in the matrix of cells in the window.
	 * @param originX
	 * @param originY
	 * @param mineField
	 */
	public Cell(int originX, int originY, MineSweepWindow mineField){
		//Define vertices and add them to the list
		this.origin = new Point2D(originX,originY);
		vertices[0] = this.origin;
		
		topRight = new Point2D(origin.getX()+HEIGHT, origin.getY());
		vertices[1] = this.topRight;
		
		bottomLeft = new Point2D(origin.getX(),origin.getY()+HEIGHT);
		vertices[2] = this.bottomLeft;
		
		bottomRight = new Point2D(origin.getX()+HEIGHT,origin.getY()+HEIGHT);
		vertices[3] = this.bottomRight;
		
		
		//define cell box - a line of width 10 pixels.
		this.side0 = new Line(this.origin.getX(),this.origin.getY(),this.bottomLeft.getX(),this.bottomLeft.getY());
		this.side1 = new Line(this.bottomLeft.getX(),this.bottomLeft.getY(),this.bottomRight.getX(),this.bottomRight.getY());
		this.side2 = new Line(this.bottomRight.getX(),this.bottomRight.getY(),this.topRight.getX(),this.topRight.getY());
		this.side3 = new Line(this.topRight.getX(),this.topRight.getY(),this.origin.getX(),this.origin.getY());
		
		this.colorTangle = new Rectangle(origin.getX(),origin.getY(),10,10);
		mineField.addCell(this);
		
			
	}
	/**
	 * get the vertices in the cell.
	 * @return
	 */
	public Point2D[] getVertices(){
		return this.vertices;
	}
	
	/**
	 * return the left side of the square
	 * @return
	 */
	public Line getSide0(){
		return this.side0;
	}
	/**
	 * return bottom side of square
	 * @return
	 */
	public Line getSide1(){
		return this.side1;
	}
	/**
	 * return the right side of the square
	 * @return
	 */
	public Line getSide2(){
		return this.side2;
	}
	/**
	 * return the top side of the square
	 * @return
	 */
	public Line getSide3(){
		return this.side3;
	}
	
	/**
	 * get the color box of the cell
	 * @return
	 */
	public Rectangle getColor(){
		return this.colorTangle;
	}
	
	/**
	 * set the color of the cell
	 * @param color
	 */
	public void setColor(Color color){
		colorTangle.setFill(color);
	}
	
	public void setSideColor(Color color,Line sides[]){
		for (int i = 0; i < sides.length; i++){
			sides[i].setStroke(color);
		}
	}
	/**
	 * denote the cell as a bomb or not a bomb.
	 */
	public void setBombStatus(boolean status){
		this.bombStatus = status;
		if (this.bombStatus){
		colorTangle.setFill(Color.RED);
		}else{
		colorTangle.setFill(Color.LIGHTGREEN);
		}
	}
	/**
	 * denote the index of the cell's location in its row.
	 * @param i
	 */
	public void setCellIndex(int i){
		cellIndex = i;
	}
	
	/**
	 * denote the row in the cell matrix of the cell.
	 * @param j
	 */
	public void setCellRow(int j){
		cellRow = j;
	}
	/**
	 * get the index of the cell
	 * @return
	 */
	public int getCellIndex(){
		return cellIndex;
	}
	
	/**
	 * get the row of the cell.
	 * @return
	 */
	public int getCellRow(){
		return cellRow;
	}
	
	/**
	 * get the origin point of the cell.
	 * @return
	 */
	public Point2D getOrigin(){
		return this.origin;
	}
	/**
	 * count the amount of bombs in the adjacency box of the cell
	 * @param adjBox
	 */
	public void countBombsAdjacent(Cell[] adjBox){
	
		for (int i = 0; i < adjBox.length;i++ ){
			Cell cell = adjBox[i];
			if (cell.isBomb()){
				numberAdjacent++;
			}
			
		}
	}
	/**
	 * return the amount of bombs adjacent to the cell.
	 * @return
	 */
	public int getNumberAdj(){
		return this.numberAdjacent;
	}
	/**
	 * For bombs - set number adjacent for a bomb so that is it not a null value.
	 * @param n
	 */
	public void setNumberAdj(int n){
		this.numberAdjacent = n;
	}
	/**
	 * get the bombStatus of the cell.
	 * @return
	 */
	public boolean isBomb(){
		return this.bombStatus;
	}
	
	/**
	 * Create an adjacency box for the cell.
	 * An adjacency box is those 8 squares that are sharing at least 1 vertex with the center cell.
	 * @param top
	 * @param center
	 * @param bottom
	 * @param centerCell
	 * @return
	 */
	public Cell[] adjacencyBox(CellArray top, CellArray center, CellArray bottom,Cell centerCell){
		Cell[] adjacencyBox = new Cell[8];
		int centerIndex = centerCell.getCellIndex();
		adjacencyBox[0] = top.getCellAtIndex(centerIndex - 1);
		adjacencyBox[1] = top.getCellAtIndex(centerIndex);
		adjacencyBox[2] = top.getCellAtIndex(centerIndex+1);
		adjacencyBox[3] = center.getCellAtIndex(centerIndex - 1);
		adjacencyBox[4] = center.getCellAtIndex(centerIndex + 1);
		adjacencyBox[5] = bottom.getCellAtIndex(centerIndex - 1);
		adjacencyBox[6] = bottom.getCellAtIndex(centerIndex);
		adjacencyBox[7] = bottom.getCellAtIndex(centerIndex+1);	
		return adjacencyBox;
	}
	/**
	 * get the adjacencyBox of a this cell.
	 * @return
	 */
	public Cell[] getAdjBox(){
		return this.adjBox;
	}
	/** 
	 * Set the adjacencyBox of a given cell.
	 * @param adjB
	 */
	public void setAdjBox(Cell[] adjB){
		this.adjBox = adjB;
	}
	
	/**
	 * Set the whether or not a cell is a boundary cell.
	 */
	public void setBoundary(){
		boundary = true;
	}
	
	/**
	 * Determine whether or not a cell is a boundary cell.
	 * @return
	 */
	public boolean getBoundary(){
		return boundary;
	}
	
	/**
	 * Determine whether or not a cell is flagged.
	 * @return
	 */
	public boolean getFlagged(){
		return flagged;
		
	/**
	 * Set a cell as flagged (as a bomb) or not.
	 */
	}
	public void setFlagged(boolean b){
		flagged = b;
	}
	
	/**
	 * Set the text representation of the flag on the cell. 
	 * This is important because the game needs to know where exactly 
	 * the flags are in order to remove them efficiently.
	 * @param flag
	 */
	public void setFlag(Text flag){
		this.flag = flag;
	
	/**
	 * Get the text representation of the flag on the cell.
	 */
	}
	public Text getFlag(){
		return flag;
	}
	
	
}
