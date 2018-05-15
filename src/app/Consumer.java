/*
 *	@autor Adela Jaworowska / adela.jaworowska@gmail.com
 */

package app;

import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Queue;

public class Consumer {

	JMSContext jmsContext;
	JMSConsumer jmsConsumer;

	public Consumer() {
	}

	public void receiveQueueMessagesAsynch(app.GameController.QueueAsynchConsumer asynchConsumer, String playerId) {
		System.out.println("CONSUMER");
		ConnectionFactory connectionFactory = new com.sun.messaging.ConnectionFactory();
		jmsContext = connectionFactory.createContext();
		try {
			Queue queue = new com.sun.messaging.Queue("ATJQueue");
			jmsConsumer = jmsContext.createConsumer(queue, "PLAYERID <> '" + playerId + "'");
			jmsConsumer.setMessageListener(asynchConsumer);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		jmsConsumer.close();
		jmsContext.close();
	}
}
