package com.sikorski.commission.configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.sikorski.commission.domain.discount.Discount;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Configuration
public class DiscountConfiguration {

    @SneakyThrows
    @Bean
    Set<Discount> rules(@Value("classpath:discounts.yaml") Resource resource) {
        List<Discount> discounts = mapDiscounts(resource.getInputStream());
        validateUniqueDiscounts(discounts);
        return new HashSet<>(discounts);
    }

    private List<Discount> mapDiscounts(InputStream inputStream) throws IOException {
        return getYamlMapper()
                .readValue(inputStream, getDiscountTypeRef());
    }

    private static ObjectMapper getYamlMapper() {
        return new ObjectMapper(new YAMLFactory());
    }

    private static TypeReference<List<Discount>> getDiscountTypeRef() {
        return new TypeReference<>() {
        };
    }

    private void validateUniqueDiscounts(List<Discount> discounts) {
        if (discounts.size() != new HashSet<>(discounts).size()) {
            throw new IllegalArgumentException();
        }
    }
}