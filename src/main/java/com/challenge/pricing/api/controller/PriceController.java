package com.challenge.pricing.api.controller;

import com.challenge.pricing.api.GetApplicablePriceUseCase;
import com.challenge.pricing.api.controller.response.PriceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;


/**
 * Prices controller that exposes endpoints to retrieve applicable prices for products based on brand and application date.
 */
@Validated
@RestController
@RequestMapping("/api/v1")
public class PriceController {

  private final GetApplicablePriceUseCase getApplicablePriceUseCase;

  public PriceController(GetApplicablePriceUseCase getApplicablePriceUseCase) {
    this.getApplicablePriceUseCase = getApplicablePriceUseCase;
  }

  @Operation(summary = "Resolve the applicable price for a product, brand and point in time")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Applicable price found"),
      @ApiResponse(responseCode = "400", description = "Invalid or missing request parameters"),
      @ApiResponse(responseCode = "404", description = "No price applies for the given combination")
  })
  @GetMapping("/prices/applicable")
  public ResponseEntity<PriceResponse> getApplicablePrice(
      @Parameter(description = "Brand/chain identifier", example = "1")
      @RequestParam @NotNull @Positive Long brandId,

      @Parameter(description = "Product identifier", example = "35455")
      @RequestParam @NotNull @Positive Long productId,

      @Parameter(description = "Point in time the price must apply at, ISO-8601", example = "2020-06-14T16:00:00")
      @RequestParam @NotNull
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime applicationDate
  ) {
    var price = getApplicablePriceUseCase.getApplicablePrice(brandId, productId, applicationDate);
    return ResponseEntity.ok(PriceResponse.fromDomain(price));
  }
}
