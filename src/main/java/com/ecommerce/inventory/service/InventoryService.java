package com.ecommerce.inventory.service;

import com.ecommerce.inventory.dto.InventoryResponse;
import com.ecommerce.inventory.dto.ReserveStockRequest;
import com.ecommerce.inventory.dto.StockUpdateRequest;
import com.ecommerce.inventory.entity.Inventory;
import com.ecommerce.inventory.entity.InventoryTransaction;
import com.ecommerce.inventory.repository.InventoryRepository;
import com.ecommerce.inventory.repository.InventoryTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private InventoryTransactionRepository transactionRepository;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private static final String INVENTORY_TOPIC = "inventory-updates";

    public InventoryResponse getInventoryByProductId(UUID productId) {
        Inventory inventory = inventoryRepository.findByProductIdAndIsActiveTrue(productId)
                .orElseThrow(() -> new RuntimeException("Inventory not found for product: " + productId));
        return mapToResponse(inventory);
    }

    public List<InventoryResponse> getAllActiveInventory() {
        return inventoryRepository.findByIsActiveTrueOrderByUpdatedAtDesc()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<InventoryResponse> getLowStockItems() {
        return inventoryRepository.findLowStockItems()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<InventoryResponse> getOutOfStockItems() {
        return inventoryRepository.findOutOfStockItems()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public InventoryResponse updateStock(StockUpdateRequest request) {
        Inventory inventory = inventoryRepository.findByProductIdAndIsActiveTrue(request.getProductId())
                .orElseGet(() -> createInitialInventory(request.getProductId()));

        Integer previousQuantity = inventory.getQuantity();
        inventory.setQuantity(request.getQuantity());

        // Create transaction record
        InventoryTransaction transaction = new InventoryTransaction(
                inventory.getId(),
                InventoryTransaction.TransactionType.ADJUSTMENT,
                request.getQuantity() - previousQuantity,
                previousQuantity,
                request.getQuantity());
        transaction.setNotes(request.getNotes());

        inventoryRepository.save(inventory);
        transactionRepository.save(transaction);

        // Publish inventory update event
        publishInventoryUpdate(inventory);

        return mapToResponse(inventory);
    }

    public InventoryResponse addStock(UUID productId, Integer quantity, String notes) {
        Inventory inventory = inventoryRepository.findByProductIdAndIsActiveTrue(productId)
                .orElseGet(() -> createInitialInventory(productId));

        Integer previousQuantity = inventory.getQuantity();
        inventory.setQuantity(inventory.getQuantity() + quantity);

        // Create transaction record
        InventoryTransaction transaction = new InventoryTransaction(
                inventory.getId(),
                InventoryTransaction.TransactionType.INBOUND,
                quantity,
                previousQuantity,
                inventory.getQuantity());
        transaction.setNotes(notes);

        inventoryRepository.save(inventory);
        transactionRepository.save(transaction);

        // Publish inventory update event
        publishInventoryUpdate(inventory);

        return mapToResponse(inventory);
    }

    public InventoryResponse removeStock(UUID productId, Integer quantity, String notes) {
        Inventory inventory = inventoryRepository.findByProductIdAndIsActiveTrue(productId)
                .orElseThrow(() -> new RuntimeException("Inventory not found for product: " + productId));

        if (inventory.getAvailableQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock available. Available: " +
                    inventory.getAvailableQuantity() + ", Requested: " + quantity);
        }

        Integer previousQuantity = inventory.getQuantity();
        inventory.setQuantity(inventory.getQuantity() - quantity);

        // Create transaction record
        InventoryTransaction transaction = new InventoryTransaction(
                inventory.getId(),
                InventoryTransaction.TransactionType.OUTBOUND,
                -quantity,
                previousQuantity,
                inventory.getQuantity());
        transaction.setNotes(notes);

        inventoryRepository.save(inventory);
        transactionRepository.save(transaction);

        // Publish inventory update event
        publishInventoryUpdate(inventory);

        return mapToResponse(inventory);
    }

    public boolean reserveStock(ReserveStockRequest request) {
        Inventory inventory = inventoryRepository.findByProductIdAndIsActiveTrue(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Inventory not found for product: " + request.getProductId()));

        if (!inventory.canReserve(request.getQuantity())) {
            return false;
        }

        Integer previousReserved = inventory.getReservedQuantity();
        inventory.setReservedQuantity(inventory.getReservedQuantity() + request.getQuantity());

        // Create transaction record
        InventoryTransaction transaction = new InventoryTransaction(
                inventory.getId(),
                InventoryTransaction.TransactionType.RESERVED,
                request.getQuantity(),
                inventory.getQuantity() - previousReserved,
                inventory.getAvailableQuantity());
        transaction.setReferenceId(request.getReferenceId());
        transaction.setReferenceType(request.getReferenceType());

        inventoryRepository.save(inventory);
        transactionRepository.save(transaction);

        // Publish inventory update event
        publishInventoryUpdate(inventory);

        return true;
    }

    public void releaseReservedStock(UUID productId, Integer quantity, UUID referenceId, String referenceType) {
        Inventory inventory = inventoryRepository.findByProductIdAndIsActiveTrue(productId)
                .orElseThrow(() -> new RuntimeException("Inventory not found for product: " + productId));

        if (inventory.getReservedQuantity() < quantity) {
            throw new RuntimeException("Cannot release more stock than reserved");
        }

        Integer previousReserved = inventory.getReservedQuantity();
        inventory.setReservedQuantity(inventory.getReservedQuantity() - quantity);

        // Create transaction record
        InventoryTransaction transaction = new InventoryTransaction(
                inventory.getId(),
                InventoryTransaction.TransactionType.RELEASED,
                -quantity,
                inventory.getQuantity() - previousReserved,
                inventory.getAvailableQuantity());
        transaction.setReferenceId(referenceId);
        transaction.setReferenceType(referenceType);

        inventoryRepository.save(inventory);
        transactionRepository.save(transaction);

        // Publish inventory update event
        publishInventoryUpdate(inventory);
    }

    public void fulfillReservedStock(UUID productId, Integer quantity, UUID referenceId, String referenceType) {
        Inventory inventory = inventoryRepository.findByProductIdAndIsActiveTrue(productId)
                .orElseThrow(() -> new RuntimeException("Inventory not found for product: " + productId));

        if (inventory.getReservedQuantity() < quantity) {
            throw new RuntimeException("Cannot fulfill more stock than reserved");
        }

        Integer previousQuantity = inventory.getQuantity();

        inventory.setQuantity(inventory.getQuantity() - quantity);
        inventory.setReservedQuantity(inventory.getReservedQuantity() - quantity);

        // Create transaction record
        InventoryTransaction transaction = new InventoryTransaction(
                inventory.getId(),
                InventoryTransaction.TransactionType.OUTBOUND,
                -quantity,
                previousQuantity,
                inventory.getQuantity());
        transaction.setReferenceId(referenceId);
        transaction.setReferenceType(referenceType);

        inventoryRepository.save(inventory);
        transactionRepository.save(transaction);

        // Publish inventory update event
        publishInventoryUpdate(inventory);
    }

    private Inventory createInitialInventory(UUID productId) {
        Inventory inventory = new Inventory();
        inventory.setProductId(productId);
        inventory.setQuantity(0);
        inventory.setReservedQuantity(0);
        inventory.setReorderLevel(10);
        inventory.setMaxStockLevel(1000);
        return inventory;
    }

    private void publishInventoryUpdate(Inventory inventory) {
        try {
            InventoryResponse response = mapToResponse(inventory);
            kafkaTemplate.send(INVENTORY_TOPIC, response);
        } catch (Exception e) {
            // Log error but don't fail the transaction
            System.err.println("Failed to publish inventory update: " + e.getMessage());
        }
    }

    private InventoryResponse mapToResponse(Inventory inventory) {
        InventoryResponse response = new InventoryResponse();
        response.setId(inventory.getId());
        response.setProductId(inventory.getProductId());
        response.setQuantity(inventory.getQuantity());
        response.setReservedQuantity(inventory.getReservedQuantity());
        response.setAvailableQuantity(inventory.getAvailableQuantity());
        response.setReorderLevel(inventory.getReorderLevel());
        response.setMaxStockLevel(inventory.getMaxStockLevel());
        response.setIsLowStock(inventory.isLowStock());
        response.setIsOutOfStock(inventory.isOutOfStock());
        response.setIsActive(inventory.getIsActive());
        response.setCreatedAt(inventory.getCreatedAt());
        response.setUpdatedAt(inventory.getUpdatedAt());
        return response;
    }
}