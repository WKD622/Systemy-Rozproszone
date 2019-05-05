package server;

import com.zeroc.Ice.ObjectAdapter;

import static com.zeroc.Ice.Util.initialize;
import static com.zeroc.Ice.Util.stringToIdentity;

import com.zeroc.Ice.Communicator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Server {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try (Communicator communicator = initialize(args)) {
            ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("BankAdapter", "default -p 10002");
            List<String> bankNames = new ArrayList<>();
            boolean end = true;
            while (end) {
                System.out.println("If you want to add bank type its name here, if not write \"no\": ");
                String bankName = br.readLine();
                if (bankName.contains("no")) {
                    end = false;
                } else {
                    bankNames.add(bankName);
                }
            }
            for (String bankName : bankNames) {
                BankI object = new BankI("localhost",50052);
                adapter.add(object, stringToIdentity(bankName));
                adapter.activate();
                System.out.println(bankName + " opened.");
            }
            communicator.waitForShutdown();
        }
    }
}
