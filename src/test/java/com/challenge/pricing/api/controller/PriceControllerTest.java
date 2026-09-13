package com.challenge.pricing.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.challenge.pricing.api.GetApplicablePriceUseCase;
import com.challenge.pricing.domain.model.Price;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class PriceControllerTest {

  @Mock
  private GetApplicablePriceUseCase getApplicablePriceUseCase;

  @Test
  void returns200WithMappedPriceWhenUseCaseResolvesOneCase1000am() {
    var controller = new PriceController(getApplicablePriceUseCase);
    var applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);
    var price = new Price(1L, applicationDate.minusHours(1), applicationDate.plusHours(1),
        1, 35455L, 1, new BigDecimal("35.50"), "EUR");

    when(getApplicablePriceUseCase.getApplicablePrice(1L, 35455L, applicationDate))
        .thenReturn(price);

    var result = controller.getApplicablePrice(1L, 35455L, applicationDate);

    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(result.getBody()).isNotNull();
    assertThat(result.getBody().priceList()).isEqualTo(1);
    assertThat(result.getBody().price()).isEqualTo(new BigDecimal("35.50"));
  }

  @Test
  void returns200WithMappedPriceWhenUseCaseResolvesOneCaseTwoWindows1600pm() {
    var controller = new PriceController(getApplicablePriceUseCase);
    var applicationDate = LocalDateTime.of(2020, 6, 14, 16, 0);
    var price = new Price(1L, applicationDate.minusHours(1), applicationDate.plusHours(1),
        2, 35455L, 1, new BigDecimal("25.45"), "EUR");

    when(getApplicablePriceUseCase.getApplicablePrice(1L, 35455L, applicationDate))
        .thenReturn(price);

    var result = controller.getApplicablePrice(1L, 35455L, applicationDate);

    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(result.getBody()).isNotNull();
    assertThat(result.getBody().priceList()).isEqualTo(2);
    assertThat(result.getBody().price()).isEqualTo(new BigDecimal("25.45"));
  }

  @Test
  void returns200WithMappedPriceWhenUseCaseResolvesOneCase2100pm() {
    var controller = new PriceController(getApplicablePriceUseCase);
    var applicationDate = LocalDateTime.of(2020, 6, 14, 21, 0);
    var price = new Price(1L, applicationDate.minusHours(1), applicationDate.plusHours(1),
        2, 35455L, 1, new BigDecimal("35.50"), "EUR");

    when(getApplicablePriceUseCase.getApplicablePrice(1L, 35455L, applicationDate))
        .thenReturn(price);

    var result = controller.getApplicablePrice(1L, 35455L, applicationDate);

    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(result.getBody()).isNotNull();
    assertThat(result.getBody().priceList()).isEqualTo(2);
    assertThat(result.getBody().price()).isEqualTo(new BigDecimal("35.50"));
  }

  @Test
  void returns200WithMappedPriceWhenUseCaseResolvesOneCaseTwoWindows1000am() {
    var controller = new PriceController(getApplicablePriceUseCase);
    var applicationDate = LocalDateTime.of(2020, 6, 15, 10, 0);
    var price = new Price(1L, applicationDate.minusHours(1), applicationDate.plusHours(1),
        2, 35455L, 1, new BigDecimal("30.50"), "EUR");

    when(getApplicablePriceUseCase.getApplicablePrice(1L, 35455L, applicationDate))
        .thenReturn(price);

    var result = controller.getApplicablePrice(1L, 35455L, applicationDate);

    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(result.getBody()).isNotNull();
    assertThat(result.getBody().priceList()).isEqualTo(2);
    assertThat(result.getBody().price()).isEqualTo(new BigDecimal("30.50"));
  }

  @Test
  void returns200WithMappedPriceWhenUseCaseResolvesOneCaseTwoWindows2100pm() {
    var controller = new PriceController(getApplicablePriceUseCase);
    var applicationDate = LocalDateTime.of(2020, 6, 16, 21, 0);
    var price = new Price(1L, applicationDate.minusHours(1), applicationDate.plusHours(1),
        2, 35455L, 1, new BigDecimal("38.95"), "EUR");

    when(getApplicablePriceUseCase.getApplicablePrice(1L, 35455L, applicationDate))
        .thenReturn(price);

    var result = controller.getApplicablePrice(1L, 35455L, applicationDate);

    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(result.getBody()).isNotNull();
    assertThat(result.getBody().priceList()).isEqualTo(2);
    assertThat(result.getBody().price()).isEqualTo(new BigDecimal("38.95"));
  }
}