-- V3__Create_inventory_table.sql

-- Inventory table
CREATE TABLE inventory (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    product_id UUID NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    warehouse_location VARCHAR(100) DEFAULT 'MAIN',
    quantity_available INTEGER NOT NULL DEFAULT 0,
    quantity_reserved INTEGER NOT NULL DEFAULT 0,
    quantity_sold INTEGER NOT NULL DEFAULT 0,
    reorder_point INTEGER DEFAULT 10,
    max_stock_level INTEGER DEFAULT 1000,
    cost_per_unit DECIMAL(10,2),
    last_restocked_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT positive_quantities CHECK (
        quantity_available >= 0 AND 
        quantity_reserved >= 0 AND 
        quantity_sold >= 0
    )
);

CREATE UNIQUE INDEX idx_inventory_product_warehouse ON inventory(product_id, warehouse_location);
CREATE INDEX idx_inventory_quantity_available ON inventory(quantity_available);
CREATE INDEX idx_inventory_reorder_point ON inventory(reorder_point);

-- Inventory movements table (for tracking stock changes)
CREATE TABLE inventory_movements (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    inventory_id UUID NOT NULL REFERENCES inventory(id) ON DELETE CASCADE,
    movement_type VARCHAR(20) NOT NULL, -- INBOUND, OUTBOUND, ADJUSTMENT, RESERVED, RELEASED
    quantity INTEGER NOT NULL,
    reason VARCHAR(255),
    reference_id UUID, -- Can reference order_id, adjustment_id, etc.
    reference_type VARCHAR(50), -- ORDER, ADJUSTMENT, RESTOCK, etc.
    performed_by UUID REFERENCES users(id),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_inventory_movements_inventory_id ON inventory_movements(inventory_id);
CREATE INDEX idx_inventory_movements_type ON inventory_movements(movement_type);
CREATE INDEX idx_inventory_movements_reference ON inventory_movements(reference_id, reference_type);
CREATE INDEX idx_inventory_movements_created_at ON inventory_movements(created_at);