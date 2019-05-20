import akka.actor.AbstractActor;

public class AddOrderActor extends AbstractActor {

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, s -> {
                    addOrder(s);
                    getSender().tell(LibraryOperation.RESULT.toString() + ":\nOrder added\n", getSelf());
                    getContext().stop(getSelf());
                })
                .build();
    }

    private void addOrder(String title) {
        DbOperations dbOperations = new DbOperations();
        dbOperations.addOrder(title);
    }
}
