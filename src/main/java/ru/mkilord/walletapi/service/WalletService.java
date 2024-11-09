package ru.mkilord.walletapi.service;

import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mkilord.walletapi.exception.InsufficientFundsException;
import ru.mkilord.walletapi.exception.WalletNotFoundException;
import ru.mkilord.walletapi.model.Wallet;
import ru.mkilord.walletapi.repository.WalletRepository;

import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Service
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class WalletService {
    public static final String WITHDRAW = "WITHDRAW";
    public static final String DEPOSIT = "DEPOSIT";

    private final WalletRepository walletRepository;

    @Transactional
    public void updateBalance(UUID walletId, String operationType, long amount) {
        var wallet = findById(walletId);
        if (operationType.equals(WITHDRAW)) {
            withdraw(wallet, amount);
        } else if (operationType.equals(DEPOSIT)) {
            deposit(wallet, amount);
        }
        walletRepository.save(wallet);
    }

    public long getBalance(UUID walletId) {
        return findById(walletId).getBalance();
    }

    private Wallet findById(UUID id) {
        return walletRepository.findById(id)
                .orElseThrow(() -> new WalletNotFoundException(id));
    }

    private void withdraw(Wallet wallet, long amount) {
        var balance = wallet.getBalance();
        if (balance < amount) {
            throw new InsufficientFundsException(balance, amount);
        }
        wallet.setBalance(balance - amount);
    }

    private void deposit(Wallet wallet, long amount) {
        wallet.setBalance(wallet.getBalance() + amount);
    }
}
