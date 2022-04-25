package com.sikorski.commission.application;

import com.sikorski.commission.api.dto.CommissionResponse;
import com.sikorski.commission.api.dto.TransactionRequest;
import com.sikorski.commission.domain.dao.ClientRepository;
import com.sikorski.commission.domain.dao.CommissionRepository;
import com.sikorski.commission.domain.fx.ExchangeRateRepository;
import com.sikorski.commission.domain.discount.DiscountEngine;
import com.sikorski.commission.domain.entity.Client;
import com.sikorski.commission.domain.entity.Money;
import com.sikorski.commission.domain.entity.Transaction;
import com.sikorski.commission.domain.fx.ExchangeRateNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommissionService {
    private final ClientRepository clientRepository;
    private final CommissionRepository commissionRepository;
    private final ExchangeRateRepository exchangeRateRepository;
    private final DiscountEngine discountEngine;

    public CommissionResponse getCommission(TransactionRequest request) throws ExchangeRateNotFound {
        var money = getMoney(request);
        var client = getClient(request.getClientId());
        var transaction = Transaction.create(money, request.getDate(), client);
        var commission = client.commitTransaction(transaction, discountEngine);

        clientRepository.save(client);
        commissionRepository.save(commission);

        return new CommissionResponse(commission);
    }

    private Money getMoney(TransactionRequest request) throws ExchangeRateNotFound {
        var money = new Money(request.getAmount());
        if (money.isDefaultCurrency(request.getCurrency())) {
            return money;
        }
        var rate = exchangeRateRepository.getRate(request.getCurrency(), request.getDate());
        return money.divide(rate);
    }

    private Client getClient(Integer clientId) {
        return clientRepository.findClientByClientId(clientId)
                .orElse(Client.create(clientId));
    }
}