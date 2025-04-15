package com.bankapp.blockchainnode;

public class SavingsAccount extends Account {
    public SavingsAccount(String accNumber, double balance) {
        super(accNumber, balance, 0, 1000); // No overdraft, $1000 daily withdrawal limit
    }
}
