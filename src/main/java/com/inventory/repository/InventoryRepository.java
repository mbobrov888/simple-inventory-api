package com.inventory.repository;

import com.inventory.domain.InventoryItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryItem, UUID> {
    @Override
    Page<InventoryItem> findAll(Pageable pageable);
    InventoryItem findByName(String name);
}