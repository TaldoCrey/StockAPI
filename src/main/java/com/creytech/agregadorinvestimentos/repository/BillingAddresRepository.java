package com.creytech.agregadorinvestimentos.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.creytech.agregadorinvestimentos.entity.BillingAddress;

@Repository
public interface BillingAddresRepository extends JpaRepository<BillingAddress, UUID>{

}
