package com.sikorski.commission.domain.handler;

import an.awesome.pipelinr.Command;
import com.sikorski.commission.domain.command.CalculateCommissionCommand;
import com.sikorski.commission.domain.repository.ClientRepository;
import com.sikorski.commission.domain.discount.DiscountEngine;
import com.sikorski.commission.domain.entity.Client;
import com.sikorski.commission.domain.entity.Commission;
import com.sikorski.commission.domain.entity.Money;
import com.sikorski.commission.domain.entity.Transaction;
import com.sikorski.commission.domain.repository.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CalculateCommissionCommandHandler implements Command.Handler<CalculateCommissionCommand, Commission> {
    private final ClientRepository clientRepository;
    private final ExchangeRateRepository exchangeRateRepository;
    private final DiscountEngine discountEngine;

    @Override
    public Commission handle(CalculateCommissionCommand command) {
        var money = getMoney(command);
        var client = getClient(command.clientId());
        var transaction = Transaction.create(money, command.date(), client);
        var commission = client.calculateCommission(transaction, discountEngine);

        clientRepository.save(client);

        return commission;
    }

    private Money getMoney(CalculateCommissionCommand command) {
        var money = new Money(command.amount());
        if (money.isDefaultCurrency(command.currency())) {
            return money;
        }
        var rate = exchangeRateRepository.getRate(command.currency(), command.date());
        return money.divide(rate);
    }

    private Client getClient(Integer clientId) {
        return clientRepository.findClientByClientId(clientId)
                .orElse(Client.create(clientId));
    }
}
