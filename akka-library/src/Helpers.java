import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class Helpers {
    public static String DATABASE_PATH = "database\\";
    public static String BOOKS_PATH = DATABASE_PATH + "books\\";
    public static String db1 = "db1";
    public static String db2 = "db2";
    public static String SERVER_CONF = "conf/server.conf";
    public static String CLIENT_CONF = "conf/client.conf";

    public static ActorRef createLocalActor(String name, Class c){
        ActorSystem system = ActorSystem.create("local_system");
        return system.actorOf(Props.create(c), name);
    }
}
