import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class ServerStarter {

    public static void main(String[] args) {
        File configFile = new File(Helpers.SERVER_CONF);
        Config config = ConfigFactory.parseFile(configFile);
        ActorSystem system = ActorSystem.create("remote_system", config);
        final ActorRef local = system.actorOf(Props.create(ServerActor.class), "remote");
        local.tell("hi", null);

        System.out.println("Write anything and type ENTER to kill server");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        system.terminate();

    }
}
