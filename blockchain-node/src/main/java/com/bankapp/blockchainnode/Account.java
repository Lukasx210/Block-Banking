package com.bankapp.blockchainnode;

public abstract class Account {
    protected final String accountNumber;
    protected double balance;
    protected final double overdraftLimit;
    protected final double withdrawalLimit;

    public Account(String accountNumber, double balance, double overdraftLimit, double withdrawalLimit) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.overdraftLimit = overdraftLimit;
        this.withdrawalLimit = withdrawalLimit;
    }

    public boolean canWithdraw(double amount) {
        return (balance - amount) >= -overdraftLimit && amount <= withdrawalLimit;
    }

    public void withdraw(double amount) {
        if (canWithdraw(amount)) {
            balance -= amount;
        } else {
            throw new IllegalArgumentException("Insufficient funds or exceeds withdrawal limit");
        }
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }
}