package com.sikorski.commission.configuration.discount;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.sikorski.commission.domain.discount.DiscountRule;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
public class DiscountConfiguration {

    @SneakyThrows
    @Bean
    Set<DiscountRule> rules(@Value("classpath:discounts.yaml") Resource resource) {
        List<DiscountRule> discounts = mapDiscounts(resource.getInputStream());
        validateUniqueDiscounts(discounts);
        return new HashSet<>(discounts);
    }

    private List<DiscountRule> mapDiscounts(InputStream inputStream) throws IOException {
        return getYamlMapper()
                .readValue(inputStream, getDiscountTypeRef());
    }

    private static ObjectMapper getYamlMapper() {
        return new ObjectMapper(new YAMLFactory());
    }

    private static TypeReference<List<DiscountRule>> getDiscountTypeRef() {
        return new TypeReference<>() {
        };
    }

    private void validateUniqueDiscounts(List<DiscountRule> discounts) {
        if (discounts.size() != new HashSet<>(discounts).size()) {
            throw new IllegalArgumentException();
        }
    }
}