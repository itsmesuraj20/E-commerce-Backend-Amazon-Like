package com.ecommerce.inventory.repository;

import com.ecommerce.inventory.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, UUID> {
    
    Optional<Inventory> findByProductIdAndIsActiveTrue(UUID productId);
    
    List<Inventory> findByIsActiveTrueOrderByUpdatedAtDesc();
    
    @Query("SELECT i FROM Inventory i WHERE i.isActive = true AND " +
           "(i.quantity - i.reservedQuantity) <= i.reorderLevel")
    List<Inventory> findLowStockItems();
    
    @Query("SELECT i FROM Inventory i WHERE i.isActive = true AND " +
           "(i.quantity - i.reservedQuantity) <= 0")
    List<Inventory> findOutOfStockItems();
    
    @Query("SELECT i FROM Inventory i WHERE i.productId IN :productIds AND i.isActive = true")
    List<Inventory> findByProductIdsAndIsActiveTrue(@Param("productIds") List<UUID> productIds);
    
    @Query("SELECT SUM(i.quantity) FROM Inventory i WHERE i.isActive = true")
    Long getTotalInventoryCount();
    
    @Query("SELECT SUM(i.reservedQuantity) FROM Inventory i WHERE i.isActive = true")
    Long getTotalReservedCount();
}