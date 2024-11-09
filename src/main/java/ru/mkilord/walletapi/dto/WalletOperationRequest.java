package ru.mkilord.walletapi.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import jakarta.validation.constraints.NotNull;


import java.util.UUID;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WalletOperationRequest {
    @NotNull
    private UUID walletId;
    @NotNull
    private String operationType;
    @NotNull
    private Long amount;

}
