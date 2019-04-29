module generated
{
    struct Client {
        string firstName;
        string lastName;
        string pesel;
        string password;
    };

    enum AccountType { Premium, Standard };

    struct AccountParameters {
        long income;
        long balance;
        string pesel;
        AccountType accountType;
    };

    sequence<Client> clients;

    interface Account {
            long getBalance();

            long addIncome(long value);

            long addOutcome(long value);

            string getPesel();

            string getAccountType();
    }

    interface Bank {
        string newAccount(string firstName, string lastName, string pesel, long monthlyIncome);

        string showClients();

        string takeLoan(long value, int months, string currency);

        bool checkIfPasswordCorrect();

        string getAccountType();

    }
}