package com.challenge.pricing.api.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * End-to-end tests against the real Spring context and the H2 in-memory database seeded via
 * data.sql. These are the 5 scenarios required by the exercise, expressed as one parameterised
 * test so the five cases can't drift out of sync with each other, plus a couple of separate
 * tests for the error paths the brief explicitly asks to cover.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class PriceControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest(name = "[Test {index}] {0} -> priceList={1}, price={2}")
    @CsvSource({
            // applicationDate,          expectedPriceList, expectedPrice
            "2020-06-14T10:00:00, 1, 35.50",  // Test 1: before any override -> base tariff
            "2020-06-14T16:00:00, 2, 25.45",  // Test 2: inside the 15:00-18:30 override window
            "2020-06-14T21:00:00, 1, 35.50",  // Test 3: override window has closed -> back to base tariff
            "2020-06-15T10:00:00, 3, 30.50",  // Test 4: inside the day-15 00:00-11:00 window
            "2020-06-16T21:00:00, 4, 38.95",  // Test 5: inside the day-15 16:00 onwards window
    })
    void resolvesApplicablePriceForEachRequiredScenario(String applicationDate, int expectedPriceList, String expectedPrice) throws Exception {
        mockMvc.perform(get("/api/v1/prices/applicable")
                        .param("brandId", "1")
                        .param("productId", "35455")
                        .param("applicationDate", applicationDate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brandId", is(1)))
                .andExpect(jsonPath("$.productId", is(35455)))
                .andExpect(jsonPath("$.priceList", is(expectedPriceList)))
                .andExpect(jsonPath("$.price", is(Double.parseDouble(expectedPrice))))
                .andExpect(jsonPath("$.currency", is("EUR")));
    }

    @Test
    void returns404WhenNoPriceAppliesForTheRequestedDate() throws Exception {
        mockMvc.perform(get("/api/v1/prices/applicable")
                        .param("brandId", "1")
                        .param("productId", "35455")
                        .param("applicationDate", "2019-01-01T00:00:00")) // before any window starts
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title", is("Price not found")));
    }

    @Test
    void returns404ForAnUnknownProduct() throws Exception {
        mockMvc.perform(get("/api/v1/prices/applicable")
                        .param("brandId", "1")
                        .param("productId", "999999")
                        .param("applicationDate", "2020-06-14T10:00:00"))
                .andExpect(status().isNotFound());
    }

    @Test
    void returns400ForMalformedApplicationDate() throws Exception {
        mockMvc.perform(get("/api/v1/prices/applicable")
                        .param("brandId", "1")
                        .param("productId", "35455")
                        .param("applicationDate", "not-a-date"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title", is("Invalid parameter type")));
    }

    @Test
    void returns400WhenRequiredParameterIsMissing() throws Exception {
        mockMvc.perform(get("/api/v1/prices/applicable")
                        .param("brandId", "1")
                        .param("applicationDate", "2020-06-14T10:00:00"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title", is("Missing request parameter")));
    }

    @Test
    void returns400ForNonPositiveBrandId() throws Exception {
        mockMvc.perform(get("/api/v1/prices/applicable")
                        .param("brandId", "-1")
                        .param("productId", "35455")
                        .param("applicationDate", "2020-06-14T10:00:00"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title", is("Invalid request parameters")));
    }
}
