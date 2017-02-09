
import javafx.scene.input.MouseEvent;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.shape.Rectangle;

/**
 * MineSweepBase constructs the window in which the game of MineSweeper is played.
 * @author danberenberg
 *
 */
public abstract class MineSweepBase extends Application{
	//various fields 
	
	public static final int GAME_PANE_DIMENSION_X = 400;
	public static final int GAME_PANE_DIMENSION_Y = 400;
	private Pane gamePane;
	private Scene gameScene;
	
	//checkBox fields
	private static int checked;
	private CheckBox checkFlag = new CheckBox("Flag");
	
	//visible representation of flag counter for user
	private Rectangle bombBoard = new Rectangle(10,10,45,25);
	/**
	 * construct a window with these dimensions
	 */
	public MineSweepBase(){
		gamePane = new Pane();
		gameScene = new Scene(gamePane,GAME_PANE_DIMENSION_X,GAME_PANE_DIMENSION_Y);
		BackgroundFill myBF = new BackgroundFill(Color.LIGHTGREY, new CornerRadii(1),
			         new Insets(0.0,0.0,0.0,0.0));
		gamePane.setBackground(new Background(myBF));
		
	}
	/**
	 * create stage and setUp the event handler
	 */
	public void start(Stage primaryStage) throws Exception{
		primaryStage.setTitle("MineSweeper!");
		primaryStage.setScene(gameScene);
		primaryStage.show();
		setUpAnimation();
		start();
	}
	
	/**
	 * adds a cell to the gameboard by adding each of the cells components.
	 * @param cell
	 */
	public void addCell(Cell cell) {
	    gamePane.getChildren().add(cell.getSide0());
	    gamePane.getChildren().add(cell.getSide1());
	    gamePane.getChildren().add(cell.getSide2());
	    gamePane.getChildren().add(cell.getSide3());
	    gamePane.getChildren().add(cell.getColor());
	}
	
	/**
	 * sets up the reset button, flag button, and mouse click event handler
	 */
	public void setUpAnimation(){
		Button btReset = new Button("New Game");
		btReset.setLayoutX(260);
		btReset.setLayoutY(10);
		gamePane.getChildren().add(btReset);
		btReset.setOnAction((ActionEvent e)->{
			reset();
		});
		
		checkFlag.setLayoutX(85);
		checkFlag.setLayoutY(10);
		gamePane.getChildren().add(checkFlag);
		checkFlag.setOnAction((ActionEvent e2) ->{
			check();
		});
		
		Button btQuit = new Button("Quit");
		btQuit.setLayoutX(350);
		btQuit.setLayoutY(10);
		
		gamePane.getChildren().add(btQuit);
		btQuit.setOnAction((ActionEvent e3)->{
			System.exit(0);
		});
		bombBoard.setFill(Color.BLACK);
		bombBoard.setStroke(Color.GOLD);
		
        gamePane.addEventHandler(MouseEvent.MOUSE_CLICKED,setUpHandler());
	}
	
	/**
	 * this adds each number that goes inside of non-bomb cells.
	 * @param text
	 */
	public void addText(Text text){
		gamePane.getChildren().add(text);
	}
	
	/**
	 * remove text from the window
	 * @param text
	 */
	public void removeText(Text text){
		gamePane.getChildren().remove(text);
	}
	
	/**
	 * adds in the bombBoard rectangle, made as a method so that
	 * it may be added later in mineSweepWindow
	 */
	public void addBombBoard(){
		gamePane.getChildren().add(bombBoard);
	}
	
	/**
	 * sets up the event handler for mouse clicks
	 * @return
	 */
	public EventHandler<MouseEvent> setUpHandler(){
		EventHandler<MouseEvent> eventHandler1 = (MouseEvent e1) ->{
			wasClicked(e1);
		};
		return eventHandler1;
	}
	
	/**
	 * based on the amount of times the check box the checkBox has been clicked, 
	 * set the checkFlag button to checked or not checked 
	 * @return
	 */
	public boolean check(){
		checked++;

		if (checked %2 == 1){
			checkFlag.setIndeterminate(true);
		}else{
			checkFlag.setIndeterminate(false);
		}
		
		return checkFlag.isSelected();
	}
		
	/**
	 * abstract method to handle clicks, implemented in MineSweepWindow
	 * @param e1
	 */
	public  abstract void wasClicked(MouseEvent e1);
	/**
	 * abstract start() method implemented in MineSweepWindow
	 */
	public abstract void start();
	/**
	 * abstract reset() method implemented in MineSweepWindow
	 */
	public abstract void reset();
	
	
	
}
