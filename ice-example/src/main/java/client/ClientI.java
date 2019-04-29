package client;

import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectPrx;
import generated.AccountPrx;
import generated.AccountType;
import generated.BankPrx;
import pom.MessagesTypes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClientI {
    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try (Communicator communicator = com.zeroc.Ice.Util.initialize(args)) {
            String pesel = "", name = "", surname = "", password = "", accountType = "";
            int income = 0;
            java.util.Map<String, String> ctx = new java.util.HashMap<String, String>();

            System.out.println("Type bank's name:");
            String bankName = br.readLine();
            ObjectPrx base = communicator.stringToProxy(bankName + ":default -p 10002");
            BankPrx bank = BankPrx.checkedCast(base);
            if (bank == null) {
                throw new Error("Invalid proxy");
            }


            System.out.println("Type \"NEW\" if you want to create new account\n" +
                    "Type \"LOG_IN\" if you want to use your account");
            String message = br.readLine();

            if (message.contains(MessagesTypes.NEW)) {
                message = MessagesTypes.ERROR;
                while (message.contains(MessagesTypes.ERROR)) {
                    System.out.println("Type your name:");
                    name = br.readLine();
                    System.out.println("Type your surname:");
                    surname = br.readLine();
                    System.out.println("Type your pesel:");
                    pesel = br.readLine();
                    System.out.println("Type your monthly income:");
                    income = Integer.valueOf(br.readLine());
                    message = bank.newAccount(name, surname, pesel, income);
                    password = message.split(" ")[1];
                    accountType = message.split(" ")[2];
                    System.out.println("Your password: " + password);
                    System.out.println("Your accountType: " + accountType);
                    ctx.put("password", password);
                    ctx.put("pesel", pesel);
                }
            } else if (message.contains(MessagesTypes.LOG_IN)) {
                System.out.println("Type your pesel:");
                pesel = br.readLine();
                System.out.println("Type your password:");
                password = br.readLine();
                ctx.put("password", password);
                ctx.put("pesel", pesel);
                if (!bank.checkIfPasswordCorrect(ctx)) {
                    System.out.println("Wrong data. System exits.");
                    System.exit(2);
                }
                accountType = bank.getAccountType(ctx);
                message = MessagesTypes.LOGED_IN;
            } else {
                System.out.println("Wrong input. System exits.");
                System.exit(1);
            }


            if (message.contains(MessagesTypes.LOGED_IN) && !accountType.equals(MessagesTypes.ERROR)) {
                System.out.println("Welcome to your " + accountType + " account");
                ObjectPrx base2 = communicator.stringToProxy(pesel + accountType + ":default -p 10002");
                AccountPrx account = AccountPrx.checkedCast(base2);
                while (true) {
                    System.out.println("Type what operation you want to make, type LIST to list all operations:");
                    String operation = br.readLine();
                    if (operation.contains(MessagesTypes.LIST)) {
                        if (account.getAccountType().equals(AccountType.Premium.toString())) {
                            System.out.println(MessagesTypes.LOAN);
                        }
                        System.out.println(MessagesTypes.BALANCE + "\n"
                                + MessagesTypes.LIST + "\n"
                                + MessagesTypes.ADD_INCOME + "\n"
                                + MessagesTypes.ADD_OUTCOME + "\n"
                                + MessagesTypes.EXIT + "\n");

                    } else if (account.getAccountType().equals(AccountType.Premium.toString()) && operation.contains(MessagesTypes.LOAN)) {
                        System.out.println("Type loan value: ");
                        int value = Integer.valueOf(br.readLine());
                        System.out.println("Type number of months: ");
                        int months = Integer.valueOf(br.readLine());
                        System.out.println("Type currency:");
                        String currency = br.readLine();
                        System.out.println(bank.takeLoan(value, months, currency, ctx));
                    } else if (operation.contains(MessagesTypes.BALANCE)) {
                        System.out.println(account.getBalance());
                    } else if (operation.contains(MessagesTypes.EXIT)) {
                        System.exit(3);
                    } else if (operation.contains(MessagesTypes.ADD_INCOME)) {
                        System.out.println("Type income value: ");
                        int value = Integer.valueOf(br.readLine());
                        System.out.println("New balance: " + account.addIncome(value));
                    } else if (operation.contains(MessagesTypes.ADD_OUTCOME)) {
                        System.out.println("Type outcome value: ");
                        int value = Integer.valueOf(br.readLine());
                        System.out.println("New balance: " + account.addOutcome(value));
                    } else {
                        System.out.println("Wrong operation");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}