package my_project;

import java.util.Scanner;

public class Message {
    private String to;
    private String on;

    public String getTo() {
        return to;
    }

    public String getOn() {
        return on;
    }

    Message(String message) {
        Scanner sc = new Scanner(message);
        if (sc.nextLine().matches("^knee\\s[A-Za-z]+")){

        }
    }


}
