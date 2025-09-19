-- V6__Insert_seed_data.sql

-- Insert default admin user (password: admin123)
INSERT INTO users (id, username, email, password, first_name, last_name, role, is_active, email_verified) VALUES
('550e8400-e29b-41d4-a716-446655440000', 'admin', 'admin@ecommerce.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye.Jjt.1R8p1V3VBf5zB8Q9f5G5Tqs5OK', 'Admin', 'User', 'ADMIN', true, true);

-- Insert sample categories
INSERT INTO categories (id, name, description, slug, is_active, sort_order) VALUES
('550e8400-e29b-41d4-a716-446655440001', 'Electronics', 'Electronic devices and accessories', 'electronics', true, 1),
('550e8400-e29b-41d4-a716-446655440002', 'Clothing', 'Fashion and apparel', 'clothing', true, 2),
('550e8400-e29b-41d4-a716-446655440003', 'Home & Garden', 'Home improvement and garden supplies', 'home-garden', true, 3),
('550e8400-e29b-41d4-a716-446655440004', 'Sports', 'Sports and outdoor equipment', 'sports', true, 4);

-- Insert subcategories
INSERT INTO categories (id, name, description, parent_id, slug, is_active, sort_order) VALUES
('550e8400-e29b-41d4-a716-446655440011', 'Smartphones', 'Mobile phones and accessories', '550e8400-e29b-41d4-a716-446655440001', 'smartphones', true, 1),
('550e8400-e29b-41d4-a716-446655440012', 'Laptops', 'Laptop computers', '550e8400-e29b-41d4-a716-446655440001', 'laptops', true, 2),
('550e8400-e29b-41d4-a716-446655440013', 'Men''s Clothing', 'Clothing for men', '550e8400-e29b-41d4-a716-446655440002', 'mens-clothing', true, 1),
('550e8400-e29b-41d4-a716-446655440014', 'Women''s Clothing', 'Clothing for women', '550e8400-e29b-41d4-a716-446655440002', 'womens-clothing', true, 2);

-- Insert sample brands
INSERT INTO brands (id, name, description, is_active) VALUES
('550e8400-e29b-41d4-a716-446655440021', 'Apple', 'Premium technology products', true),
('550e8400-e29b-41d4-a716-446655440022', 'Samsung', 'Electronics and mobile devices', true),
('550e8400-e29b-41d4-a716-446655440023', 'Nike', 'Sports and athletic wear', true),
('550e8400-e29b-41d4-a716-446655440024', 'Adidas', 'Sports apparel and equipment', true);

-- Insert sample products
INSERT INTO products (id, name, description, short_description, sku, slug, category_id, brand_id, price, compare_price, is_active, is_featured, track_inventory) VALUES
('550e8400-e29b-41d4-a716-446655440031', 'iPhone 15 Pro', 'Latest iPhone with advanced features', 'Premium smartphone with Pro camera system', 'IPH15PRO001', 'iphone-15-pro', '550e8400-e29b-41d4-a716-446655440011', '550e8400-e29b-41d4-a716-446655440021', 999.99, 1099.99, true, true, true),
('550e8400-e29b-41d4-a716-446655440032', 'Samsung Galaxy S23', 'High-performance Android smartphone', 'Flagship Samsung smartphone', 'SGS23001', 'samsung-galaxy-s23', '550e8400-e29b-41d4-a716-446655440011', '550e8400-e29b-41d4-a716-446655440022', 799.99, 899.99, true, true, true),
('550e8400-e29b-41d4-a716-446655440033', 'MacBook Pro 14"', 'Professional laptop for creative work', 'M3 chip with exceptional performance', 'MBP14M3001', 'macbook-pro-14', '550e8400-e29b-41d4-a716-446655440012', '550e8400-e29b-41d4-a716-446655440021', 1999.99, 2199.99, true, true, true),
('550e8400-e29b-41d4-a716-446655440034', 'Nike Air Max 270', 'Comfortable running shoes', 'Lightweight and breathable athletic shoes', 'NAM270001', 'nike-air-max-270', '550e8400-e29b-41d4-a716-446655440004', '550e8400-e29b-41d4-a716-446655440023', 129.99, 149.99, true, false, true);

-- Insert inventory for products
INSERT INTO inventory (id, product_id, quantity_available, reorder_point, max_stock_level, cost_per_unit) VALUES
('550e8400-e29b-41d4-a716-446655440041', '550e8400-e29b-41d4-a716-446655440031', 50, 10, 100, 750.00),
('550e8400-e29b-41d4-a716-446655440042', '550e8400-e29b-41d4-a716-446655440032', 75, 15, 150, 600.00),
('550e8400-e29b-41d4-a716-446655440043', '550e8400-e29b-41d4-a716-446655440033', 25, 5, 50, 1500.00),
('550e8400-e29b-41d4-a716-446655440044', '550e8400-e29b-41d4-a716-446655440034', 200, 20, 500, 80.00);

-- Insert product images
INSERT INTO product_images (id, product_id, image_url, alt_text, sort_order, is_primary) VALUES
('550e8400-e29b-41d4-a716-446655440051', '550e8400-e29b-41d4-a716-446655440031', '/images/products/iphone-15-pro-1.jpg', 'iPhone 15 Pro front view', 1, true),
('550e8400-e29b-41d4-a716-446655440052', '550e8400-e29b-41d4-a716-446655440031', '/images/products/iphone-15-pro-2.jpg', 'iPhone 15 Pro back view', 2, false),
('550e8400-e29b-41d4-a716-446655440053', '550e8400-e29b-41d4-a716-446655440032', '/images/products/samsung-s23-1.jpg', 'Samsung Galaxy S23 front view', 1, true),
('550e8400-e29b-41d4-a716-446655440054', '550e8400-e29b-41d4-a716-446655440033', '/images/products/macbook-pro-14-1.jpg', 'MacBook Pro 14 inch open view', 1, true),
('550e8400-e29b-41d4-a716-446655440055', '550e8400-e29b-41d4-a716-446655440034', '/images/products/nike-air-max-270-1.jpg', 'Nike Air Max 270 side view', 1, true);

-- Insert product attributes
INSERT INTO product_attributes (id, product_id, attribute_name, attribute_value) VALUES
('550e8400-e29b-41d4-a716-446655440061', '550e8400-e29b-41d4-a716-446655440031', 'Storage', '128GB'),
('550e8400-e29b-41d4-a716-446655440062', '550e8400-e29b-41d4-a716-446655440031', 'Color', 'Titanium Natural'),
('550e8400-e29b-41d4-a716-446655440063', '550e8400-e29b-41d4-a716-446655440032', 'Storage', '256GB'),
('550e8400-e29b-41d4-a716-446655440064', '550e8400-e29b-41d4-a716-446655440032', 'Color', 'Phantom Black'),
('550e8400-e29b-41d4-a716-446655440065', '550e8400-e29b-41d4-a716-446655440033', 'Processor', 'M3 Pro'),
('550e8400-e29b-41d4-a716-446655440066', '550e8400-e29b-41d4-a716-446655440033', 'RAM', '16GB'),
('550e8400-e29b-41d4-a716-446655440067', '550e8400-e29b-41d4-a716-446655440034', 'Size', '10.5'),
('550e8400-e29b-41d4-a716-446655440068', '550e8400-e29b-41d4-a716-446655440034', 'Color', 'Black/White');

-- Insert a sample customer user
INSERT INTO users (id, username, email, password, first_name, last_name, role, is_active, email_verified) VALUES
('550e8400-e29b-41d4-a716-446655440071', 'johndoe', 'john.doe@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye.Jjt.1R8p1V3VBf5zB8Q9f5G5Tqs5OK', 'John', 'Doe', 'CUSTOMER', true, true);

-- Insert address for sample customer
INSERT INTO addresses (id, user_id, street_address, city, state, postal_code, country, is_default, address_type) VALUES
('550e8400-e29b-41d4-a716-446655440081', '550e8400-e29b-41d4-a716-446655440071', '123 Main Street', 'New York', 'NY', '10001', 'USA', true, 'SHIPPING');