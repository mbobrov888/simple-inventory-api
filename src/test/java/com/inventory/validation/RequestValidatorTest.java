package com.inventory.validation;

import com.inventory.exception.InventoryItemBadRequestException;
import org.junit.jupiter.api.Test;

import static com.inventory.Constants.API_QUERY_PARAM_LIMIT;
import static com.inventory.Constants.API_QUERY_PARAM_OFFSET;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RequestValidatorTest {

    @Test
    void testIsValidRequestParameters_success() {
        RequestValidator.isValidRequestParameters(20, 40);
    }

    @Test
    void testIsValidRequestParameters_badRequestExceptionOffsetBelowMin() {
        InventoryItemBadRequestException thrown = assertThrows(
                InventoryItemBadRequestException.class,
                () -> RequestValidator.isValidRequestParameters(-1, 40),
                "Expected InventoryItemNotFoundException, but it's never thrown"
        );

        assertEquals("Request parameters " + API_QUERY_PARAM_OFFSET + " and/or " +
                API_QUERY_PARAM_LIMIT + " are invalid", thrown.getMessage());
    }

    @Test
    void testIsValidRequestParameters_badRequestExceptionLimitOverMax() {
        InventoryItemBadRequestException thrown = assertThrows(
                InventoryItemBadRequestException.class,
                () -> RequestValidator.isValidRequestParameters(20, 100),
                "Expected InventoryItemNotFoundException, but it's never thrown"
        );

        assertEquals("Request parameters " + API_QUERY_PARAM_OFFSET + " and/or " +
                API_QUERY_PARAM_LIMIT + " are invalid", thrown.getMessage());
    }

    @Test
    void testIsValidRequestParameters_badRequestExceptionLimitBelowMin() {
        InventoryItemBadRequestException thrown = assertThrows(
                InventoryItemBadRequestException.class,
                () -> RequestValidator.isValidRequestParameters(20, -1),
                "Expected InventoryItemNotFoundException, but it's never thrown"
        );

        assertEquals("Request parameters " + API_QUERY_PARAM_OFFSET + " and/or " +
                API_QUERY_PARAM_LIMIT + " are invalid", thrown.getMessage());
    }
}
