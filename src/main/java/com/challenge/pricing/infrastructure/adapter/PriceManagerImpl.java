package com.challenge.pricing.infrastructure.adapter;

import com.challenge.pricing.application.PriceManager;
import com.challenge.pricing.domain.model.Price;
import com.challenge.pricing.infrastructure.adapter.repository.PriceRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Implementation of PriceManager that uses a Spring Data JPA repository to find applicable prices.
 */
@Component
public class PriceManagerImpl implements PriceManager {

    private final PriceRepository jpaRepository;

    public PriceManagerImpl(PriceRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Price> findApplicablePrice(Long brandId, Long productId, LocalDateTime applicationDate) {
        return jpaRepository
                .findFirstByBrandIdAndProductIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(
                        brandId, productId, applicationDate, applicationDate)
                .map(this::toDomain);
    }

    private Price toDomain(com.challenge.pricing.infrastructure.entity.Price entity) {
        return new Price(
                entity.getBrandId(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getPriceList(),
                entity.getProductId(),
                entity.getPriority(),
                entity.getPrice(),
                entity.getCurrency()
        );
    }
}
