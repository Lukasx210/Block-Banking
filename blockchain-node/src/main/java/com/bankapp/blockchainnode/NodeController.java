package com.bankapp.blockchainnode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NodeController {
    private final Node node;
    private final Blockchain blockchain;

    @Autowired
    public NodeController(Node node, Blockchain blockchain) {
        this.node = node;
        this.blockchain = blockchain;
    }

    // Existing validation endpoint
    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateTransaction(@RequestBody Transaction transaction) {
        try {
            return ResponseEntity.ok(node.validateTransaction(transaction));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(false);
        }
    }

    // New endpoints for transaction submission and block synchronization
    @PostMapping("/transactions")
    public ResponseEntity<String> submitTransaction(@RequestBody Transaction transaction) {
        try {
            // Directly process without PBFT for testing
            blockchain.addTransaction(transaction);
            blockchain.mineBlock();
            return ResponseEntity.ok("Transaction mined!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/blocks")
    public ResponseEntity<List<Block>> getBlocks() {
        return ResponseEntity.ok(blockchain.getChain());
    }
}
