package com.inventory.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "INVENTORY_ITEM")
@Getter
@Setter
public class InventoryItem {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    @Column(name = "release_date")
    private LocalDateTime releaseDate;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "manufacturer_id")
    private Manufacturer manufacturer;
}
