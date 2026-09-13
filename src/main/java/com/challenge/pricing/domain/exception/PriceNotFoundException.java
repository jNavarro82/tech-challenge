package com.challenge.pricing.domain.exception;

import java.time.LocalDateTime;

/**
 * Raised when no price row's validity window covers the requested date for the given
 * brand/product pair.
 */
public class PriceNotFoundException extends RuntimeException {

    public PriceNotFoundException(Long brandId, Long productId, LocalDateTime applicationDate) {
        super("No applicable price found for brandId=%d, productId=%d, applicationDate=%s"
                .formatted(brandId, productId, applicationDate));
    }
}
