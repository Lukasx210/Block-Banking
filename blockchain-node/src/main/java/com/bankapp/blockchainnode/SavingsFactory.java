package com.bankapp.blockchainnode;

class SavingsFactory implements AccountFactory {
    @Override
    public Account createAccount(String accNumber, double balance) { // Fixed signature
        return new SavingsAccount(accNumber, balance);
    }
}