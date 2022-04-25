package com.sikorski.commission.domain.dao;

import com.sikorski.commission.domain.entity.Commission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CommissionRepository extends JpaRepository<Commission, UUID> {
}