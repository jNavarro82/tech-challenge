package com.challenge.pricing.infrastructure.adapter;

import com.challenge.pricing.domain.model.Price;
import com.challenge.pricing.infrastructure.adapter.repository.PriceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PriceManagerImplTest {

	@Mock
	private PriceRepository jpaRepository;

	@Test
	void returnsMappedDomainPriceWhenRepositoryFindsApplicableEntity() {
		var manager = new PriceManagerImpl(jpaRepository);
		var applicationDate = LocalDateTime.of(2020, 6, 14, 16, 0);
		var entity = new com.challenge.pricing.infrastructure.entity.Price(
				1L,
				LocalDateTime.of(2020, 6, 14, 15, 0),
				LocalDateTime.of(2020, 6, 14, 18, 30),
				2,
				35455L,
				1,
				new BigDecimal("25.45"),
				"EUR"
		);

		when(jpaRepository.findFirstByBrandIdAndProductIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(
				1L, 35455L, applicationDate, applicationDate)).thenReturn(Optional.of(entity));

		var result = manager.findApplicablePrice(1L, 35455L, applicationDate);

		assertThat(result).isPresent();
		assertThat(result.get()).isEqualTo(new Price(
				1L,
				LocalDateTime.of(2020, 6, 14, 15, 0),
				LocalDateTime.of(2020, 6, 14, 18, 30),
				2,
				35455L,
				1,
				new BigDecimal("25.45"),
				"EUR"
		));
	}

	@Test
	void returnsEmptyWhenRepositoryFindsNoApplicableEntity() {
		var manager = new PriceManagerImpl(jpaRepository);
		var applicationDate = LocalDateTime.of(2019, 1, 1, 0, 0);

		when(jpaRepository.findFirstByBrandIdAndProductIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(
				1L, 35455L, applicationDate, applicationDate)).thenReturn(Optional.empty());

		var result = manager.findApplicablePrice(1L, 35455L, applicationDate);

		assertThat(result).isEmpty();
	}
}