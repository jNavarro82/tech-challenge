package com.challenge.pricing.infrastructure.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * JPA representation of the PRICES table.
 * <p>
 * Assumption: the original spec's sample table has no explicit primary key, so a surrogate
 * BIGINT identity column is added here - it doesn't change query semantics or the business rule.
 */
@Entity
@Table(name = "PRICES", indexes = {
        @jakarta.persistence.Index(
                name = "idx_prices_lookup",
                columnList = "BRAND_ID, PRODUCT_ID, START_DATE, END_DATE"
        )
})
public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "BRAND_ID", nullable = false)
    private Long brandId;

    @Column(name = "START_DATE", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "END_DATE", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "PRICE_LIST", nullable = false)
    private Integer priceList;

    @Column(name = "PRODUCT_ID", nullable = false)
    private Long productId;

    @Column(name = "PRIORITY", nullable = false)
    private Integer priority;

    @Column(name = "PRICE", nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "CURR", nullable = false, length = 3)
    private String currency;

    protected Price() {
        // required by JPA
    }

    public Price(Long brandId, LocalDateTime startDate, LocalDateTime endDate,
                           Integer priceList, Long productId, Integer priority,
                           BigDecimal price, String currency) {
        this.brandId = brandId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.priceList = priceList;
        this.productId = productId;
        this.priority = priority;
        this.price = price;
        this.currency = currency;
    }

    public Long getId() {
        return id;
    }

    public Long getBrandId() {
        return brandId;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public Integer getPriceList() {
        return priceList;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getPriority() {
        return priority;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }
}
