import akka.actor.AbstractActor;

public class ReturnBookActor extends AbstractActor {
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, s -> {
                    returnBook(s);
                    System.out.println("RETURNING BOOK");
                    getSender().tell(LibraryOperation.RESULT.toString() + ":\nBook returned\n", getSelf());
                    getContext().stop(getSelf());
                })
                .build();
    }

    private void returnBook(String title) {
        DbOperations dbOperations1 = new DbOperations();
        DbOperations dbOperations2 = new DbOperations();
        dbOperations1.createLoadDB(Helpers.db1);
        dbOperations2.createLoadDB(Helpers.db2);
        if (dbOperations1.checkIfDbExists() && dbOperations1.checkIfExists(title) && !dbOperations1.checkIfAvailable(title)) {
            dbOperations1.returnBook(title);
        }
        if (dbOperations2.checkIfDbExists() && dbOperations2.checkIfExists(title) && !dbOperations2.checkIfAvailable(title)) {
            dbOperations2.returnBook(title);
        }
    }
}
