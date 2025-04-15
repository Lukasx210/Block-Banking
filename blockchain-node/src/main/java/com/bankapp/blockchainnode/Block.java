package com.bankapp.blockchainnode;

import java.security.MessageDigest;
import java.util.List;
import java.util.ArrayList;

public class Block {
    private int index;
    private long timestamp;
    private List<Transaction> transactions;
    private String previousHash;
    private String hash;
    private int nonce;

    public Block(int index, long timestamp, List<Transaction> transactions, String previousHash) {
        this.index = index;
        this.timestamp = timestamp;
        this.transactions = new ArrayList<>(transactions); // Defensive copy
        this.previousHash = previousHash;
        this.nonce = 0;
        this.hash = calculateHash();
    }

    public String calculateHash() {
        try {
            // Use all block data for hash calculation
            String input = index + timestamp + transactions.toString() + previousHash + nonce;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void mineBlock(int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0');
        long startTime = System.currentTimeMillis();
        System.out.println("Mining block with difficulty " + difficulty + "...");

        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();

            // Print progress every 1 million attempts
            if (nonce % 1_000_000 == 0) {
                System.out.println("Nonce: " + nonce + ", Hash: " + hash);
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Block mined: " + hash);
        System.out.println("Time taken: " + (endTime - startTime) / 1000 + " seconds");
    }

    // Getters
    public String getHash() { return hash; }
    public String getPreviousHash() { return previousHash; }
    public List<Transaction> getTransactions() { return transactions; }

    // Add these getters to expose all block data
    public int getIndex() { return index; }
    public long getTimestamp() { return timestamp; }
    public int getNonce() { return nonce; }
}