package com.bankapp.blockchainnode;

class CheckingFactory implements AccountFactory {
    @Override
    public Account createAccount(String accNumber, double balance) { // Fixed signature
        return new CheckingAccount(accNumber, balance);
    }
}