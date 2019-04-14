package my_project;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;

public class Doctor {
    public static void main(String[] args) throws IOException, TimeoutException {
        System.out.println("DOCTOR\nType doctor's name:");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String name = br.readLine();

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();

        System.out.println("Write doctors specializations, (for example: \"elbow knee\"):");
        String specializationsForParsing = br.readLine();
        Specializations specializations = new Specializations(specializationsForParsing);
        if (specializations.areInitialized()) {
            if (specializations.isElbow() && specializations.isHip()) {
                Channel hip = connection.createChannel();
                Channel elbow = connection.createChannel();
                hip.exchangeDeclare(Specializations.HIP_STR, BuiltinExchangeType.FANOUT);
                elbow.exchangeDeclare(Specializations.ELBOW_STR, BuiltinExchangeType.FANOUT);
                MessageManager messageManager = new MessageManager(name, hip, elbow, Specializations.HIP_STR, Specializations.ELBOW_STR);
                messageManager.start();
            } else if (specializations.isElbow() && specializations.isKnee()) {
                Channel elbow = connection.createChannel();
                Channel knee = connection.createChannel();
                elbow.exchangeDeclare(Specializations.ELBOW_STR, BuiltinExchangeType.FANOUT);
                knee.exchangeDeclare(Specializations.KNEE_STR, BuiltinExchangeType.FANOUT);
                MessageManager messageManager = new MessageManager(name, knee, elbow, Specializations.KNEE_STR, Specializations.ELBOW_STR);
                messageManager.start();
            } else if (specializations.isHip() && specializations.isKnee()) {
                Channel knee = connection.createChannel();
                Channel hip = connection.createChannel();
                hip.exchangeDeclare(Specializations.HIP_STR, BuiltinExchangeType.FANOUT);
                knee.exchangeDeclare(Specializations.KNEE_STR, BuiltinExchangeType.FANOUT);
                MessageManager messageManager = new MessageManager(name, knee, hip, Specializations.KNEE_STR, Specializations.HIP_STR);
                messageManager.start();
            }
        }
    }
}
