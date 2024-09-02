-- Database: pay_my_buddy

-- DROP DATABASE IF EXISTS pay_my_buddy;

-- Create Person table
CREATE TABLE IF NOT EXISTS Person (
    user_id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email_address VARCHAR(255) UNIQUE NOT NULL,
    date_of_birth DATE,
    wallet DECIMAL(19, 4) DEFAULT 0.0000,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- Create MoneyTransfers table
CREATE TABLE IF NOT EXISTS Money_Transfers (
    transfer_id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    sender_first_name VARCHAR(255) NOT NULL,
    sender_last_name VARCHAR(255) NOT NULL,
    sender_id BIGINT NOT NULL,
    sender_username VARCHAR(255) NOT NULL,
    receiver_first_name VARCHAR(255) NOT NULL,
    receiver_last_name VARCHAR(255) NOT NULL,
    receiver_id BIGINT NOT NULL,
    description TEXT,
    transfer_date DATE,
    transfer_amount DECIMAL(19, 4) NOT NULL,
    receiver_username VARCHAR(255) NOT NULL
);


-- Create Friends table
CREATE TABLE IF NOT EXISTS Friends (
    friendship_id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    sender_first_name VARCHAR(255) NOT NULL,
    sender_last_name VARCHAR(255) NOT NULL,
    sender_id BIGINT NOT NULL,
    sender_username VARCHAR(255) NOT NULL,
    receiver_first_name VARCHAR(255) NOT NULL,
    receiver_last_name VARCHAR(255) NOT NULL,
    receiver_id BIGINT NOT NULL,
    receiver_username VARCHAR(255) NOT NULL
);


-- Create CompanyWallet table
CREATE TABLE IF NOT EXISTS Company_Wallet (
    id BIGINT DEFAULT 1 CHECK (id = 1),
    wallet DECIMAL(19, 4) DEFAULT 0.0000,
    UNIQUE (id)
);

-- Insert initial value into CompanyWallet
INSERT INTO Company_Wallet (id, wallet) VALUES (1, 0.0000)
ON CONFLICT (id) DO NOTHING;

