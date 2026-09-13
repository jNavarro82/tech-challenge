package com.challenge.pricing.api.controller.response;

import com.challenge.pricing.domain.model.Price;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Applicable price response for a product.
 */
@Schema(description = "Applicable price for a product, brand and point in time")
public record PriceResponse(
        @Schema(description = "Product identifier", example = "35455") Long productId,
        @Schema(description = "Brand/chain identifier", example = "1") Long brandId,
        @Schema(description = "Identifier of the price list (tariff) applied", example = "2") Integer priceList,
        @Schema(description = "Start of the applied tariff's validity window") LocalDateTime startDate,
        @Schema(description = "End of the applied tariff's validity window") LocalDateTime endDate,
        @Schema(description = "Final price to apply", example = "25.45") BigDecimal price,
        @Schema(description = "ISO 4217 currency code", example = "EUR") String currency
) {
    public static PriceResponse fromDomain(Price price) {
        return new PriceResponse(
                price.productId(),
                price.brandId(),
                price.priceList(),
                price.startDate(),
                price.endDate(),
                price.amount(),
                price.currency()
        );
    }
}
