package com.creytech.agregadorinvestimentos.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.creytech.agregadorinvestimentos.controller.dto.AccountStockDto;
import com.creytech.agregadorinvestimentos.controller.dto.AccountStockResponseDto;
import com.creytech.agregadorinvestimentos.controller.dto.CreateAccountDto;
import com.creytech.agregadorinvestimentos.service.AccountService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/v1/accounts")
public class AccountController {

    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/{accountId}/stocks")
    public ResponseEntity<Void> associateStock(@PathVariable("accountId") String accountId, @RequestBody AccountStockDto accountStockDto) {
        
        accountService.associateStock(accountId, accountStockDto);
        
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{accountId}/stocks")
    public ResponseEntity<List<AccountStockResponseDto>> listStocks(@PathVariable("accountId") String accountId) {
        
        var stocks = accountService.listStock(accountId);
        
        return ResponseEntity.ok(stocks);
    }
    
}
