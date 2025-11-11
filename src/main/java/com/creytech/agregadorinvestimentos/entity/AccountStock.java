package com.creytech.agregadorinvestimentos.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name="tb_accounts_stocks")
public class AccountStock {

    @EmbeddedId
    private AccountStockId id;

    @ManyToOne
    @MapsId("accountId")
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @MapsId("stockId")
    @JoinColumn(name = "stock_id")
    private Stock stock;

    @Column(name = "quantity")
    private Integer quantity;

    public AccountStock() {}

    public AccountStock(Account account, Stock stock, AccountStockId id, Integer quantity) {
        this.account = account;
        this.stock = stock;
        this.id = id;
        this.quantity = quantity;
    }

    public Account getAccount() {
        return account;
    }

    public Stock getStock() {
        return stock;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    
}
