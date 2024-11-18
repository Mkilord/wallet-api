package ru.mkilord.walletapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Entity
@Table(name = "wallet",schema = "wallet_schema")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Wallet {
    @Id
    private UUID id;

    @Column(nullable = false)
    private long balance;

}
