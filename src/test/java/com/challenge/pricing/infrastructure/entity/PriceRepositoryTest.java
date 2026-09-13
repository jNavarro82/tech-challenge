package com.challenge.pricing.infrastructure.entity;

import com.challenge.pricing.infrastructure.adapter.repository.PriceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Targets the derived query directly, bypassing the web/application layers, using a brand and
 * product not present in the exercise's seed data so this test is independent of data.sql
 * content. Verifies the core business rule the whole exercise hinges on: when two tariffs
 * overlap the same instant, the higher PRIORITY value wins.
 */
@DataJpaTest
@Sql(statements = {
        "INSERT INTO PRICES (BRAND_ID, START_DATE, END_DATE, PRICE_LIST, PRODUCT_ID, PRIORITY, PRICE, CURR) " +
                "VALUES (99, '2024-01-01 00:00:00', '2024-01-31 23:59:59', 10, 111, 0, 10.00, 'EUR')",
        "INSERT INTO PRICES (BRAND_ID, START_DATE, END_DATE, PRICE_LIST, PRODUCT_ID, PRIORITY, PRICE, CURR) " +
                "VALUES (99, '2024-01-10 00:00:00', '2024-01-20 23:59:59', 11, 111, 5, 20.00, 'EUR')"
})
class PriceRepositoryTest {

    @Autowired
    private PriceRepository repository;

    @Test
    void picksTheHigherPriorityRowWhenTwoWindowsOverlap() {
        var applicationDate = LocalDateTime.of(2024, 1, 15, 12, 0); // inside both windows

        var result = repository.findFirstByBrandIdAndProductIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(
                99L, 111L, applicationDate, applicationDate);

        assertThat(result).isPresent();
        assertThat(result.get()).extracting("priority").isEqualTo(5);
        assertThat(result.get()).extracting("price").isEqualTo(new BigDecimal("20.00"));
    }

    @Test
    void fallsBackToLowerPriorityWindowOnceTheHigherOneHasEnded() {
        var applicationDate = LocalDateTime.of(2024, 1, 25, 12, 0); // only the base window covers this

        var result = repository.findFirstByBrandIdAndProductIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(
                99L, 111L, applicationDate, applicationDate);

        assertThat(result).isPresent();
        assertThat(result.get()).extracting("priority").isEqualTo(0);
    }

    @Test
    void returnsEmptyWhenNoWindowCoversTheDate() {
        var applicationDate = LocalDateTime.of(2023, 12, 1, 0, 0);

        var result = repository.findFirstByBrandIdAndProductIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(
                99L, 111L, applicationDate, applicationDate);

        assertThat(result).isEmpty();
    }
}
