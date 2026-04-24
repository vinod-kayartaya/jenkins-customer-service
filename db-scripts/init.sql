CREATE DATABASE IF NOT EXISTS customerdb;
USE customerdb;

CREATE TABLE IF NOT EXISTS customers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(255),
    city VARCHAR(255)
);

INSERT INTO customers (first_name, last_name, email, phone, city) VALUES
('John', 'Doe', 'john.doe@example.com', '1234567890', 'New York'),
('Jane', 'Smith', 'jane.smith@example.com', '0987654321', 'Los Angeles'),
('Alice', 'Johnson', 'alice.j@example.com', '1112223333', 'Chicago'),
('Bob', 'Williams', 'bob.w@example.com', '4445556666', 'Houston'),
('Charlie', 'Brown', 'charlie.b@example.com', '7778889999', 'Phoenix'),
('Diana', 'Prince', 'diana.p@example.com', '1010101010', 'Philadelphia'),
('Eve', 'Davis', 'eve.d@example.com', '2020202020', 'San Antonio'),
('Frank', 'Miller', 'frank.m@example.com', '3030303030', 'San Diego'),
('Grace', 'Wilson', 'grace.w@example.com', '4040404040', 'Dallas'),
('Hank', 'Moore', 'hank.m@example.com', '5050505050', 'San Jose'),
('Ivy', 'Taylor', 'ivy.t@example.com', '6060606060', 'Austin'),
('Jack', 'Anderson', 'jack.a@example.com', '7070707070', 'Jacksonville'),
('Karen', 'Thomas', 'karen.t@example.com', '8080808080', 'Fort Worth'),
('Leo', 'Jackson', 'leo.j@example.com', '9090909090', 'Columbus'),
('Mia', 'White', 'mia.w@example.com', '1212121212', 'San Francisco');
