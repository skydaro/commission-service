package com.sikorski.commission.domain.dao;

import com.sikorski.commission.domain.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}