import akka.actor.AbstractActor;

public class BorrowBookActor extends AbstractActor {
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, s -> {
                    borrowBook(s);
                    getSender().tell(LibraryOperation.RESULT.toString() + ":\nBook borrowed\n", getSelf());
                    getContext().stop(getSelf());
                })
                .build();
    }

    private void borrowBook(String title) {
        DbOperations dbOperations1 = new DbOperations();
        DbOperations dbOperations2 = new DbOperations();
        dbOperations1.createLoadDB(Helpers.db1);
        dbOperations2.createLoadDB(Helpers.db2);
        if (dbOperations1.checkIfDbExists() && dbOperations1.checkIfAvailable(title)) {
            dbOperations1.borrowBook(title);
        }
        if (dbOperations2.checkIfDbExists() && dbOperations2.checkIfAvailable(title)) {
            dbOperations2.borrowBook(title);
        }
    }
}
