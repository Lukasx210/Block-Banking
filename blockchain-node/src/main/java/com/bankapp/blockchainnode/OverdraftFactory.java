package com.bankapp.blockchainnode;

class OverdraftFactory implements AccountFactory {
    @Override
    public Account createAccount(String accNumber, double balance) { // Fixed signature
        return new OverdraftAccount(accNumber, balance);
    }
}