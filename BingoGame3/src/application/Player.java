package application;

/**
 * author Monera 205807308 & Reem 313227928
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class Player implements Runnable {

	/**
	 * Variables Deceleration
	 */

	private GridPane Card; // each player has a card filled with random numbers
	private int[][] pressed; // each player has an array of the pressed numbers (color = red)
	private int playerID; // each player has a different id (start from 1)
	private int count; // a variable used when checking 'bingo'
	private int cardSize; // card size
	private int lastRandNum; // the last generated number (given from Game class)
	private Button bingo; // each player has a 'bingo' button
	private VBox vbox;
	private Stage stage;
	private Label playerNum; // displaying the player number at the window
	private VBox root;
	private HashMap<Integer, int[]> hash; // each generated number has a coordinates in the array, in this map we save
											// both of them (number + coords)
	private ArrayList<Integer> myNumbers; // each player has generated numbers. we save those numbers in this list

	/**
	 * 
	 * @param cardSize
	 * @param playerID
	 * @param stage
	 */
	public Player(int cardSize, int playerID, Stage stage) {
		/* Initialization of variables */
		this.cardSize = cardSize;
		this.stage = stage;
		this.myNumbers = new ArrayList<Integer>();
		this.Card = new GridPane();
		this.count = 0;
		this.pressed = new int[this.cardSize][this.cardSize];
		this.playerID = playerID;
		this.playerNum = new Label("Player " + (playerID + 1) + ":");
		this.bingo = new Button("Bingo!");
		this.hash = new HashMap<Integer, int[]>();
		this.root = new VBox();
		this.CreateCard();

	}

	/**
	 * @return
	 */
	public void CreateCard() {

		Card.setStyle("-fx-background-color: Pink; -fx-grid-lines-visible: false");
		/* Create a card for each player */
		for (int i = 0; i < pressed.length; i++) { // pressed.length = number of players

			for (int j = 0; j < pressed.length; j++) {
				vbox = new VBox(5); // each player has a vbox that contains bingo button, player number and a card
									// of numbers
				vbox.setPadding(new Insets(1));
				bingo.setMaxWidth(60.0);

				Random rand = new Random();
				int number = rand.nextInt(100) + 1; // generate a random number from 1-100
				while ((myNumbers.contains(number))) { // to remove duplicates: each number should appear once
														// each player has a list called myNumbers that contains his
														// generated numbers
					number = rand.nextInt(100) + 1; // generate again
				}
				myNumbers.add(number); // add it to the array
				int[] indexList = new int[2]; // to save the i,j of the button
				indexList[0] = i;
				indexList[1] = j;
				hash.put(number, indexList); // each number has its coordinates in each card
				Card.setHgap(5);
				Card.setVgap(5);
				Button num = new Button("" + number);
				num.setMinSize(50, 30);
				num.setMaxWidth(50.0);
				Card.add(bingo, 0, 0);
				Card.add(num, j, i);
				vbox.getChildren().addAll(this.playerNum, this.bingo, this.Card); // add all elements to the vbox
				root.getChildren().add(vbox); // add all vbox'es to the root (main vbox)

				// what to do when a number is pressed?
				num.setOnAction(e -> {
					int pressedNumber = Integer.parseInt(num.getText().toString()); // get the pressed number
					if (pressedNumber == this.getLastRandNum()) { // check if the pressed number is equal to the last
																	// generated number

						num.setStyle("-fx-text-fill: #ff0000"); // red String id
						int indexI = hash.get(pressedNumber)[0]; // save the index of row for that pressed number
						int indexJ = hash.get(pressedNumber)[1]; // same^ but saving the column
						pressed[indexI][indexJ] = 1; // 1 is like a true flag means that the player has pressed a random

					}

					// if the pressed number isn't equal to the last generated number, then do
					// nothing (means that the button
					// remains in the same color - black

				});

			}

		}

	}

	/**
	 * display the winner
	 * 
	 * @return
	 */
	public void displayWin() {
		Alert alert = new Alert(AlertType.INFORMATION, "Bingo! Player " + (this.playerID + 1) + " has won!",
				ButtonType.OK);
		alert.setHeaderText("Congratulations");
		alert.show();
		this.stage.hide();
	}

	@Override
	public void run() {
		// what to do when 'bingo' has been pressed from a player?
		this.bingo.setOnAction(e1 -> {

			// scan if player got bingo (checking for rows)
			for (int m = 0; m < cardSize; m++) {
				this.count = 0;
				for (int n = 0; n < cardSize; n++) {
					if (pressed[m][n] == 1) {
						this.count++;
					}
				}
				if (this.count == cardSize) {
					displayWin();
				}

			}

			// scan if the player got bingo (checking for columns)

			for (int m = 0; m < cardSize; m++) {
				this.count = 0;
				for (int n = 0; n < cardSize; n++) {
					if (pressed[n][m] == 1) {
						this.count++;
					}
				}
				if (this.count == cardSize) {
					displayWin();
				}

			}

			// scan if the player got bingo (checking for main diagonal)
			this.count = 0;
			for (int m = 0; m < cardSize; m++) {
				for (int n = 0; n < cardSize; n++) {
					if (m == n)
						if (pressed[m][n] == 1)
							this.count++;

				}

			}
			if (this.count == this.cardSize) {
				displayWin();
			}

			// scan if the player got bingo (checking for secondary diagonal)
			this.count = 0;
			for (int m = this.cardSize - 1; m >= 0; m--) {
				for (int n = 0; n < this.cardSize; n++) {
					if (m == this.cardSize - n - 1 && pressed[m][n] == 1) {
						this.count++;
					}
				}

			}
			if (this.count == this.cardSize) {
				displayWin();
			}

		});

	}

	/**
	 * 
	 * @return Card
	 */
	public GridPane getCard() {

		return Card;

	}

	/**
	 * 
	 * @return root
	 */
	public VBox getRoot() {
		return root;
	}

	/**
	 * 
	 * @param Card
	 */
	public void setCard(GridPane Card) {

		this.Card = Card;

	}

	/**
	 * 
	 * @param root
	 */
	public void setRoot(VBox root) {
		this.root = root;
	}

	/**
	 * 
	 * @return playerID
	 */
	public int getPlayerID() {
		return playerID;
	}

	/**
	 * 
	 * @param playerID
	 */
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}

	/**
	 * 
	 * @return count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * 
	 * @param count
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * 
	 * @return cardSize
	 */
	public int getCardSize() {
		return cardSize;
	}

	/**
	 * 
	 * @param cardSize
	 */
	public void setCardSize(int cardSize) {
		this.cardSize = cardSize;
	}

	/**
	 * 
	 * @return bingo
	 */
	public Button getBingo() {
		return bingo;
	}

	/**
	 * 
	 * @param bingo
	 */
	public void setBingo(Button bingo) {
		this.bingo = bingo;
	}

	/**
	 * 
	 * @return vbox
	 */
	public VBox getVbox() {
		return vbox;
	}

	/**
	 * 
	 * @param vbox
	 */
	public void setVbox(VBox vbox) {
		this.vbox = vbox;
	}

	/**
	 * 
	 * @return stage
	 */
	public Stage getStage() {
		return stage;
	}

	/**
	 * 
	 * @param stage
	 */
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	/**
	 * 
	 * @return playerNum
	 */
	public Label getPlayerNum() {
		return playerNum;
	}

	/**
	 * 
	 * @param playerNum
	 */
	public void setPlayerNum(Label playerNum) {
		this.playerNum = playerNum;
	}

	/**
	 * 
	 * @return hash
	 */
	public HashMap<Integer, int[]> getHash() {
		return hash;
	}

	/**
	 * 
	 * @param hash
	 */
	public void setHash(HashMap<Integer, int[]> hash) {
		this.hash = hash;
	}

	/**
	 * 
	 * @return myNumbers
	 */
	public ArrayList<Integer> getMyNumbers() {
		return myNumbers;
	}

	/**
	 * 
	 * @param myNumbers
	 */
	public void setMyNumbers(ArrayList<Integer> myNumbers) {
		this.myNumbers = myNumbers;
	}

	/**
	 * 
	 * @return pressed
	 */
	public int[][] getPressed() {
		return pressed;
	}

	/**
	 * 
	 * @return lastRandNum
	 */
	public int getLastRandNum() {
		return lastRandNum;
	}

	/**
	 * 
	 * @param lastRandNum
	 */
	public void setLastRandNum(int lastRandNum) {
		this.lastRandNum = lastRandNum;
	}

	/**
	 * 
	 * @param pressed
	 */
	public void setPressed(int[][] pressed) {
		this.pressed = pressed;
	}

}
