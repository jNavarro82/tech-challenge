package com.challenge.pricing.application;

import com.challenge.pricing.application.service.PriceUseCaseImpl;
import com.challenge.pricing.domain.exception.PriceNotFoundException;
import com.challenge.pricing.domain.model.Price;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

/**
 * Pure unit test: the repository port is mocked, so this validates the use case's own logic
 * (delegate to the port, translate an empty Optional into a domain exception) in isolation
 * from Spring, JPA, and H2 entirely - it should run in milliseconds.
 */
@ExtendWith(MockitoExtension.class)
class PriceUseCaseImplTest {

    @Mock
    private PriceManager priceManager;

    @Test
    void returnsThePriceWhenTheRepositoryFindsOne() {
        var service = new PriceUseCaseImpl(priceManager);
        var applicationDate = LocalDateTime.of(2020, 6, 14, 16, 0);
        var expected = new Price(1L, applicationDate.minusHours(1), applicationDate.plusHours(1),
                2, 35455L, 1, new BigDecimal("25.45"), "EUR");

        when(priceManager.findApplicablePrice(1L, 35455L, applicationDate))
                .thenReturn(Optional.of(expected));

        Price result = service.getApplicablePrice(1L, 35455L, applicationDate);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void throwsPriceNotFoundWhenTheRepositoryFindsNothing() {
        var service = new PriceUseCaseImpl(priceManager);
        var applicationDate = LocalDateTime.of(2019, 1, 1, 0, 0);

        when(priceManager.findApplicablePrice(1L, 35455L, applicationDate))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getApplicablePrice(1L, 35455L, applicationDate))
                .isInstanceOf(PriceNotFoundException.class);
    }
}
