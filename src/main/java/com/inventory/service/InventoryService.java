package com.inventory.service;

import com.inventory.dto.InventoryItemDto;

import java.util.List;

public interface InventoryService {
    List<InventoryItemDto> findItems(int offset, int limit);
    InventoryItemDto getItem(String uuid);
    InventoryItemDto createItem(InventoryItemDto newItem);
}
