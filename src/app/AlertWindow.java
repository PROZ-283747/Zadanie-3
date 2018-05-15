/*
 *	@autor Adela Jaworowska / adela.jaworowska@gmail.com
 */

package app;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertWindow {

	public void winner(String winnerSign) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Info");
		alert.setHeaderText("Winner :) ");
		alert.setContentText("Winner: " + winnerSign + ". Congratulations! ");
		alert.showAndWait();
	}

	public void looser(String looserSign) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Info");
		alert.setHeaderText("Looser :( ");
		alert.setContentText(looserSign + " lost. Good luck next time! ");
		alert.showAndWait();
	}

	public void draw() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Info");
		alert.setHeaderText("Draw");
		alert.setContentText("It is a draw! ");
		alert.showAndWait();
	}
}
