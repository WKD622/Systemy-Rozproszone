import com.rabbitmq.client.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;


public class Admin {
    public static void main(String[] args) throws IOException, TimeoutException {
        System.out.println(Consts.ADMIN + "\n");

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(Consts.HOST);
        Connection connection = factory.newConnection();
        Channel channel1 = connection.createChannel();


        channel1.exchangeDeclare(Consts.TOPIC_EXCHANGE, BuiltinExchangeType.TOPIC);
        channel1.queueDeclare(Consts.HIP_TECHNICIAN_QUEUE, false, false, false, null);
        channel1.queueDeclare(Consts.ELBOW_TECHNICIAN_QUEUE, false, false, false, null);
        channel1.queueDeclare(Consts.KNEE_TECHNICIAN_QUEUE, false, false, false, null);
        channel1.queueDeclare(Consts.ADMIN_QUEUE, false, false, false, null);

        channel1.exchangeDeclare(Consts.FANOUT_EXCHANGE, BuiltinExchangeType.FANOUT);
        channel1.queueDeclare(Consts.ADMIN_MESSAGES_QUEUE, false, false, false, null);

        channel1.queueBind(Consts.ADMIN_QUEUE, Consts.TOPIC_EXCHANGE, "#");

        Consumer consumer = new DefaultConsumer(channel1) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String analysis = new String(body, Consts.CHARSET);
                System.out.println("Log: " + analysis + " | From: " + properties.getReplyTo());
            }
        };

        channel1.basicConsume(Consts.ADMIN_QUEUE, true, consumer);

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.println("Type message you want to send:");
            String message = br.readLine();
            channel1.basicPublish(Consts.FANOUT_EXCHANGE, "", null, message.getBytes(Consts.CHARSET));
        }
    }
}
