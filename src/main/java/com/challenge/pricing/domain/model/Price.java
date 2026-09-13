package com.challenge.pricing.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Price domain model. Represents a price row in the database.
 */
public record Price(
        Long brandId,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Integer priceList,
        Long productId,
        Integer priority,
        BigDecimal amount,
        String currency
) {
}
