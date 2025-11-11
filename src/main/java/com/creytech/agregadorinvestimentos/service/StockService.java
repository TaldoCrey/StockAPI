package com.creytech.agregadorinvestimentos.service;

import org.springframework.stereotype.Service;

import com.creytech.agregadorinvestimentos.controller.dto.CreateStockDto;
import com.creytech.agregadorinvestimentos.entity.Stock;
import com.creytech.agregadorinvestimentos.repository.StockRepository;

@Service
public class StockService {
    private StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public void createStock(CreateStockDto createStockDto) {
        //DTO -> ENTITY
        var stock = new Stock(
            createStockDto.stockId(),
            createStockDto.description()
        );

        stockRepository.save(stock);
    }
}
