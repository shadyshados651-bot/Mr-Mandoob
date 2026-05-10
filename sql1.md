CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE,
    phone VARCHAR(20),

    password VARCHAR(255) NOT NULL,
    token VARCHAR(255),
    role ENUM('admin','mandoob') DEFAULT 'mandoob',

    status TINYINT(1) DEFAULT 1,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_email (email),
    INDEX idx_role (role),
    INDEX idx_status (status)
);

CREATE TABLE clients (
    id INT AUTO_INCREMENT PRIMARY KEY,

    mandoob_id INT,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    address TEXT,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,


    FOREIGN KEY (mandoob_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_mandoob (mandoob_id)
);

CREATE TABLE products (
    id INT AUTO_INCREMENT PRIMARY KEY,

    name VARCHAR(150) NOT NULL,
    price_cost DECIMAL(10,2) NOT NULL, -- سعر الشراء
    sell_price DECIMAL(10,2) NOT NULL,

    quantity INT DEFAULT 0,
    min_stock INT DEFAULT 0,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_quantity (quantity)
);


CREATE TABLE invoices (
    id INT AUTO_INCREMENT PRIMARY KEY,

    mandoob_id INT,
    client_id INT,

    total DECIMAL(10,2) DEFAULT 0,
    paid DECIMAL(10,2) DEFAULT 0,

    status ENUM('paid','partial','unpaid') DEFAULT 'unpaid',

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (mandoob_id) REFERENCES users(id) ON DELETE SET NULL,
    FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE SET NULL,

    INDEX idx_mandoob (mandoob_id),
    INDEX idx_client (client_id)
);
CREATE TABLE invoice_items (
    id INT AUTO_INCREMENT PRIMARY KEY,

    invoice_id INT,
    product_id INT,

    quantity INT,
    price_cost DECIMAL(10,2) NOT NULL,
    sell_price DECIMAL(10,2) NOT NULL,
    profit DECIMAL(10,2),

    total DECIMAL(10,2),

    FOREIGN KEY (invoice_id) REFERENCES invoices(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);
CREATE TABLE payments (
    id INT AUTO_INCREMENT PRIMARY KEY,

    invoice_id INT,
    amount DECIMAL(10,2),

    notes TEXT,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (invoice_id) REFERENCES invoices(id) ON DELETE CASCADE,
    INDEX idx_invoice (invoice_id)
);

CREATE TABLE cash_requests (
    id INT AUTO_INCREMENT PRIMARY KEY,

    mandoob_id INT,
    amount DECIMAL(10,2),

    status ENUM('pending','approved','rejected') DEFAULT 'pending',

    notes TEXT,
    approved_by INT,
    created_by ENUM('admin','mandoob'),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    approved_at TIMESTAMP NULL,

    FOREIGN KEY (mandoob_id) REFERENCES users(id) ON DELETE CASCADE
);


-- =========================================
-- 📦 مخزون المندوب (البضاعة اللي معاه)
-- =========================================
CREATE TABLE mandoob_stock (
    id INT AUTO_INCREMENT PRIMARY KEY,
    
    mandoob_id INT NOT NULL,
    product_id INT NOT NULL,
    
    quantity INT DEFAULT 0,  -- الكمية الموجودة مع المندوب حالياً
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (mandoob_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    
    UNIQUE KEY uniq_mandoob_product (mandoob_id, product_id),
    INDEX idx_mandoob (mandoob_id),
    INDEX idx_product (product_id)
);
