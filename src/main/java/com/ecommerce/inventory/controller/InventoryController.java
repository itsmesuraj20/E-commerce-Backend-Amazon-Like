package com.ecommerce.inventory.controller;

import com.ecommerce.inventory.dto.InventoryResponse;
import com.ecommerce.inventory.dto.ReserveStockRequest;
import com.ecommerce.inventory.dto.StockUpdateRequest;
import com.ecommerce.inventory.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/inventory")
@Tag(name = "Inventory Management", description = "Stock management operations")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/{productId}")
    @Operation(summary = "Get inventory for a product")
    public ResponseEntity<InventoryResponse> getInventoryByProductId(@PathVariable UUID productId) {
        InventoryResponse inventory = inventoryService.getInventoryByProductId(productId);
        return ResponseEntity.ok(inventory);
    }

    @GetMapping
    @Operation(summary = "Get all active inventory")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<InventoryResponse>> getAllActiveInventory() {
        List<InventoryResponse> inventory = inventoryService.getAllActiveInventory();
        return ResponseEntity.ok(inventory);
    }

    @GetMapping("/low-stock")
    @Operation(summary = "Get low stock items")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<InventoryResponse>> getLowStockItems() {
        List<InventoryResponse> inventory = inventoryService.getLowStockItems();
        return ResponseEntity.ok(inventory);
    }

    @GetMapping("/out-of-stock")
    @Operation(summary = "Get out of stock items")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<InventoryResponse>> getOutOfStockItems() {
        List<InventoryResponse> inventory = inventoryService.getOutOfStockItems();
        return ResponseEntity.ok(inventory);
    }

    @PutMapping("/update")
    @Operation(summary = "Update stock quantity")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InventoryResponse> updateStock(@Valid @RequestBody StockUpdateRequest request) {
        InventoryResponse inventory = inventoryService.updateStock(request);
        return ResponseEntity.ok(inventory);
    }

    @PostMapping("/add/{productId}")
    @Operation(summary = "Add stock to product")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InventoryResponse> addStock(
            @PathVariable UUID productId,
            @RequestParam Integer quantity,
            @RequestParam(required = false) String notes) {
        InventoryResponse inventory = inventoryService.addStock(productId, quantity, notes);
        return ResponseEntity.ok(inventory);
    }

    @PostMapping("/remove/{productId}")
    @Operation(summary = "Remove stock from product")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InventoryResponse> removeStock(
            @PathVariable UUID productId,
            @RequestParam Integer quantity,
            @RequestParam(required = false) String notes) {
        InventoryResponse inventory = inventoryService.removeStock(productId, quantity, notes);
        return ResponseEntity.ok(inventory);
    }

    @PostMapping("/reserve")
    @Operation(summary = "Reserve stock for an order")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<String> reserveStock(@Valid @RequestBody ReserveStockRequest request) {
        boolean success = inventoryService.reserveStock(request);
        if (success) {
            return ResponseEntity.ok("Stock reserved successfully");
        } else {
            return ResponseEntity.badRequest().body("Insufficient stock available");
        }
    }

    @PostMapping("/release/{productId}")
    @Operation(summary = "Release reserved stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> releaseReservedStock(
            @PathVariable UUID productId,
            @RequestParam Integer quantity,
            @RequestParam UUID referenceId,
            @RequestParam String referenceType) {
        inventoryService.releaseReservedStock(productId, quantity, referenceId, referenceType);
        return ResponseEntity.ok("Reserved stock released successfully");
    }

    @PostMapping("/fulfill/{productId}")
    @Operation(summary = "Fulfill reserved stock (complete order)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> fulfillReservedStock(
            @PathVariable UUID productId,
            @RequestParam Integer quantity,
            @RequestParam UUID referenceId,
            @RequestParam String referenceType) {
        inventoryService.fulfillReservedStock(productId, quantity, referenceId, referenceType);
        return ResponseEntity.ok("Reserved stock fulfilled successfully");
    }
}