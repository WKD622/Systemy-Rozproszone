import com.rabbitmq.client.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;

public class Technician {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println(Consts.TECHNICIAN + "\n");
        System.out.println("Type technician's name:");
        String technicianName = br.readLine();

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(Consts.HOST);
        Connection connection = factory.newConnection();
        Channel channel1 = connection.createChannel();
        channel1.basicQos(1);

        System.out.println("Type 1st specialization:");
        String specialization1 = br.readLine();
        System.out.println("Type 2nd specialization:");
        String specialization2 = br.readLine();
        if ((specialization1.equals(Consts.ELBOW_TECHNICIAN_QUEUE) || specialization1.equals(Consts.HIP_TECHNICIAN_QUEUE) || specialization1.equals(Consts.KNEE_TECHNICIAN_QUEUE))
                && (specialization2.equals(Consts.ELBOW_TECHNICIAN_QUEUE) || specialization2.equals(Consts.HIP_TECHNICIAN_QUEUE) || specialization2.equals(Consts.KNEE_TECHNICIAN_QUEUE))) {
            channel1.queueBind(specialization1, Consts.TOPIC_EXCHANGE, "*." + specialization1);
            channel1.queueBind(specialization2, Consts.TOPIC_EXCHANGE, "*." + specialization2);
            channel1.queueDeclare(technicianName + "_info", false, false, false, null);
            channel1.queueBind(technicianName + "_info", Consts.FANOUT_EXCHANGE, "admin_message");
        } else {
            System.exit(9);
        }


        System.out.println("Waiting on data to analyse...");

        Consumer consumer1 = new DefaultConsumer(channel1) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String analysis = new String(body, Consts.CHARSET);
                System.out.println("Received: " + analysis);
                String replyTo = properties.getReplyTo();
                String message = new String(body, Consts.CHARSET);
                String[] parts = message.split(" ");
                String response = "results for " + parts[0] + "'s - " + parts[1] + " are done.";
                channel1.basicPublish(Consts.TOPIC_EXCHANGE, replyTo + "." + parts[1] + ".result", new AMQP.BasicProperties.Builder().replyTo(technicianName).build(), response.getBytes(Consts.CHARSET));
            }
        };

        channel1.basicConsume(specialization1, true, consumer1);

        Consumer consumer2 = new DefaultConsumer(channel1) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String analysis = new String(body, Consts.CHARSET);
                System.out.println("Received: " + analysis);
            }
        };

        channel1.basicConsume(specialization2, true, consumer2);

        Consumer consumer3 = new DefaultConsumer(channel1) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, Consts.CHARSET);
                System.out.println("Admin message: " + message);
            }
        };

        channel1.basicConsume(technicianName + "_info", true, consumer3);
    }
}
