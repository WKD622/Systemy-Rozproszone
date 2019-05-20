public class DbFiller {

    public static void main(String[] args) {
        DbOperations dbOperations1 = new DbOperations();
        DbOperations dbOperations2 = new DbOperations();
        dbOperations1.createLoadDB(Helpers.db1);
        dbOperations2.createLoadDB(Helpers.db2);
        dbOperations1.addRecord("Calineczka", 20);
        dbOperations1.addRecord("Imie rozy", 30);
        dbOperations1.addRecord("Twoja stara", 60);
        dbOperations2.addRecord("1Q84", 25);
        dbOperations2.addRecord("Winnetou", 20);
        dbOperations2.addRecord("Marsjanin", 40);
    }
}
