package com.sikorski.commission.domain.discount;

import com.sikorski.commission.domain.entity.Client;
import com.sikorski.commission.domain.entity.Commission;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class DiscountEngine {
    private final Set<Discount> discountRules;

    public Commission process(Client client) {
        return discountRules.stream()
                .filter(rule -> rule.matches(client))
                .map(rule -> calculate(client, rule))
                .min(Comparator.comparing(Commission::getAmount))
                .orElseThrow();
    }

    private Commission calculate(Client client, DiscountRule rule) {
        var commission = rule.calculate(client);
        log.info("{} for clientId: {}", commission.getInfoLog(), client.getClientId());
        return commission;
    }
}
