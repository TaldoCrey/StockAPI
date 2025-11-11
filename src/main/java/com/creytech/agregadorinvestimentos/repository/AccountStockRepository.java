package com.creytech.agregadorinvestimentos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.creytech.agregadorinvestimentos.entity.AccountStock;
import com.creytech.agregadorinvestimentos.entity.AccountStockId;

@Repository
public interface AccountStockRepository extends JpaRepository<AccountStock, AccountStockId>{

}
