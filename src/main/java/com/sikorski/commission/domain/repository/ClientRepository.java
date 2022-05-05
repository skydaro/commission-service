package com.sikorski.commission.domain.repository;

import com.sikorski.commission.domain.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {
    Optional<Client> findClientByClientId(Integer clientId);
}