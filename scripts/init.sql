-- Database initialization script for Docker
-- This script will be run when the PostgreSQL container starts

-- The database 'ecommerce_db' and user 'ecommerce_user' are already created by Docker environment variables
-- We just need to ensure proper permissions

GRANT ALL PRIVILEGES ON DATABASE ecommerce_db TO ecommerce_user;

-- Connect to the ecommerce_db database
\c ecommerce_db;

-- Grant schema permissions
GRANT ALL ON SCHEMA public TO ecommerce_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO ecommerce_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO ecommerce_user;