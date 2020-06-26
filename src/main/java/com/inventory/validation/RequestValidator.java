package com.inventory.validation;

import com.inventory.Constants;
import com.inventory.exception.InventoryItemBadRequestException;

public class RequestValidator implements Constants {

    public static void isValidRequestParameters(int offset, int limit) {
        if (offset < OFFSET_MINIMUM || limit < LIMIT_MINIMUM || limit > LIMIT_MAXIMUM) {
            throw new InventoryItemBadRequestException("Request parameters " + API_QUERY_PARAM_OFFSET +
                    " and/or " + API_QUERY_PARAM_LIMIT + " are invalid");
        }
    }
}
