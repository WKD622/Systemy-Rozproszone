package server;

import com.zeroc.Ice.Current;
import generated.AccountType;
import generated.Bank;
import generated.Client;
import pom.MessagesTypes;

import java.util.HashMap;
import java.util.Map;

import static com.zeroc.Ice.Util.stringToIdentity;

public class BankI implements Bank {
    private Map<Client, AccountI> clients = new HashMap<>();
    private Map<AccountI, String> accounts = new HashMap<>();

    @Override
    public String showClients(Current current) {
        StringBuilder clientsStr = new StringBuilder();
        for (Client client : clients.keySet()) {
            clientsStr.append(client.firstName).append(" ").append(client.lastName).append("\n");
        }
        return clientsStr.toString();
    }

    @Override
    public String takeLoan(long value, int months, String currency, Current current) {
        double cost = 1.05;
        String pesel = current.ctx.get("pesel");
        for (AccountI accountI : accounts.keySet()) {
            if (accountI.getPesel(current).equals(pesel) && accountI.getAccountType(current).equals(AccountType.Premium.toString())) {
                StringBuilder info = new StringBuilder();
                accountI.addIncome(value, current);
                return info.append("monthly installment: ").append((value * cost) / months).toString();
            }
        }
        return MessagesTypes.ERROR;
    }

    @Override
    public boolean checkIfPasswordCorrect(Current current) {
        String password = current.ctx.get("password");
        String pesel = current.ctx.get("pesel");
        for (AccountI accountI : accounts.keySet()) {
            if (accountI.getPesel(current).equals(pesel)) {
                return accounts.get(accountI).equals(password);
            }
        }
        return false;
    }

    @Override
    public String getAccountType(Current current) {
        String pesel = current.ctx.get("pesel");
        for (Client client : this.clients.keySet()) {
            if (client.pesel.equals(pesel)) {
                return clients.get(client).getAccountType(current);
            }
        }
        return MessagesTypes.ERROR;
    }

    @Override
    public String newAccount(String firstName, String lastName, String pesel, long monthlyIncome, Current current) {
        if (pesel.matches("[0-9]+") && pesel.length() == 11) {
            String password = new RandomString(10).nextString();
            Client createdClient = new Client(firstName, lastName, pesel, password);
            AccountI newAccount = new AccountI(0, monthlyIncome, pesel);
            current.adapter.add(newAccount, stringToIdentity(pesel + newAccount.getAccountType(current)));
            current.adapter.activate();
            accounts.put(newAccount, password);
            clients.put(createdClient, newAccount);
            return MessagesTypes.LOGED_IN + " " + password + " " + newAccount.getAccountType(current);
        }
        return MessagesTypes.ERROR;
    }
}