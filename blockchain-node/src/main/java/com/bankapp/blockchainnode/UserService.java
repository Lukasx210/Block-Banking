package com.bankapp.blockchainnode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Lazy;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    private final Map<String, User> users = new HashMap<>();
    private final Blockchain blockchain;

    @Autowired
    public UserService(@Lazy Blockchain blockchain) {
        this.blockchain = blockchain;
    }

    public void addUser(String username, String password, PublicKey publicKey) {
        users.put(username, new User(username, password, publicKey));
    }

    public boolean processTransaction(User sender, String recipientUsername, double amount, Transaction transaction) throws Exception {
        System.out.println("[DEBUG] Validating signature for: " + transaction.getSender());
        PublicKey senderPublicKey = getUserPublicKey(transaction.getSender());
        if (senderPublicKey == null) {
            System.out.println("[ERROR] Sender " + transaction.getSender() + " not found!");
            return false;
        }
        User recipient = users.get(recipientUsername);
        if (recipient == null || sender.getAccounts().isEmpty() || recipient.getAccounts().isEmpty()) {
            return false;
        }

        Account senderAccount = sender.getAccounts().get(0);
        Account recipientAccount = recipient.getAccounts().get(0);

        if (!senderAccount.canWithdraw(amount) || !blockchain.validateTransaction(transaction)) {
            return false;
        }

        blockchain.addTransaction(transaction);
        blockchain.mineBlock();

        if (blockchain.isChainValid()) {
            senderAccount.withdraw(amount);
            recipientAccount.deposit(amount);
            return true;
        }
        return false;
    }

    public User login(String username, String password) {
        User user = users.get(username);
        if (user != null && user.login(password)) return user;
        return null;
    }

    public User getUser(String username) {
        return users.get(username);
    }

    public PublicKey getUserPublicKey(String username) {
        User user = users.get(username);
        return user != null ? user.getPublicKey() : null;
    }
}