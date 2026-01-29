DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
                       id BIGINT PRIMARY KEY,
                       full_name VARCHAR(100) NOT NULL,
                       balance DECIMAL(15,2) NOT NULL
);

CREATE TABLE payments (
                          id BIGSERIAL PRIMARY KEY,
                          user_id BIGINT NOT NULL,
                          amount DECIMAL(15,2) NOT NULL,
                          status VARCHAR(20) NOT NULL,
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (user_id) REFERENCES users(id)
);