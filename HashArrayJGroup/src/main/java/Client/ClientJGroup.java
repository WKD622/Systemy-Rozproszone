package Client;

import Map.DistributedMap;
import Message1.Types;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.protocols.*;
import org.jgroups.protocols.pbcast.*;
import org.jgroups.stack.ProtocolStack;
import org.jgroups.util.Util;

import java.io.*;
import java.util.Arrays;


public class ClientJGroup extends ReceiverAdapter {
    private DistributedMap distributedMap;
    private JChannel channel;
    private String user_name = System.getProperty("user.name", "n/a");


    public static void main(String[] args) throws Exception {
        new ClientJGroup().start();
    }


    private void start() throws Exception {
        distributedMap = new DistributedMap();
        configureCommunication();
        eventLoop();
        channel.close();
    }


    private void configureCommunication() {
        this.channel = null;
        try {
            channel = new JChannel();
        } catch (Exception e) {
            e.printStackTrace();
        }
        channel.setReceiver(this);
        ProtocolStack stack = new ProtocolStack();
        channel.setProtocolStack(stack);
        stack.addProtocol(new UDP())
                .addProtocol(new PING())
                .addProtocol(new MERGE3())
                .addProtocol(new FD_SOCK())
                .addProtocol(new FD_ALL()
                        .setValue("timeout", 12000)
                        .setValue("interval", 3000))
                .addProtocol(new VERIFY_SUSPECT())
                .addProtocol(new BARRIER())
                .addProtocol(new NAKACK2())
                .addProtocol(new UNICAST3())
                .addProtocol(new STABLE())
                .addProtocol(new GMS())
                .addProtocol(new UFC())
                .addProtocol(new MFC())
                .addProtocol(new FRAG2())
                .addProtocol(new SEQUENCER())
                .addProtocol(new FLUSH())
                .addProtocol(new STATE_TRANSFER());
        try {
            stack.init();
            channel.connect("hashmap");
            channel.getState(null, 10000);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void eventLoop() {

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            try {
                System.out.print("> ");
                System.out.flush();
                String line = in.readLine();
                if (line.startsWith("quit") || line.startsWith("exit"))
                    break;
                line = "[" + this.user_name + "] " + line;
                String[] toSend = line.split("\\s");
                String message_type = toSend[1];
                if (Types.GET.name().equals(message_type)) {
                    System.out.println(distributedMap.get(toSend[2]));
                } else if (Types.CONTAINS_KEY.name().equals(message_type)) {
                    System.out.println(distributedMap.containsKey(toSend[2]));
                } else if (Types.REMOVE.name().equals(message_type)) {
                    Message msg = new Message(null, null, line);
                    channel.send(msg);
                } else if (Types.PRINT.name().equals(message_type)) {
                    Message msg = new Message(null, null, line);
                    channel.send(msg);
                } else if (Types.PUT.name().equals(message_type)) {
                    Message msg = new Message(null, null, line);
                    channel.send(msg);
                } else {
                    System.out.println("Wrong input");
                }
            } catch (Exception ignored) {
            }
        }
    }

    public void viewAccepted(View new_view) {
        System.out.println("** view: " + new_view + "\n>");
    }


    public void receive(Message msg) {
        String[] recv;
        String line = msg.getSrc() + ": " + msg.getObject();
        recv = line.split("\\s");
        String message_type = recv[2];
        if (Types.PUT.name().equals(message_type)) {
            synchronized (distributedMap) {
                distributedMap.put(recv[4], Integer.parseInt(recv[3]));
            }
        } else if (Types.PRINT.name().equals(message_type)) {
            System.out.println(this.distributedMap.toString());
        } else if (Types.REMOVE.name().equals(message_type)) {
            synchronized (distributedMap) {
                distributedMap.remove(recv[3]);
            }
        }
    }


    public void getState(OutputStream output) throws Exception {
        synchronized (distributedMap) {
            Util.objectToStream(distributedMap, new DataOutputStream(output));
        }
    }

    public void setState(InputStream input) throws Exception {
        DistributedMap distributedMap = (DistributedMap) Util.objectFromStream(new DataInputStream(input));
        synchronized (this.distributedMap) {
            this.distributedMap.clear();
            this.distributedMap.addAll(distributedMap);
        }
        System.out.println(distributedMap);
    }
}
