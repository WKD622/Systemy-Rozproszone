package server;

import com.zeroc.Ice.Current;
import generated.Account;
import generated.AccountParameters;
import generated.AccountType;

public class AccountI implements Account {
    private AccountParameters accountParameters;

    public AccountI(long balance, long income, String pesel) {
        if (income > 5000) {
            accountParameters = new AccountParameters(income, balance, pesel, AccountType.Premium);
        } else {
            accountParameters = new AccountParameters(income, balance, pesel, AccountType.Standard);
        }
    }

    @Override
    public long getBalance(Current current) {
        return accountParameters.balance;
    }

    @Override
    public long addIncome(long value, Current current) {
        this.accountParameters.balance += value;
        return this.accountParameters.balance;
    }

    @Override
    public long addOutcome(long value, Current current) {
        this.accountParameters.balance -= value;
        return this.accountParameters.balance;
    }

    @Override
    public String getPesel(Current current) {
        return this.accountParameters.pesel;
    }

    @Override
    public String getAccountType(Current current) {
        return this.accountParameters.accountType.toString();
    }
}
