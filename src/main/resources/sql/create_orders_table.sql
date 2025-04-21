CREATE TABLE IF NOT EXISTS orders (
    Order_ID INT PRIMARY KEY AUTO_INCREMENT,
    User_ID INT NOT NULL,
    Subscription_ID INT NOT NULL,
    Order_Date DATE NOT NULL,
    status VARCHAR(20) DEFAULT 'Active',
    Delivery_Status VARCHAR(20) DEFAULT 'Pending',
    Total_Amount DECIMAL(10,2) DEFAULT 0.00,
    Shipping_Address TEXT NOT NULL,
    Feedback TEXT,
    Created_At TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Updated_At TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (User_ID) REFERENCES users(User_ID),
    FOREIGN KEY (Subscription_ID) REFERENCES subscriptions(Subscription_ID)
); 