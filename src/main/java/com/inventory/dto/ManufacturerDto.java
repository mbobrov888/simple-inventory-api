package com.inventory.dto;

import com.inventory.domain.Manufacturer;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Getter
public class ManufacturerDto {
    private Long id = 0L;
    @NotEmpty
    private String name;
    private String homePage;
    private String phone;

    public ManufacturerDto() {}

    public ManufacturerDto(Manufacturer manufacturer) {
        id = manufacturer.getId();
        name = manufacturer.getName();
        homePage = manufacturer.getHomePage();
        phone = manufacturer.getPhone();
    }
}
