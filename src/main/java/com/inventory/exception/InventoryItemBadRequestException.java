package com.inventory.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InventoryItemBadRequestException extends RuntimeException {
    public InventoryItemBadRequestException(String errorMessage) {
        super(errorMessage);
    }
}
