package com.bankapp.blockchainnode;

import java.security.*;

public class Transaction {
    private String sender;
    private String recipient;
    private double amount;
    private String signature;

    public Transaction(String sender, String recipient, double amount, String signature) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.signature = signature;
    }

    public static Transaction createSignedTransaction(String sender, String recipient, double amount, PrivateKey privateKey) throws Exception {
        String data = String.format("sender=%s,recipient=%s,amount=%.2f", sender, recipient, amount);
        System.out.println("Data to sign: " + data); // Debugging
        String signature = CryptoUtils.sign(data, privateKey);
        return new Transaction(sender, recipient, amount, signature);
    }

    public boolean verifySignature(PublicKey publicKey) throws Exception {
        String data = String.format("sender=%s,recipient=%s,amount=%.2f", sender, recipient, amount);
        System.out.println("Data to verify: " + data); // Debugging
        return CryptoUtils.verify(data, signature, publicKey);
    }

    // Getters
    public String getSender() { return sender; }
    public String getRecipient() { return recipient; }
    public double getAmount() { return amount; }
    public String getSignature() { return signature; }
}