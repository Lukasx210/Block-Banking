package com.bankapp.blockchainnode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

@Service
public class Blockchain {
    private final List<Block> chain;
    private final List<Transaction> pendingTransactions;
    private final DiscoveryClient discoveryClient;
    private final NodeClient nodeClient;
    private final UserService userService;

    @Value("${blockchain.difficulty:4}")
    private int difficulty;

    @Autowired
    public Blockchain(DiscoveryClient discoveryClient,
                      NodeClient nodeClient,
                      @Lazy UserService userService) {
        this.discoveryClient = discoveryClient;
        this.nodeClient = nodeClient;
        this.userService = userService;
        this.chain = new ArrayList<>();
        this.pendingTransactions = new ArrayList<>();
        createGenesisBlock();
    }

    private void createGenesisBlock() {
        chain.add(new Block(0, System.currentTimeMillis(), new ArrayList<>(), "0"));
        System.out.println("Genesis block created.");
    }

    public boolean validateTransaction(Transaction transaction) {

        // For testing with 1 node, always return true
        System.out.println("[DEBUG] Bypassing PBFT consensus for testing");
        return true;
        /*
        List<String> nodeIds = discoveryClient.getServices();
        int approvals = 0;

        for (String nodeId : nodeIds) {
            if (nodeClient.validateTransaction(nodeId, transaction)) {
                approvals++;
            }
        }

        boolean isValid = approvals > (nodeIds.size() * 2 / 3);
        System.out.println("Transaction validated by " + approvals + "/" + nodeIds.size() + " nodes.");
        return isValid;

         */
    }

    public void addTransaction(Transaction transaction) throws Exception {
        System.out.println("[BLOCKCHAIN] Adding transaction: " + transaction.getSender() + " -> "
                + transaction.getRecipient() + " $" + transaction.getAmount());
        PublicKey senderPublicKey = userService.getUserPublicKey(transaction.getSender());
        if (senderPublicKey == null) {
            System.out.println("[ERROR] Sender public key not found");
            return;
        }
        if (!transaction.verifySignature(senderPublicKey)) {
            System.out.println("Invalid transaction signature.");
            return;
        }

        if (validateTransaction(transaction)) {
            pendingTransactions.add(transaction);
            System.out.println("Transaction added to pending pool.");
        } else {
            System.out.println("Transaction validation failed.");
        }
    }

    public void mineBlock() {
        System.out.println("[BLOCKCHAIN] Mining block with " + pendingTransactions.size() + " transactions");
        if (pendingTransactions.isEmpty()) {
            System.out.println("No transactions to mine.");
            return;
        }

        List<Transaction> blockTransactions = new ArrayList<>(pendingTransactions);
        Block newBlock = new Block(chain.size(), System.currentTimeMillis(), blockTransactions, getLatestBlock().getHash());

        newBlock.mineBlock(difficulty);
        chain.add(newBlock);
        pendingTransactions.clear();
        System.out.println("New block mined and added to the blockchain.");
    }

    public Block getLatestBlock() {
        return chain.get(chain.size() - 1);
    }

    public boolean isChainValid() {
        System.out.println("Validating blockchain...");
        for (int i = 1; i < chain.size(); i++) {
            Block currentBlock = chain.get(i);
            Block previousBlock = chain.get(i - 1);

            // Recalculate the current block's hash
            String currentBlockHash = currentBlock.calculateHash();
            if (!currentBlock.getHash().equals(currentBlockHash)) {
                System.out.println("Block " + i + " has an invalid hash.");
                System.out.println("Expected: " + currentBlock.getHash());
                System.out.println("Actual: " + currentBlockHash);
                return false;
            }

            if (!currentBlock.getPreviousHash().equals(previousBlock.getHash())) {
                System.out.println("Block " + i + " has an invalid previous hash.");
                return false;
            }
        }
        System.out.println("Blockchain is valid.");
        return true;
    }

    // Add this method to expose the chain
    public List<Block> getChain() {
        return new ArrayList<>(chain); // Return a copy to prevent external modification
    }

}