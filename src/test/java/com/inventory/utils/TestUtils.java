package com.inventory.utils;

import com.inventory.domain.InventoryItem;
import com.inventory.domain.Manufacturer;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

public class TestUtils {
    public static InventoryItem createInventoryItem() {
        InventoryItem item = new InventoryItem();
        item.setId(UUID.randomUUID());
        item.setName(createRandomString());
        item.setReleaseDate(LocalDateTime.now().minusHours(new Random().nextInt(24)));
        item.setManufacturer(createManufacturer());
        return item;
    }

    public static Manufacturer createManufacturer() {
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setId(0L);
        manufacturer.setName(createRandomString());
        return manufacturer;
    }

    public static String createRandomString() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 20);
    }
}
