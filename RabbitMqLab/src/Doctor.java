import com.rabbitmq.client.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;

public class Doctor {
    public static void main(String args[]) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(Consts.HOST);
        Connection connection = factory.newConnection();
        Channel channel1 = connection.createChannel();
        channel1.exchangeDeclare(Consts.TOPIC_EXCHANGE, BuiltinExchangeType.TOPIC);

        System.out.println(Consts.DOCTOR + "\nType doctor's name:");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String queueName = br.readLine();

        channel1.queueDeclare(queueName, false, false, false, null);
        channel1.queueBind(queueName, Consts.TOPIC_EXCHANGE, queueName + ".*.result");

        channel1.queueDeclare(queueName + "_info", false, false, false, null);
        channel1.queueBind(queueName + "_info", Consts.FANOUT_EXCHANGE, "admin_message");

        Consumer consumer = new DefaultConsumer(channel1) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String analysis = new String(body, Consts.CHARSET);
                System.out.println("Received: " + analysis);
            }
        };

        channel1.basicConsume(queueName, true, consumer);

        Consumer consumer2 = new DefaultConsumer(channel1) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, Consts.CHARSET);
                System.out.println("Admin message: " + message);
            }
        };

        channel1.basicConsume(queueName + "_info", true, consumer2);

        while (true) {
            System.out.println("What do you want to analyse?");
            String partOfBody = br.readLine();
            System.out.println("Type patients surname");
            String patientSurname = br.readLine();
            channel1.basicPublish(Consts.TOPIC_EXCHANGE, queueName + "." + partOfBody, new AMQP.BasicProperties.Builder().replyTo(queueName).build(), (patientSurname + " " + partOfBody).getBytes(Consts.CHARSET));
        }
    }
}

