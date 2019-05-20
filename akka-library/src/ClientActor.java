import akka.actor.AbstractActor;

public class ClientActor extends AbstractActor {

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, s -> {
                    if (s.contains(LibraryOperation.RESULT.toString())){
                        System.out.println("============================================");
                        System.out.println("\n" + s);
                        System.out.println("============================================");
                    } else {
                        getContext().actorSelection("akka.tcp://remote_system@127.0.0.1:2552/user/remote").tell(s, getSelf());
                    }
                })
                .build();

    }
}
