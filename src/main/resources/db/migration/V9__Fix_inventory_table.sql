-- V9__Fix_inventory_table.sql

-- Add missing columns to inventory table to match Inventory entity
ALTER TABLE inventory ADD COLUMN is_active BOOLEAN NOT NULL DEFAULT true;

-- Rename columns to match entity expectations
ALTER TABLE inventory RENAME COLUMN quantity_available TO quantity;
ALTER TABLE inventory RENAME COLUMN quantity_reserved TO reserved_quantity;
ALTER TABLE inventory RENAME COLUMN reorder_point TO reorder_level;

-- The entity expects these columns but the migration has different names
-- Adding indexes for the new column
CREATE INDEX idx_inventory_is_active ON inventory(is_active);