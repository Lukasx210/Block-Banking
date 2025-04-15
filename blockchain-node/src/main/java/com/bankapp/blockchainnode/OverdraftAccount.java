package com.bankapp.blockchainnode;

public class OverdraftAccount extends Account {
    public OverdraftAccount(String accNumber, double balance) {
        super(accNumber, balance, 500, Double.MAX_VALUE); // $500 overdraft, unlimited withdrawals
    }
}