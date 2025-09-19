package com.ecommerce.inventory.repository;

import com.ecommerce.inventory.entity.InventoryTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, UUID> {

    List<InventoryTransaction> findByInventoryIdOrderByCreatedAtDesc(UUID inventoryId);

    Page<InventoryTransaction> findByInventoryIdOrderByCreatedAtDesc(UUID inventoryId, Pageable pageable);

    List<InventoryTransaction> findByTransactionTypeOrderByCreatedAtDesc(
            InventoryTransaction.TransactionType transactionType);

    @Query("SELECT it FROM InventoryTransaction it WHERE it.referenceId = :referenceId " +
            "AND it.referenceType = :referenceType ORDER BY it.createdAt DESC")
    List<InventoryTransaction> findByReferenceIdAndReferenceType(
            @Param("referenceId") UUID referenceId,
            @Param("referenceType") String referenceType);

    @Query("SELECT it FROM InventoryTransaction it WHERE it.createdAt BETWEEN :startDate AND :endDate " +
            "ORDER BY it.createdAt DESC")
    List<InventoryTransaction> findByCreatedAtBetween(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}