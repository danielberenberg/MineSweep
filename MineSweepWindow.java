import java.util.Random;
import javafx.scene.text.Text;
import javafx.application.Application;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 * 
 * @author danberenberg
 *MineSweepWindow hosts the window for the game, creates the cells inside of it,
 *and randomly places the bombs
 */
public class MineSweepWindow extends MineSweepBase{
	//Various fields to be used for the game
	private static int bombCount = 0;
	
	//bottom layer of gameboard
	private CellArray[] cellMatrix1;
	
	//top layer of game board
	private static CellArray[] cellMatrix2;
	
	//random variable to be used later in bomb placement
	private Random rand = new Random();
	
	//the amount of non bomb cells 
	private static int cellCount = 1440;
	
	//array of all bombs
	private Cell[] bombs = new Cell[200];
	
	//the amount of bombs the user can identify
	private static int countBombs =100;
	private Text count_Bombs;
	
	//win and loss texts
	private Text winMessage;
	private Text lossMessage;
	
	
	/**
	 * constructor to initialize this window.
	 */
	public MineSweepWindow(){
		super();
		
	}
	/**
	 * start() method sets the game board for the first time upon initilization of the window
	 */
	@Override
	public void start() {
		//set game board
		cellMatrix1 = new CellArray[42];
		//blank grey squares
		cellMatrix2 = new CellArray[42];
		
		//reset
		this.reset();
		addBombBoard();
		displayCountBombs();
	}
		
	/**
	 * get the number of bombs in the window.
	 * @return
	 */
	public int getBombCount(){
		return bombCount;
	}
	/**
	 * add 1 to the bomb count.
	 */
	public void resetBombCount(){
		MineSweepWindow.bombCount++;
	}
	
	/**
	 * set 200 bombs in distinct places on the game board.
	 */
	public void setBombs(){
		while (MineSweepWindow.bombCount < 100){
			int m = rand.nextInt(35)+5;
			int n = rand.nextInt(40)+1;
		
			setBomb(m,n);	
		}
	}
	/**
	 * set one bomb in a cell that is not already denoted as a bomb cell.
	 * @param m
	 * @param n
	 */
	public void setBomb(int m, int n){
		if (cellMatrix1[m].getCellAtIndex(n).isBomb() == false){
			cellMatrix1[m].getCellAtIndex(n).setBombStatus(true);
			bombs[bombCount] = cellMatrix1[m].getCellAtIndex(n);
			resetBombCount();
		}
		
		
	}
	/**
	 * clear the bottom  gameboard of bombs
	 */
	public void clearBottomLayer(){
		for (int i = 4; i < cellMatrix1.length; i++){
			this.cellMatrix1[i] = new CellArray(10*i,this,Color.PLUM);
			
		}
	}
	/**
	 * reset the top, clickable gameBoard to all grey cells
	 */
	public void clearTopLayer(){
		for(int i = 4; i < cellMatrix2.length; i++){
			MineSweepWindow.cellMatrix2[i] = new CellArray(10*i,this,Color.LIGHTGREY);
		}
	}
	
	/**
	 * set the boundaries of the window, whether a cell is boundary or not 
	 * is important in the implementation of cell deletion
	 */
	public void setBoundaries(){
		for (int k = 0; k < 42; k++){
			Cell cell = cellMatrix1[4].getCellAtIndex(k);
			cell.setBoundary();
		
			Cell topCell = cellMatrix2[4].getCellAtIndex(k);
			Line[] sides = {topCell.getSide0(),topCell.getSide1(),topCell.getSide2(), topCell.getSide3()};
			topCell.setSideColor(Color.LIGHTGREY, sides);
			
			Cell cell2 = cellMatrix1[41].getCellAtIndex(k);
			cell2.setBoundary();
			
			Cell topCell2 = cellMatrix2[41].getCellAtIndex(k);
			Line[] sides2 = {topCell2.getSide0(),topCell2.getSide1(),topCell2.getSide2()};
			topCell2.setSideColor(Color.LIGHTGREY, sides2);
		}
		
		for (int s = 5; s < 42; s++ ){
			Cell cell3 = cellMatrix1[s].getCellAtIndex(0);
			cell3.setBoundary();
			
			Cell topCell3 = cellMatrix2[s].getCellAtIndex(0);
			Line[] sides3 = {topCell3.getSide0(),topCell3.getSide1(), topCell3.getSide3()};
			topCell3.setSideColor(Color.LIGHTGREY, sides3);
			
			Cell cell4 = cellMatrix1[s].getCellAtIndex(41);
			cell4.setBoundary();
			
			Cell topCell4 = cellMatrix2[s].getCellAtIndex(41);
			Line[] sides4 = {topCell4.getSide1(),topCell4.getSide2(), topCell4.getSide3()};
			cellMatrix2[s].getCellAtIndex(41).setSideColor(Color.LIGHTGREY,sides4);
			
		}
	}
	/**
	 * reset() is called every time the reset button is touched and at the start of the game.
	 * the method clears the board, resets the bomb locations, and does several actions to clear
	 * the evidence of the last game played.
	 */
	public void reset(){
		removeText(lossMessage);
		removeText(winMessage);
		bombCount = 0;
		countBombs = 100;
		clearBottomLayer();
		this.setBombs();
		cellCount = (1440 - bombCount);
		for (int i = 5; i < 41; i++){
			for(int j = 1; j < 41; j++){
				Cell cell = cellMatrix1[i].getCellAtIndex(j);
				cell.setCellRow(i);
				Cell[] adjacencyBox = cell.adjacencyBox(cellMatrix1[i - 1],cellMatrix1[i], cellMatrix1[i+1],cell);
				cell.setAdjBox(adjacencyBox);
				if (cell.isBomb() == false){
					cell.countBombsAdjacent(adjacencyBox);
					if (cell.getNumberAdj()!=0){
						Text numberAdjacent = 
						new Text(cell.getVertices()[2].getX(),cell.getVertices()[2].getY(),
								String.valueOf(cell.getNumberAdj()));
						this.addText(numberAdjacent);
					}
				}else{	
					cell.setNumberAdj(25);
				}
			}
		}
		clearTopLayer();
		setBoundaries();
		displayCountBombs();
		
	}
	/**
	 * the deleteCell method recursively deletes cells until the chain of 
	 * deletion reaches a boundary.
	 * @param cell
	 */
	public static void deleteCell(Cell cell,Cell underCell){
		
		//delete a cell
		if (underCell.getBoundary() == false && !cell.getFlagged()){
			if (cell.colorTangle.getFill() == Color.LIGHTGREY){
				cell.colorTangle.setFill(Color.TRANSPARENT);
				cellCount--;
			//check to see if surrounded by nothing, if so, delete all cells in Adjacency box
				if(underCell.getNumberAdj() == 0){
					Cell[] topCellAdjBox = new Cell[8];
					for (int n = 0; n < topCellAdjBox.length; n++){
						if (cellMatrix2[underCell.getAdjBox()[n].getCellRow()] != null){
							Cell newCell = 
								cellMatrix2[underCell.getAdjBox()[n].getCellRow()].
								getCellAtIndex(underCell.getAdjBox()[n].getCellIndex());
							topCellAdjBox[n] = newCell;
						}
					}
					cell.setAdjBox(topCellAdjBox);
					for (int m = 0; m < 8; m++){
						deleteCell(cell.getAdjBox()[m],underCell.getAdjBox()[m]);
				
					}
				}
			}
		}
	}
	
	/**
	 * find the cell that was clicked and either places a flag, deletes a flag, deletes a cell,
	 * or loses the game or wins the game.
	 */
	public void wasClicked(MouseEvent e1){
		double xCoord = e1.getX();
		double yCoord = e1.getY();
		
		for (int i = 5; i < 41; i++){
			for (int j = 1; j < 41; j++){
				Cell cell = cellMatrix2[i].getCellAtIndex(j);
				if((xCoord >= cell.getVertices()[0].getX() && xCoord< cell.getVertices()[1].getX()) &&
				(yCoord >=cell.getVertices()[0].getY() && yCoord < cell.getVertices()[2].getY())){
					Cell pointCell = cell;
	
					//if the flag checkbox is checked and the cell is flagged as a bomb
					if(check() && pointCell.getFlagged()){
						this.removeText(pointCell.getFlag());
						pointCell.setFlagged(false);
						pointCell.setFlag(null);
						
					
					//if the flag checkbox is checked and the cell has not been deleted
					}else if (check() && pointCell.getColor().getFill() == Color.LIGHTGREY){
						if (countBombs != 0){
							countBombs--;
						}
						
						
						displayCountBombs();
						pointCell.setFlagged(true);
						Text flag = 
						new Text(pointCell.getVertices()[2].getX(),pointCell.getVertices()[2].getY(),
							String.valueOf('+'));	
						pointCell.setFlag(flag);
						this.addText(pointCell.getFlag());
						
					//if the cell is not flagged and the checkbox is not checked 
					}else if (!pointCell.getFlagged() && !check()){
						if (cellMatrix1[i].getCellAtIndex(j).isBomb()){
							youLose();
						}else if (cellCount == 0){
							youWin();
						}else{
							deleteCell(pointCell,cellMatrix1[i].getCellAtIndex(j));
						}
					}
				}
			}				
		}
	}
	
	/**
	 * the lose method for when a player clicks a bomb
	 */
	public void youLose(){
		lossMessage = new Text(165,30,"YOU LOSE");
		lossMessage.setFill(Color.RED);
		lossMessage.setScaleX(1.75);
		lossMessage.setScaleY(1.75);
		this.addText(lossMessage);
		for(int i = 5; i < cellMatrix2.length; i++){
			for(int j = 1; j < cellMatrix2[i].length()-1; j++){
				Cell cell = cellMatrix2[i].getCellAtIndex(j);
				cell.setColor(Color.TRANSPARENT);
			}
		}
	}
	
	/**
	 * the win method for after the user has correctly identified all nonbombs.
	 */
	public void youWin(){
		winMessage = new Text(170,30,"YOU WIN!");
		winMessage.setFill(Color.GREEN);
		winMessage.setScaleX(1.75);
		winMessage.setScaleY(1.75);
		this.addText(winMessage);
		for(int i = 0; i < bombs.length; i++){
			Text flag = new Text(bombs[i].getVertices()[2].getX(),bombs[i].getVertices()[2].getY(),
					String.valueOf('+'));
			bombs[i].setFlag(flag);	
			bombs[i].setFlagged(true);
			this.addText(bombs[i].getFlag());
		}
	}
	
	/**
	 * display the number of bombs remaining, based on the amount of flags on the board,
	 * does not actually reflect the amount of bombs on the board, only what the user
	 * has identified as a bomb
	 */
	public void displayCountBombs(){
		this.removeText(this.count_Bombs);
		this.count_Bombs = new Text(20,25,String.valueOf(countBombs));
		count_Bombs.setFill(Color.RED);
		this.addText(count_Bombs);
	}
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args){
		Application.launch(args);
	}
}
