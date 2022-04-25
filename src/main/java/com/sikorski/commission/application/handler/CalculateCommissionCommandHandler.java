package com.sikorski.commission.application.handler;

import an.awesome.pipelinr.Command;
import com.sikorski.commission.api.controller.dto.TransactionRequest;
import com.sikorski.commission.application.command.CalculateCommissionCommand;
import com.sikorski.commission.domain.dao.ClientRepository;
import com.sikorski.commission.domain.dao.CommissionRepository;
import com.sikorski.commission.domain.discount.DiscountEngine;
import com.sikorski.commission.domain.entity.Client;
import com.sikorski.commission.domain.entity.Commission;
import com.sikorski.commission.domain.entity.Money;
import com.sikorski.commission.domain.entity.Transaction;
import com.sikorski.commission.domain.fx.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CalculateCommissionCommandHandler implements Command.Handler<CalculateCommissionCommand, Commission> {
    private final ClientRepository clientRepository;
    private final CommissionRepository commissionRepository;
    private final ExchangeRateRepository exchangeRateRepository;
    private final DiscountEngine discountEngine;

    @Override
    public Commission handle(CalculateCommissionCommand command) {
        var money = getMoney(command.request());
        var client = getClient(command.request().getClientId());
        var transaction = Transaction.create(money, command.request().getDate(), client);
        var commission = client.commitTransaction(transaction, discountEngine);

        clientRepository.save(client);
        commissionRepository.save(commission);

        return commission;
    }

    private Money getMoney(TransactionRequest request) {
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
