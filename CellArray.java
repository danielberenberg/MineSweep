import javafx.scene.paint.Color;

/**
 * 
 * @author danberenberg
 *CellArray represents one row of cells in the cellMatrix in MineSweepWindow
 */
public class CellArray{
	private Cell[] cells;	
	
	/**
	 * Construct an array of cells in the specified window, with the specified color and y coordinate
	 * @param yCoord
	 * @param mineField
	 * @param color
	 */
	public CellArray(int yCoord, MineSweepWindow mineField, Color color){
		cells = new Cell[42];
		for (int i = 0; i < cells.length; i++){
			cells[i] = new Cell(10*(i-1),yCoord,mineField);
			cells[i].setCellIndex(i);
			cells[i].setColor(color);
				
		}
			
	}
		
	/**
	 * find one cell in the row
	 * @param i
	 * @return
	 */
	public Cell getCellAtIndex(int i){
		return cells[i];
	}
	/**
	 * get the length of the row.
	 * @return
	 */
	public int length(){
		return this.cells.length;
	}
	
}
