package com.bankapp.blockchainnode;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class User {
    private final String username;
    private final String password;
    private final List<Account> accounts;
    private final PublicKey publicKey;

    public User(String username, String password, PublicKey publicKey) {
        this.username = username;
        this.password = password;
        this.accounts = new ArrayList<>();
        this.publicKey = publicKey;
    }

    public boolean login(String password) {
        return this.password.equals(password);
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    // Getters
    public String getUsername() { return username; }
    public List<Account> getAccounts() { return accounts; }
    public PublicKey getPublicKey() { return publicKey; }
}