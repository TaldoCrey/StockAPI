package com.creytech.agregadorinvestimentos.service;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.creytech.agregadorinvestimentos.controller.dto.AccountStockDto;
import com.creytech.agregadorinvestimentos.controller.dto.AccountStockResponseDto;
import com.creytech.agregadorinvestimentos.entity.AccountStock;
import com.creytech.agregadorinvestimentos.entity.AccountStockId;
import com.creytech.agregadorinvestimentos.repository.AccountRepository;
import com.creytech.agregadorinvestimentos.repository.AccountStockRepository;
import com.creytech.agregadorinvestimentos.repository.StockRepository;

@Service
public class AccountService {
    private AccountRepository accountRepository;
    private StockRepository stockRepository;
    private AccountStockRepository accountStockRepository;

    public AccountService(AccountRepository accountRepository, StockRepository stockRepository, AccountStockRepository accountStockRepository) {
        this.accountRepository = accountRepository;
        this.stockRepository = stockRepository;
        this.accountStockRepository = accountStockRepository;
    }


    public void associateStock(String accountId, AccountStockDto accountStockDto) {
        var account = accountRepository.findById(UUID.fromString(accountId))
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var stock = stockRepository.findById(accountStockDto.stockId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        
        //DTO -> Entity

        var id = new AccountStockId(account.getAccountId(), stock.getStockId());
        var entity = new AccountStock(
            account,
            stock,
            id,
            accountStockDto.quantity()
        );

        accountStockRepository.save(entity);

    }


    public List<AccountStockResponseDto> listStock(String accountId) {
        
        var account = accountRepository.findById(UUID.fromString(accountId))
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return account.getAccountStocks().stream()
            .map(as -> new AccountStockResponseDto(as.getStock().getStockId(), as.getQuantity(), 0.0))
            .toList();
    }
}
