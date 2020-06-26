package com.inventory.dto;

import com.inventory.domain.InventoryItem;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class InventoryItemDto {
    private UUID id;
    @NotEmpty
    private String name;
    @Past
    private LocalDateTime releaseDate;
    private ManufacturerDto manufacturer;

    public InventoryItemDto() {}

    public InventoryItemDto(InventoryItem item) {
        id = item.getId();
        name = item.getName();
        releaseDate = item.getReleaseDate();
        manufacturer = new ManufacturerDto(item.getManufacturer());
    }
}
