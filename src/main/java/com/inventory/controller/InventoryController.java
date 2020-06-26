package com.inventory.controller;

import com.inventory.Constants;
import com.inventory.dto.InventoryItemDto;
import com.inventory.exception.InventoryItemBadRequestException;
import com.inventory.service.InventoryService;
import com.inventory.validation.RequestValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class InventoryController implements Constants {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @RequestMapping(path = API_PATH_INVENTORY, produces = {API_JSON_RESPONSE}, method = RequestMethod.GET)
    public ResponseEntity<List<InventoryItemDto>> getInventoryItems(
            @RequestParam(name = API_QUERY_PARAM_OFFSET, required = false, defaultValue = "" + OFFSET_MINIMUM) Integer offset,
            @RequestParam(name = API_QUERY_PARAM_LIMIT, required = false, defaultValue = "" + LIMIT_MAXIMUM) Integer limit) {
        RequestValidator.isValidRequestParameters(offset, limit);
        return ResponseEntity.ok(inventoryService.findItems(offset, limit));
    }

    @RequestMapping(path = API_PATH_INVENTORY, produces = {API_JSON_RESPONSE}, method = RequestMethod.POST)
    public ResponseEntity<InventoryItemDto> createInventoryItem(@Valid @RequestBody InventoryItemDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inventoryService.createItem(dto));
    }

    @RequestMapping(path = API_PATH_INVENTORY_BY_ID, produces = {API_JSON_RESPONSE}, method = RequestMethod.GET)
    public ResponseEntity<InventoryItemDto> getInventoryItem(@PathVariable(API_PATH_PARAM_ID) String uuid) {
        return ResponseEntity.ok(inventoryService.getItem(uuid));
    }
}
