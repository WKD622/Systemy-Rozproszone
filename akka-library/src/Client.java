import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Client {

    public static void main(String[] args) throws IOException {
        File configFile = new File(Helpers.CLIENT_CONF);
        Config config = ConfigFactory.parseFile(configFile);
        final ActorSystem system = ActorSystem.create("remote_system", config);
        final ActorRef remote = system.actorOf(Props.create(ClientActor.class), "remote");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Welcome in our library");

        while (true) {
            System.out.println("Choose operation you want to perform.\n" +
                    "Type LIST to list all operations.");
            LibraryOperation operation = parseOperation(br.readLine());
            String title = "";
            String message = "";
            if (!operation.equals(LibraryOperation.LIST) && !operation.equals(LibraryOperation.EXIT)) {
                System.out.println("Type book title:");
                title = br.readLine();
                message = createMessage(operation.toString(), title);
            }
            switch (operation) {
                case LIST:
                    listAllOperations();
                    break;
                case EXIT:
                    System.exit(0);
                default:
                    remote.tell(message, null);
            }
        }
    }

    private static void listAllOperations() {
        for (LibraryOperation libraryOperation : LibraryOperation.values()) {
            if (!libraryOperation.equals(LibraryOperation.RESULT))
                System.out.println(libraryOperation);
        }
    }

    private static String createMessage(String operation, String title) {
        return operation + "##" + title;
    }

    private static LibraryOperation parseOperation(String operation) {
        if (operation.contains(LibraryOperation.BORROW.toString())) {
            return LibraryOperation.BORROW;
        } else if (operation.contains(LibraryOperation.READ.toString())) {
            return LibraryOperation.READ;
        } else if (operation.contains(LibraryOperation.SEARCH.toString())) {
            return LibraryOperation.SEARCH;
        } else if (operation.contains(LibraryOperation.EXIT.toString())) {
            return LibraryOperation.EXIT;
        } else if (operation.contains(LibraryOperation.RETURN.toString())) {
            return LibraryOperation.RETURN;
        } else {
            return LibraryOperation.LIST;
        }
    }
}