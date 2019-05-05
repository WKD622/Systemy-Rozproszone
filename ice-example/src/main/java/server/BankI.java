package server;

import com.zeroc.Ice.Current;
import generated.AccountType;
import generated.Bank;
import generated.Client;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import pom.MessagesTypes;
import proto_gen.CurrencyType;
import proto_gen.ExchangeRateServiceGrpc;

import java.util.HashMap;
import java.util.Map;

import static com.zeroc.Ice.Util.stringToIdentity;

public class BankI implements Bank {
    private Map<Client, AccountI> clients = new HashMap<>();
    private Map<AccountI, String> accounts = new HashMap<>();
    private final ManagedChannel channel;
    private final ExchangeRateServiceGrpc.ExchangeRateServiceBlockingStub calcBlockingStub;
    private final ExchangeRateServiceGrpc.ExchangeRateServiceStub calcNonBlockingStub;

    public BankI(String host, int port) {
        channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext(true).build();
        calcBlockingStub = proto_gen.ExchangeRateServiceGrpc.newBlockingStub(channel);
        calcNonBlockingStub = ExchangeRateServiceGrpc.newStub(channel);
    }

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
        if (currency.contains("PLN") || currency.contains("GBP") || currency.contains("EUR")) {
            String pesel = current.ctx.get("pesel");
            for (AccountI accountI : accounts.keySet()) {
                if (accountI.getPesel(current).equals(pesel) && accountI.getAccountType(current).equals(AccountType.Premium.toString())) {
                    CurrencyType currencyType;
                    if (currency.contains("GBP")) {
                        currencyType = CurrencyType.GBP;
                    } else if (currency.contains("EUR")) {
                        currencyType = CurrencyType.EUR;
                    } else {
                        currencyType = CurrencyType.PLN;
                    }
                    proto_gen.ExchangeRateOpArguments request = proto_gen.ExchangeRateOpArguments.newBuilder().setArg(currencyType).build();
                    double result = calcBlockingStub.getExchangeRate(request).next().getRes();
                    StringBuilder info = new StringBuilder();
                    accountI.addIncome((long) (value * result), current);
                    return info.append("monthly installment: ").append((value * result) / months).toString();
                }
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