package my_project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

public class MessageManager extends Thread {
    private Channel channel1;
    private Channel channel2;
    private String exchange1;
    private String exchange2;
    private String name;

    MessageManager(String name, Channel channel1, Channel channel2, String exchange1, String exchange2) {
        this.channel1 = channel1;
        this.channel2 = channel2;
        this.exchange1 = exchange1;
        this.exchange2 = exchange2;
        this.name = name;
    }


    @Override
    public void run() {

        Receiver receiver1 = new Receiver();
        Receiver receiver2 = new Receiver();
        receiver1.start();
        receiver2.start();

        while (true) {
            String partOfBody = "";
            System.out.println("Type part of body you want analysis on: ");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            try {
                partOfBody = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (partOfBody.matches(channel1.toString())) {
                String adressee = readAdressee();
                try {
                    channel1.basicPublish(this.exchange1, new AMQP.BasicProperties.Builder().replyTo(this.name).toString(), null, adressee.getBytes(StandardCharsets.UTF_8));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (partOfBody.matches(channel2.toString())) {
                String adressee = readAdressee();
                try {
                    channel2.basicPublish(this.exchange2, new AMQP.BasicProperties.Builder().replyTo(this.name).toString(), null, adressee.getBytes(StandardCharsets.UTF_8));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("I cant't help you");
            }
        }
    }

    private String readAdressee() {
        String adressee = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Type adressee:");
        try {
            adressee = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return adressee;
    }
}
