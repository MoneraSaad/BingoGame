
package application;

/**
 * author Monera 205807308 & Reem 313227928
 */
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import java.util.List;

public class Main extends Application {

	/**
	 * Variables Deceleration
	 */

	private int playersNum;
	private int cardSize;
	private Alert alert;

	@Override
	/**
	 * @param Stage
	 */
	public void start(Stage Stage) {
		Parameters params = getParameters();
		List<String> list = params.getRaw(); // save the input parameters in a list
		playersNum = Integer.parseInt(list.get(0));
		cardSize = Integer.parseInt(list.get(1));
		if (this.checkInput()) {
			Game game = new Game(Stage, playersNum, cardSize); // create a game
			Scene scene = new Scene(game.getBorder(), 1500, 600);
			game.getBigGrid().prefWrapLengthProperty().bind(scene.widthProperty().subtract(50));
			Stage.setTitle("BingoGame");
			Stage.setScene(scene);
			Stage.show(); // show bingo game
		}

	}

	/**
	 * 
	 * @return true or false
	 */
	public boolean checkInput() {
		// if the players number is less than 2 or bigger than 12, then show warning
		// alert
		if (playersNum < 2 || playersNum > 12) {
			alert = new Alert(AlertType.WARNING, "Re-try again by picking a number from 2-12", ButtonType.OK);
			alert.setHeaderText("Illegal players number input");
			alert.show();
			return false;
		}
		// if the card size is less than 5 or bigger than 7, then show warning alert
		if (cardSize < 5 || cardSize > 7) {
			alert = new Alert(AlertType.WARNING, "Re-try again by picking a number from 5-7", ButtonType.OK);
			alert.setHeaderText("Illegal card size input");
			alert.show();
			return false;
		}

		return true;

	}

	/**
	 * 
	 * @param args
	 */

	public static void main(String[] args) {

		launch(args);

	}

}
