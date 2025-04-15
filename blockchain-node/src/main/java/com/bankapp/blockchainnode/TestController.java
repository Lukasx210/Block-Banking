package com.bankapp.blockchainnode;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.*;

@RestController
@RequestMapping("/test")
public class TestController {
    private final UserService userService;
    private final Blockchain blockchain;
    private KeyPair aliceKeys;
    private KeyPair bobKeys;

    public TestController(UserService userService, Blockchain blockchain) throws NoSuchAlgorithmException {
        this.userService = userService;
        this.blockchain = blockchain;
        initializeTestUsers();
    }

    private void initializeTestUsers() throws NoSuchAlgorithmException {
        aliceKeys = CryptoUtils.generateKeyPair();
        bobKeys = CryptoUtils.generateKeyPair();

        userService.addUser("alice", "pass123", aliceKeys.getPublic());
        userService.addUser("bob", "secure456", bobKeys.getPublic());

        // Create initial accounts
        //createAccount("alice", "pass123", "checking");
        //createAccount("bob", "secure456", "savings");

        // Directly create accounts through service
        User alice = userService.getUser("alice");
        Account aliceAccount = new CheckingAccount("ACC-ALICE", 1000);
        alice.addAccount(aliceAccount);

        User bob = userService.getUser("bob");
        Account bobAccount = new CheckingAccount("ACC-BOB", 1000); // Use CheckingAccount for both
        bob.addAccount(bobAccount);

    }

    @PostMapping("/account")
    public ResponseEntity<String> createAccount(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("type") String type) {

        try {
            User user = userService.login(username, password);
            if (user == null) {
                return ResponseEntity.status(401).body("Invalid credentials");
            }

            Account account = AccFactorySelector.getFactory(type)
                    .createAccount("ACC-" + System.currentTimeMillis(), 500);
            user.addAccount(account);

            return ResponseEntity.ok("Created account: " + account.getAccountNumber());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/transaction")
    public ResponseEntity<String> createTransaction(
            @RequestParam("sender") String sender,
            @RequestParam("password") String password,
            @RequestParam("recipient") String recipient,
            @RequestParam("amount") double amount) {

        try {
            System.out.println("[DEBUG] Transaction attempt: " + sender + " -> " + recipient + " $" + amount);

            User user = userService.login(sender, password);
            if (user == null) {
                System.out.println("[ERROR] Invalid credentials for: " + sender);
                return ResponseEntity.status(401).body("Invalid credentials!");
            }

            PrivateKey privateKey = sender.equals("alice") ? aliceKeys.getPrivate() : bobKeys.getPrivate();

            Transaction transaction = Transaction.createSignedTransaction(sender, recipient, amount, privateKey);
            System.out.println("[DEBUG] Created transaction: " + transaction);

            boolean success = userService.processTransaction(user, recipient, amount, transaction);
            return ResponseEntity.ok(success ? "Transaction successful!" : "Transaction failed!");
        } catch (Exception e) {
            System.out.println("[ERROR] Transaction failed: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/blockchain")
    public String validateChain() {
        return blockchain.isChainValid() ? "Chain is valid" : "Chain is invalid";
    }
}