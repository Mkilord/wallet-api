package ru.mkilord.walletapi.exception;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(long currentBalance, long amount) {
        super("Insufficient funds. Current balance: " + currentBalance + ", attempted withdrawal: " + amount);
    }
}
