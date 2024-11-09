package ru.mkilord.walletapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import ru.mkilord.walletapi.dto.WalletOperationRequest;
import ru.mkilord.walletapi.exception.InsufficientFundsException;
import ru.mkilord.walletapi.exception.WalletNotFoundException;
import ru.mkilord.walletapi.service.WalletService;

import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.mkilord.walletapi.service.WalletService.DEPOSIT;
import static ru.mkilord.walletapi.service.WalletService.WITHDRAW;

@FieldDefaults(level = PRIVATE)
@TestPropertySource(properties = {
        "PORT=8080"
})
@WebMvcTest(WalletController.class)
class WalletControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    WalletService walletService;
    @Autowired
    ObjectMapper objectMapper;

    final String URL = "/api/v1/wallet";

    @Test
    public void performOperation_shouldReturnOk() throws Exception {
        var request = new WalletOperationRequest();
        request.setWalletId(UUID.randomUUID());
        request.setOperationType(DEPOSIT);
        request.setAmount(100L);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Operation successful"));

    }

    @Test
    public void performOperation_shouldReturnBadRequest() throws Exception {
        var request = new WalletOperationRequest();
        request.setWalletId(UUID.randomUUID());
        request.setOperationType(WITHDRAW);
        request.setAmount(100L);

        doThrow(new InsufficientFundsException(50L, 100L))
                .when(walletService).updateBalance(any(UUID.class), eq(WITHDRAW), eq(100L));

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Insufficient funds. Current balance: 50, attempted withdrawal: 100"));
    }

    @Test
    public void testGetBalance_success() throws Exception {
        var walletId = UUID.randomUUID();
        when(walletService.getBalance(walletId)).thenReturn(100L);
        mockMvc.perform(get(URL + "/" + walletId))
                .andExpect(status().isOk())
                .andExpect(content().string("100"));
    }

    @Test
    public void testGetBalance_notFound() throws Exception {
        var walletId = UUID.randomUUID();
        when(walletService.getBalance(walletId))
                .thenThrow(new WalletNotFoundException(walletId));
        mockMvc.perform(get(URL + "/" + walletId))
                .andExpect(status().isNotFound());
    }

}