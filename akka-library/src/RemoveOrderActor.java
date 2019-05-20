import akka.actor.AbstractActor;

public class RemoveOrderActor extends AbstractActor {
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, s -> {
                    removeOrder(s);
                    getSender().tell(LibraryOperation.RESULT.toString() + ":\nOrder removed\n", getSelf());
                    getContext().stop(getSelf());
                })
                .build();
    }

    private void removeOrder(String title) {
        DbOperations dbOperations = new DbOperations();
        dbOperations.deleteOrder(title);
    }
}
