package com.creytech.agregadorinvestimentos.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.creytech.agregadorinvestimentos.controller.dto.CreateStockDto;
import com.creytech.agregadorinvestimentos.service.StockService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/v1/stocks")
public class StockController {
    private StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping
    public ResponseEntity<Void> createStock(@RequestBody CreateStockDto creatStockDto) {
        stockService.createStock(creatStockDto);
        
        return ResponseEntity.ok().build();
    }
    
}
