package com.challenge.pricing.api;

import com.challenge.pricing.domain.exception.PriceNotFoundException;
import com.challenge.pricing.domain.model.Price;

import java.time.LocalDateTime;

/**
 * GetApplicablePriceUseCase is a use case interface that defines the contract
 * for resolving the applicable price for a product of a given brand at
 * a specific point in time.
 */
public interface GetApplicablePriceUseCase {

    /**
     * Resolves the single applicable price for a product of a given brand at a given instant.
     *
     * @throws PriceNotFoundException if no price row
     *         covers the requested date for that brand/product combination.
     */
    Price getApplicablePrice(Long brandId, Long productId, LocalDateTime applicationDate);
}
