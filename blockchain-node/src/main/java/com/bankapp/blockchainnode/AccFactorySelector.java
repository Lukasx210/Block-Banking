package com.bankapp.blockchainnode;

public class AccFactorySelector {
    public static AccountFactory getFactory(String accountType) {
        return switch (accountType.toLowerCase()) {
            case "checking" -> new CheckingFactory();
            case "savings" -> new SavingsFactory();
            case "overdraft" -> new OverdraftFactory();
            default -> throw new IllegalArgumentException("Invalid account type");
        };
    }
}