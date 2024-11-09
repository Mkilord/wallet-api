package ru.mkilord.walletapi.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mkilord.walletapi.dto.WalletOperationRequest;
import ru.mkilord.walletapi.service.WalletService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallet")
@AllArgsConstructor
public class WalletController {
    private WalletService walletService;

    @PostMapping
    public ResponseEntity<String> performOperation(@Valid @RequestBody WalletOperationRequest request) {
        try{
        walletService.updateBalance(request.getWalletId(), request.getOperationType(), request.getAmount());
        return ResponseEntity.ok("Operation successful");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/{walletID}")
    public ResponseEntity<Long> getBalance(@PathVariable UUID walletID){
        try{
            var balance = walletService.getBalance(walletID);
            return ResponseEntity.ok(balance);
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }
}
