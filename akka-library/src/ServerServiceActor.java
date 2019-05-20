import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class ServerServiceActor extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, s -> {
                    if (s.contains(LibraryOperation.BORROW.toString())) {
                        String title = this.getTitle(s);
                        if (checkIfCanBeBorrowed(title)) {
                            ActorRef borrowActor = Helpers.createLocalActor("borrow_book_actor", BorrowBookActor.class);
                            ActorRef addOrderActor = Helpers.createLocalActor("add_order_actor", AddOrderActor.class);
                            borrowActor.tell(title, getSender());
                            addOrderActor.tell(title, getSender());
                        } else {
                            getSender().tell(LibraryOperation.RESULT + ":\n There is no such book or it is not available.", getSelf());
                        }
                    } else if (s.contains(LibraryOperation.RETURN.toString())) {
                        String title = this.getTitle(s);
                        ActorRef returnActor = Helpers.createLocalActor("return_book_actor", ReturnBookActor.class);
                        ActorRef removeOrderActor = Helpers.createLocalActor("remove_order_actor", RemoveOrderActor.class);
                        returnActor.tell(title, getSender());
                        removeOrderActor.tell(title, getSender());
                    } else if (s.contains(LibraryOperation.SEARCH.toString())) {
                        String result = searchForBook(getTitle(s));
                        if (!result.equals("")) {
                            String resultToSend = LibraryOperation.RESULT.toString() + ": \n" +
                                    "Title: " + result.split(" ")[0] + "\n" +
                                    "Price: " + result.split(" ")[1] + "\n" +
                                    "Status: " + result.split(" ")[2] + "\n";
                            getSender().tell(resultToSend, getSelf());
                        } else if (s.contains(LibraryOperation.READ.toString())) {
//                            TODO
                        } else {
                            getSender().tell(LibraryOperation.RESULT.toString() + ": \nThere is no such book or it is not available.\n", getSelf());
                        }
                    } else {
                        getSender().tell(LibraryOperation.RESULT + ":\nCannot understand operation", getSelf());
                    }
                    getContext().stop(this.getSelf());
                })
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }

    private String getTitle(String message) {
        return message.split("##")[1];
    }

    private String searchForBook(String title) {
        DbOperations dbOperations1 = new DbOperations();
        DbOperations dbOperations2 = new DbOperations();
        dbOperations1.createLoadDB(Helpers.db1);
        dbOperations2.createLoadDB(Helpers.db2);
        if (dbOperations1.checkIfDbExists() && dbOperations1.checkIfAvailable(title)) {
            return dbOperations1.searchForTitle(title);
        } else if (dbOperations2.checkIfDbExists() && dbOperations2.checkIfAvailable(title)) {
            return dbOperations2.searchForTitle(title);
        }
        return "";
    }

    private boolean checkIfCanBeBorrowed(String title) {
        DbOperations dbOperations1 = new DbOperations();
        DbOperations dbOperations2 = new DbOperations();
        dbOperations1.createLoadDB(Helpers.db1);
        dbOperations2.createLoadDB(Helpers.db2);
        if (dbOperations1.checkIfDbExists() && dbOperations1.checkIfAvailable(title)) {
            return true;
        } else return dbOperations2.checkIfDbExists() && dbOperations2.checkIfAvailable(title);
    }
}
