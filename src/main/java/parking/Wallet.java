package parking;

public class Wallet {
    private double balance;

    public Wallet() {
        this.balance = 0.0;
    }

    public Wallet(double balance) {
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public void addFunds(double amount) {
        if(amount > 0) {
            balance += amount;
        } else {
            throw new InvalidAmountException();
        }
    }

    public void deductFunds(double amount) {
        if(amount > 0) {
            if(balance >= amount) {
                balance -= amount;
            } else {
                throw new InsufficientFundsException();
            }
        } else {
            throw new InvalidAmountException();
        }
    }

    public void transferFunds(Wallet toWallet, double amount) {
        if(amount > 0) {
            if(balance >= amount) {
                this.deductFunds(amount);
                toWallet.addFunds(amount);
            } else {
                throw new InsufficientFundsException();
            }
        } else {
            throw new InvalidAmountException();
        }
    }
}

class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException() {
        super("Insufficient funds in wallet.");
    }
}

class InvalidAmountException extends RuntimeException {
    public InvalidAmountException() {
        super("Invalid amount. Amount must be positive.");
    }
}