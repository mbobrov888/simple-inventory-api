package com.inventory.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InventoryItemNotFoundException extends RuntimeException {
    public InventoryItemNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
