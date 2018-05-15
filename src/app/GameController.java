/*
 *	@autor Adela Jaworowska / adela.jaworowska@gmail.com
 */

package app;

import java.util.Date;
import java.util.UUID;

import javax.jms.Message;
import javax.jms.MessageListener;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class GameController {

	private String playerId;
	private GameModel gameModel;
	private Producer producer;
	private Consumer consumer;
	private AlertWindow alertWindow;
	private String state; // "waitForOpponent" / "myMove" / "opponentMove" / "won" / "lost" / "draw"
	private String thisPlayerSign;
	private String opponentSign;
	private long startTime;

	@FXML
	Text playerSignField;
	@FXML
	Text turnStm;

	@FXML
	Button btn00;
	@FXML
	Button btn01;
	@FXML
	Button btn02;
	@FXML
	Button btn10;
	@FXML
	Button btn11;
	@FXML
	Button btn12;
	@FXML
	Button btn20;
	@FXML
	Button btn21;
	@FXML
	Button btn22;
	@FXML
	Button resetBtn;

	// [00][10][20]
	// [01][11][21]
	// [02][12][22]
	private Button buttons[][];
	private int boardSize;

	public GameController() {
	}

	@FXML
	private void initialize() {
		System.out.println("INITIALIZE");

		playerId = (UUID.randomUUID()).toString();
		gameModel = new GameModel(3);
		state = "waitForOpponent";
		thisPlayerSign = "X";
		opponentSign = "O";
		startTime = new Date().getTime();
		boardSize = 3;

		alertWindow = new AlertWindow();
		producer = new Producer(playerId);
		consumer = new Consumer();
		consumer.receiveQueueMessagesAsynch(new QueueAsynchConsumer(), playerId);
		sendWelcomeMessage();

		buttons = new Button[3][3];
		buttons[0][0] = btn00;
		buttons[0][1] = btn01;
		buttons[0][2] = btn02;
		buttons[1][0] = btn10;
		buttons[1][1] = btn11;
		buttons[1][2] = btn12;
		buttons[2][0] = btn20;
		buttons[2][1] = btn21;
		buttons[2][2] = btn22;
	}

	@FXML
	private void btn00_Click() {
		buttonClick(0, 0);
	}

	@FXML
	private void btn01_Click() {
		buttonClick(0, 1);
	}

	@FXML
	private void btn02_Click() {
		buttonClick(0, 2);
	}

	@FXML
	private void btn10_Click() {
		buttonClick(1, 0);
	}

	@FXML
	private void btn11_Click() {
		buttonClick(1, 1);
	}

	@FXML
	private void btn12_Click() {
		buttonClick(1, 2);
	}

	@FXML
	private void btn20_Click() {
		buttonClick(2, 0);
	}

	@FXML
	private void btn21_Click() {
		buttonClick(2, 1);
	}

	@FXML
	private void btn22_Click() {
		buttonClick(2, 2);
	}

	@FXML
	private void resetBtn_Click() {
		resetBoard();
		sendResetMessage();
	}

	private void buttonClick(int x, int y) {
		if (turnStm.getText().equals("Waiting for opponent..."))
			return;
		if (state.equals("opponentMove"))
			return;
		if (isButtonSet(buttons[x][y]))
			return;
		else {
			setButton(thisPlayerSign, buttons[x][y]);
			gameModel.setFieldOnBoard(thisPlayerSign, x, y);
			sendMyMove(x, y);
			afterCheckGame();
			setOpponentMove();
		}
	}

	private boolean isButtonSet(Button btn) {
		if (btn.getText().equals(""))
			return false;
		else
			return true;
	}

	private void setButton(String playerSign, Button btn) {
		btn.setText(playerSign);
		btn.setDisable(true);
		gameModel.incrementMoveCount();
	}

	private void drawOpponentMove(int x, int y) {
		System.out.println("DRAW_OPPONENT_MOVE");
		setButton(opponentSign, buttons[x][y]);
		gameModel.setFieldOnBoard(opponentSign, x, y);
	}

	private void afterMessage(String text, String senderId, int x, int y, long opponentTimeStamp) {
		System.out.println("AFTER MESSAGE");

		if (state.equals("waitForOpponent") && text.equals("START")) {
			if (startTime < opponentTimeStamp) {
				setThisPlayerMove();
				playerSignField.setText("X");
			} else {
				thisPlayerSign = "O";
				opponentSign = "X";
				setOpponentMove();
				playerSignField.setText("O");
			}
		}
		if (state.equals("opponentMove") && text.equals("COORDINATES")) {
			drawOpponentMove(x, y);
			afterCheckGame();
			setThisPlayerMove();
		}
		if (text.equals("RESET")) {
			resetResponse();
		}
		if (text.equals("CLOSE")) {
			afterOpponentClose();
		}
	}

	private void afterCheckGame() {
		String result = gameModel.checkGame();
		System.out.println("Check " + thisPlayerSign + ": " + result);
		if (result.equals(thisPlayerSign)) {
			state = "won";
			alertWindow.winner(result);
			disableButtons(true);
		}
		if (result.equals(opponentSign)) {
			state = "lost";
			alertWindow.looser(result);
			disableButtons(true);
		}
		if (result.equals("draw")) {
			state = "draw";
			alertWindow.draw();
			disableButtons(true);
		}
		if (result.equals("continue")) {
			return;
		}
	}

	private void afterOpponentClose() {
		clearButtons();
		disableButtons(false);
		state = "waitForOpponent";
		turnStm.setText("Waiting for opponent...");
		gameModel.resetGameModel();
		sendWelcomeMessage();
	}

	private void setOpponentMove() {
		state = "opponentMove";
		turnStm.setText("Your opponent's turn...");
	}

	private void setThisPlayerMove() {
		state = "myMove";
		turnStm.setText("Your turn...");
	}

	// reseting board after clicking ResetButton
	private void resetBoard() {
		clearButtons();
		disableButtons(false);
		gameModel.resetGameModel();
		sendResetMessage();
		sendWelcomeMessage();
	}

	// response for the event in which opponent clicks on ResetButton
	private void resetResponse() {
		clearButtons();
		disableButtons(false);
		gameModel.resetGameModel();
		sendWelcomeMessage();
	}

	private void clearButtons() {
		for (int i = 0; i < boardSize; ++i) {
			for (int j = 0; j < boardSize; ++j) {
				buttons[i][j].setText("");
			}
		}
	}

	private void disableButtons(boolean a) {
		for (int i = 0; i < boardSize; ++i) {
			for (int j = 0; j < boardSize; ++j) {
				buttons[i][j].setDisable(a);
			}
		}
	}

	private void sendWelcomeMessage() {
		System.out.println("START");
		producer.sendQueueMessages("START", 0, 0);
	}

	private void sendResetMessage() {
		System.out.println("RESET");
		producer.sendQueueMessages("RESET", 0, 0);
	}

	private void sendMyMove(int x, int y) {
		producer.sendQueueMessages("COORDINATES", x, y);
	}

	private void sendCloseMessage() {
		producer.sendQueueMessages("CLOSE", 0, 0);
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public void closeProgram() {
		ConsumerCleaner consumerCleaner = new ConsumerCleaner();
		consumerCleaner.receiveQueueMessages();
		// sendCloseMessage();
		consumer.close();
	}

	public class QueueAsynchConsumer implements MessageListener {
		@Override
		public void onMessage(Message message) {
			System.out.println("ON_MESSAGE");
			if (message.equals(null))
				return;
			else {

				Platform.runLater(() -> {
					try {
						afterMessage(message.getStringProperty("INFO"), message.getStringProperty("PLAYERID"),
								message.getIntProperty("COORDINATE_X"), message.getIntProperty("COORDINATE_Y"),
								message.getLongProperty("TIMESTAMP"));
						System.out.println("Odebrano wiadomoœæ: " + message.getStringProperty("INFO") + " ");
					} catch (javax.jms.JMSException e) {
						e.printStackTrace();
					}
				});
			}
		}
	}
}
