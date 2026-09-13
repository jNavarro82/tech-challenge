package com.challenge.pricing.application.service;

import com.challenge.pricing.api.GetApplicablePriceUseCase;
import com.challenge.pricing.application.PriceManager;
import com.challenge.pricing.domain.exception.PriceNotFoundException;
import com.challenge.pricing.domain.model.Price;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Application service implementing the use case.
 */
@Service
public class PriceUseCaseImpl implements GetApplicablePriceUseCase {

    private final PriceManager priceManager;

    public PriceUseCaseImpl(PriceManager priceManager) {
        this.priceManager = priceManager;
    }

    @Override
    public Price getApplicablePrice(Long brandId, Long productId, LocalDateTime applicationDate) {
        return priceManager.findApplicablePrice(brandId, productId, applicationDate)
                .orElseThrow(() -> new PriceNotFoundException(brandId, productId, applicationDate));
    }
}
