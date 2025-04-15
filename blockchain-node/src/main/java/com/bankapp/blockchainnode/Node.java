package com.bankapp.blockchainnode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.*;

@Component
public class Node {
    @Value("${spring.application.instance-id:${random.uuid}}")
    private String id;

    @Autowired
    private UserService userService;

    public String getId() {
        return id;
    }

    // This would be exposed via a REST endpoint
    public boolean validateTransaction(Transaction transaction) {
        try {
            PublicKey senderPublicKey = userService.getUserPublicKey(transaction.getSender());
            if (!transaction.verifySignature(senderPublicKey)) {
                return false;
            }

            User sender = userService.getUser(transaction.getSender());
            if (sender == null || sender.getAccounts().isEmpty()) {
                return false;
            }

            Account senderAccount = sender.getAccounts().get(0);
            return senderAccount.canWithdraw(transaction.getAmount());
        } catch (Exception e) {
            return false;
        }
    }
}