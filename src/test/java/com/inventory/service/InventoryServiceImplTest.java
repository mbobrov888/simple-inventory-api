package com.inventory.service;

import com.inventory.domain.InventoryItem;
import com.inventory.domain.Manufacturer;
import com.inventory.dto.InventoryItemDto;
import com.inventory.exception.InventoryItemBadRequestException;
import com.inventory.exception.InventoryItemExistsException;
import com.inventory.exception.InventoryItemNotFoundException;
import com.inventory.repository.InventoryRepository;
import com.inventory.repository.ManufacturerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static com.inventory.utils.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceImplTest {
    @Mock private InventoryRepository inventoryRepository;
    @Mock private ManufacturerRepository manufacturerRepository;
    @InjectMocks private InventoryServiceImpl inventoryService;

    @Test
    void testFindItems() {
        InventoryItem item1 = createInventoryItem();
        InventoryItem item2 = createInventoryItem();
        when(inventoryRepository.findAll(any(Pageable.class))).thenReturn(
                new PageImpl<>(new ArrayList<>() {{
                    add(item1);
                    add(item2);
                }})
        );

        List<InventoryItemDto> result = inventoryService.findItems(0, 50);

        assertEquals(2, result.size());
        assertEquals(item1.getId(), result.get(0).getId());
        assertEquals(item1.getName(), result.get(0).getName());
        assertEquals(item1.getReleaseDate(), result.get(0).getReleaseDate());
        assertEquals(item1.getManufacturer().getName(), result.get(0).getManufacturer().getName());
        assertEquals(item2.getId(), result.get(1).getId());
        assertEquals(item2.getName(), result.get(1).getName());
        assertEquals(item2.getReleaseDate(), result.get(1).getReleaseDate());
        assertEquals(item2.getManufacturer().getName(), result.get(1).getManufacturer().getName());
    }

    @Test
    void testGetItem_success() {
        InventoryItem item = createInventoryItem();

        when(inventoryRepository.findById(any(UUID.class))).thenReturn(Optional.of(item));

        InventoryItemDto result = inventoryService.getItem(UUID.randomUUID().toString());

        assertEquals(item.getId(), result.getId());
        assertEquals(item.getName(), result.getName());
        assertEquals(item.getReleaseDate(), result.getReleaseDate());
        assertEquals(item.getManufacturer().getName(), result.getManufacturer().getName());
    }

    @Test
    void testGetItem_notFound() {
        String uuid = UUID.randomUUID().toString();
        when(inventoryRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        InventoryItemNotFoundException thrown = assertThrows(
            InventoryItemNotFoundException.class,
            () -> inventoryService.getItem(uuid),
            "Expected InventoryItemNotFoundException, but it's never thrown"
        );

        assertEquals("Inventory Item with ID " + uuid + " not found", thrown.getMessage());
    }

    @Test
    void testCreateItem_manufacturerNotFoundById() {
        InventoryItem item = createInventoryItem();
        item.getManufacturer().setId(25L);
        InventoryItemDto dto = new InventoryItemDto(item);

        when(manufacturerRepository.findById(eq(25L))).thenReturn(Optional.empty());

        InventoryItemBadRequestException thrown = assertThrows(
                InventoryItemBadRequestException.class,
                () -> inventoryService.createItem(dto),
                "Expected InventoryItemCreateException about not existing Manufacturer, but it's never thrown"
        );

        assertEquals("Manufacturer with ID 25 does not exist", thrown.getMessage());
    }

    @Test
    void testCreateItem_manufacturerNotFoundByName() {
        InventoryItem item = createInventoryItem();
        InventoryItemDto dto = new InventoryItemDto(item);

        when(manufacturerRepository.findByName(eq(dto.getManufacturer().getName()))).thenReturn(null);

        InventoryItemBadRequestException thrown = assertThrows(
                InventoryItemBadRequestException.class,
                () -> inventoryService.createItem(dto),
                "Expected InventoryItemCreateException about not existing Manufacturer, but it's never thrown"
        );

        assertEquals("Manufacturer with name " + dto.getManufacturer().getName() + " does not exist",
                thrown.getMessage());
    }

    @Test
    void testCreateItem_inventoryItemAlreadyExists() {
        InventoryItem item = createInventoryItem();
        InventoryItemDto dto = new InventoryItemDto(item);
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setName(item.getManufacturer().getName());
        manufacturer.setId(49L);

        when(manufacturerRepository.findByName(eq(dto.getManufacturer().getName()))).thenReturn(manufacturer);
        when(inventoryRepository.findByName(eq(item.getName()))).thenReturn(item);

        InventoryItemExistsException thrown = assertThrows(
                InventoryItemExistsException.class,
                () -> inventoryService.createItem(dto),
                "Expected InventoryItemExistsException for already existing Item, but it's never thrown"
        );

        assertEquals("Inventory item with name " + item.getName() + " already exists",
                thrown.getMessage());
    }

    @Test
    void testCreateItem_success() {
        InventoryItem item = createInventoryItem();
        InventoryItem result = createInventoryItem();
        InventoryItemDto dto = new InventoryItemDto(item);
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setName(item.getManufacturer().getName());
        manufacturer.setId(49L);

        when(manufacturerRepository.findByName(eq(dto.getManufacturer().getName()))).thenReturn(manufacturer);
        when(inventoryRepository.findByName(eq(item.getName()))).thenReturn(null);
        when(inventoryRepository.save(any(InventoryItem.class))).thenReturn(result);

        InventoryItemDto dtoResult = inventoryService.createItem(dto);

        assertEquals(result.getId(), dtoResult.getId());
        assertEquals(result.getName(), dtoResult.getName());
        assertEquals(result.getReleaseDate(), dtoResult.getReleaseDate());
        assertEquals(result.getManufacturer().getName(), dtoResult.getManufacturer().getName());
    }
}
