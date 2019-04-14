package my_project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Admin {
    public static void main(String[] args) throws IOException {
        System.out.println("ADMIN\nType admin's name:");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String name = br.readLine();

    }
}
