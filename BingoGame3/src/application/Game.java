package application;

/**
 * author Monera 205807308 & Reem 313227928
 */
import java.util.Random;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.lang.Thread;

public class Game {

	/**
	 * Variables Deceleration
	 */

	private FlowPane BigGrid = new FlowPane();
	private HBox hbox = new HBox();
	private BorderPane border = new BorderPane(); // main pane
	private int rndNum; // the generated random number
	private Thread[] threads; // array of threads for the players
	private Player[] playersArray; // array of players
	private ScrollPane sp = new ScrollPane(); // scroll pane when resizing
	private int cardSize; // card size

	/**
	 * 
	 * @param Stage
	 * @param playersNum
	 * @param cardSize
	 */
	public Game(Stage Stage, int playersNum, int cardSize) {
		/* Game initializations */
		this.cardSize = cardSize;
		BigGrid.setPrefWrapLength(600);
		BigGrid.setHgap(15.0);
		threads = new Thread[playersNum];
		playersArray = new Player[playersNum];
		BigGrid.setVgap(15.0);
		hbox.setPadding(new Insets(15, 12, 15, 12));
		hbox.setSpacing(10);
		hbox.setStyle("-fx-background-color: #ffaaaa;");
		Button finish = new Button("Finish");
		finish.setPrefSize(100, 20);
		Button next = new Button("Next");
		Label label = new Label("");
		TextArea textArea = new TextArea();
		textArea.setPrefSize(600, 100);
		textArea.setText("Welcome to our Bingo Game (Made By Monera Saad & Reem Hamed\n"
				+ "Press 'Next' Button to start the game (to generate a random number)\n"
				+ "Each player should check in his card contains the generated number\n"
				+ "If true, then the pressed number will be coloed as 'red'\n" + "Else, it will remain black\n"
				+ "\n\nPress 'Bingo' button only if you have any of these:\n"
				+ "(1)You colored a full row in your card\n" + "(2)You colored a full column in your card\n"
				+ "(3)You colored a diagnoal (main diagonal or secondary diagonal)\n"
				+ "\n\nPress 'Finish' if you want to exit the game");
		textArea.setEditable(false);
		next.setPrefSize(100, 20);
		hbox.getChildren().addAll(next, label, finish, textArea);
		label.setPrefSize(100, 30);
		label.setStyle("-fx-border-color: black;");

		// what to do when 'next' button is clicked?
		next.setOnAction(e -> {
			Random rnd = new Random();
			this.rndNum = rnd.nextInt(100) + 1; // generate a new random number (from 1-100)
			for (int i = 0; i < playersArray.length; i++) {
				playersArray[i].setLastRandNum(this.rndNum); // set the random number for each player (update them)
			}
			label.setText("" + this.rndNum); // update the text field of the random number
		});

		// create 'x' number of threads (each player is a different thread)
		for (int i = 0; i < threads.length; i++) {

			try {

				Player p = new Player(cardSize, i, Stage); // create an instance of player
				p.setLastRandNum(0); // default initialization of random number is 0
				playersArray[i] = p; // add the player to players array
				threads[i] = new Thread(playersArray[i]); // create a thread
				threads[i].setDaemon(true);// Terminate the running thread if the application exits
				threads[i].start(); // start the thread
				BigGrid.getChildren().add(playersArray[i].getRoot()); // add the cards to the main grid

			} catch (IllegalThreadStateException e) {
				e.printStackTrace(); // print a suitable message when exception is thrown
			}

		}
		/* edit the main border */
		BigGrid.setAlignment(Pos.CENTER);
		border.setTop(hbox);
		sp.setContent(BigGrid);
		sp.setPannable(true);
		sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		border.setCenter(sp);

		// what to do when 'finish' button is clicked?
		finish.setOnAction(e -> {
			Alert finishAlert = new Alert(AlertType.INFORMATION, "Farwell!", ButtonType.OK);
			finishAlert.setHeaderText("Game has ended");
			border.setVisible(false);
			finishAlert.show(); // show the information alert that the game has been ended
			Stage.hide(); // hide the game window because the game has been ended

		});

	}

	/**
	 * 
	 * @return border
	 */
	public BorderPane getBorder() {
		return border;
	}

	/**
	 * 
	 * @return BigGrid
	 */
	public FlowPane getBigGrid() {
		return BigGrid;
	}

	/**
	 * 
	 * @return rndNum
	 */
	public int getRndNum() {
		return rndNum;
	}

	/**
	 * 
	 * @param rndNum
	 */
	public void setRndNum(int rndNum) {
		this.rndNum = rndNum;
	}

}
