package com.bankapp.blockchainnode;

public interface AccountFactory {
    Account createAccount(String accNumber, double balance);
}