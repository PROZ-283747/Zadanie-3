/*
 *	@autor Adela Jaworowska / adela.jaworowska@gmail.com
 */

package app;

import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Topic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

public class Main extends Application {

	private GameController gameController = new GameController();

	@Override
	public void start(Stage primaryStage) {
		try {
			System.out.println("Welcome! Game for 2 players. To test it you should start program twice. ");

			FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("window.fxml"));
			AnchorPane root = fxmlLoader.load();
			gameController = fxmlLoader.getController();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Tic Tac Toe Game");
			primaryStage.setResizable(false);
			primaryStage.setOnHiding(e -> primaryStage_Hiding(e, fxmlLoader));
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void primaryStage_Hiding(WindowEvent e, FXMLLoader fxmlLoader) {

		if (e == null)
			System.out.println("e is null");
		if (fxmlLoader == null)
			System.out.println("fxmlLoader is null");
		if (fxmlLoader.getController() == null)
			System.out.println("fxmlLoader.getController() is null");
		gameController.closeProgram();
	}

	public static void main(String[] args) {
		launch(args);
	}
}