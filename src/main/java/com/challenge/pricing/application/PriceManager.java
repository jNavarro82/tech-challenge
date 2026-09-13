package com.challenge.pricing.application;

import com.challenge.pricing.domain.model.Price;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Price manager.
 */
public interface PriceManager {

    /**
     * Returns the highest-priority price row whose validity window contains applicationDate,
     * for the given brand and product.
     */
    Optional<Price> findApplicablePrice(Long brandId, Long productId, LocalDateTime applicationDate);
}
