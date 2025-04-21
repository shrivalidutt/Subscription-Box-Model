CREATE TABLE IF NOT EXISTS subscription_types (
    Subscription_Type_ID INT PRIMARY KEY AUTO_INCREMENT,
    Type_Name VARCHAR(50) NOT NULL,
    Description TEXT,
    Duration INT NOT NULL COMMENT 'Duration in months',
    Price DECIMAL(10,2) NOT NULL,
    Created_At TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Updated_At TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Insert some default subscription types
INSERT INTO subscription_types (Type_Name, Description, Duration, Price) VALUES
('Monthly Basic', 'Basic monthly subscription package', 1, 29.99),
('Quarterly Premium', 'Premium quarterly subscription with additional benefits', 3, 79.99),
('Annual Pro', 'Professional annual subscription with all features', 12, 299.99); 