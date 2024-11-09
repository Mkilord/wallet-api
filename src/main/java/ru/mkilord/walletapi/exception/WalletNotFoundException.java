package ru.mkilord.walletapi.exception;

import java.util.UUID;

public class WalletNotFoundException extends RuntimeException{
    public WalletNotFoundException(UUID walletID){
        super("Wallet not found: " + walletID);
    }
}
