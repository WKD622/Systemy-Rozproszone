import java.util.concurrent.Semaphore;

public class DBadministrator {
    public static Semaphore db1 = new Semaphore(1);
    public static Semaphore db2 = new Semaphore(1);
}
