import akka.actor.AbstractActor;
import akka.actor.ActorRef;

public class ServerActor extends AbstractActor {

    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(String.class, s -> {
                    if (s.equals("hi")) {
                        System.out.println("STATUS: Server working");
                    } else {
                        ActorRef actor = Helpers.createLocalActor("client_service", ServerServiceActor.class);
                        actor.tell(s, getSender());
                    }
                })
                .build();
    }
}

