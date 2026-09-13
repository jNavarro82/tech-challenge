package com.challenge.pricing.infrastructure.adapter.repository;

import com.challenge.pricing.infrastructure.entity.Price;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Spring Data derives the full query from the method name:
 *   - startDate <= applicationDate AND endDate >= applicationDate  -> the window contains the date
 *   - ordered by priority descending, and we take only the first row
 */
public interface PriceRepository extends JpaRepository<Price, Long> {

    Optional<Price> findFirstByBrandIdAndProductIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(
            Long brandId,
            Long productId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );
}
