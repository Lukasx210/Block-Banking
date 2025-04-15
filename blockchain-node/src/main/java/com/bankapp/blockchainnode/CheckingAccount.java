package com.bankapp.blockchainnode;

public class CheckingAccount extends Account {
    public CheckingAccount(String accNumber, double balance) {
        super(accNumber, balance, 0, Double.MAX_VALUE); // No overdraft, unlimited withdrawals
    }
}