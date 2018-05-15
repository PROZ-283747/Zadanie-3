/*
 *	@autor Adela Jaworowska / adela.jaworowska@gmail.com
 */

package app;

import java.util.Date;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.Message;

public class Producer {

	private String producerId;

	public Producer(String playerId) {
		this.producerId = playerId;
	}

	public void sendQueueMessages(String text, int x, int y) {
		ConnectionFactory connectionFactory = new com.sun.messaging.ConnectionFactory();
		try {
			((com.sun.messaging.ConnectionFactory) connectionFactory)
					.setProperty(com.sun.messaging.ConnectionConfiguration.imqAddressList, "localhost:7676/jms");

			JMSContext jmsContext = connectionFactory.createContext();
			JMSProducer jmsProducer = jmsContext.createProducer();
			Queue queue = new com.sun.messaging.Queue("ATJQueue");

			Message message = jmsContext.createMessage();
			message.setStringProperty("INFO", text);
			message.setIntProperty("COORDINATE_X", x);
			message.setIntProperty("COORDINATE_Y", y);
			message.setStringProperty("PLAYERID", producerId);
			message.setLongProperty("TIMESTAMP", new Date().getTime());

			jmsProducer.send(queue, message);
			System.out.println("Wiadomoœæ " + message.getStringProperty("INFO") + " zosta³a wys³ana.");
			jmsContext.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
