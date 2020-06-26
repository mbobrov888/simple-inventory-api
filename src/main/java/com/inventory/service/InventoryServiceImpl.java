package com.inventory.service;

import com.inventory.domain.InventoryItem;
import com.inventory.domain.Manufacturer;
import com.inventory.dto.InventoryItemDto;
import com.inventory.exception.InventoryItemBadRequestException;
import com.inventory.exception.InventoryItemExistsException;
import com.inventory.exception.InventoryItemNotFoundException;
import com.inventory.repository.InventoryRepository;
import com.inventory.repository.ManufacturerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ManufacturerRepository manufacturerRepository;

    public InventoryServiceImpl(InventoryRepository inventoryRepository,
                                ManufacturerRepository manufacturerRepository) {
        this.inventoryRepository = inventoryRepository;
        this.manufacturerRepository = manufacturerRepository;
    }

    public List<InventoryItemDto> findItems(int offset, int limit) {
        Page<InventoryItem> items = inventoryRepository.findAll(PageRequest.of(offset, limit));
        return items.stream().map(InventoryItemDto::new).collect(Collectors.toList());
    }

    public InventoryItemDto getItem(String id) {
        return new InventoryItemDto(inventoryRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new InventoryItemNotFoundException("Inventory Item with ID " + id + " not found")));
    }

    public InventoryItemDto createItem(InventoryItemDto newItem) {
        if (newItem.getManufacturer() == null) {
            throw new InventoryItemBadRequestException("Manufacturer must be provided when creating new inventory item");
        }
        Manufacturer manufacturer = newItem.getManufacturer().getId() > 0L ?
                manufacturerRepository.findById(newItem.getManufacturer().getId()).orElseThrow(
                        () -> new InventoryItemBadRequestException(
                                "Manufacturer with ID " + newItem.getManufacturer().getId() + " does not exist")) :
                manufacturerRepository.findByName(newItem.getManufacturer().getName());
        if (manufacturer == null) {
            throw new InventoryItemBadRequestException("Manufacturer with name "+ newItem.getManufacturer().getName() +
                    " does not exist");
        }
        if (inventoryRepository.findByName(newItem.getName()) != null) {
            throw new InventoryItemExistsException("Inventory item with name " + newItem.getName() + " already exists");
        }
        InventoryItem item = new InventoryItem();
        item.setName(newItem.getName());
        item.setReleaseDate(newItem.getReleaseDate());
        item.setManufacturer(manufacturer);
        item = inventoryRepository.save(item);
        return new InventoryItemDto(item);
    }
}
