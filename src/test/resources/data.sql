CREATE TABLE IF NOT EXISTS users (
                                     id SERIAL PRIMARY KEY,
                                     username VARCHAR(255) NOT NULL,
    login VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
    );

-- Добавляем пользователя
INSERT INTO users (id, username, login, password)
VALUES
    (1, 'testuser', 'test@example.com', 'password');
