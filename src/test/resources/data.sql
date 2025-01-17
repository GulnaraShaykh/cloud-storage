CREATE TABLE IF NOT EXISTS users (
                                     id SERIAL PRIMARY KEY,
                                     username VARCHAR(255) NOT NULL,
                                     password VARCHAR(255) NOT NULL,

                                   );

INSERT INTO users (id ,user_name, password) VALUES (1, 'testuser', 'password');
