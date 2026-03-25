INSERT INTO users (user_name, password, name, email, type)
VALUES ('john_doe', '', 'John Doe','john@gmail.com', 'REGULAR')
    ON DUPLICATE KEY UPDATE user_name = user_name;

-- Insertar dos tareas para 'alice' de forma idempotente
INSERT INTO tasks (user_id, description, status)
SELECT u.id, 'Comprar leche',   'PENDING'
FROM users u WHERE u.user_name = 'john_doe'
ON DUPLICATE KEY UPDATE description = description;

INSERT INTO tasks (user_id, description, status)
SELECT u.id, 'Reparar llantas del carro',   'INPROGRESS'
FROM users u WHERE u.user_name = 'john_doe'
ON DUPLICATE KEY UPDATE description = description;
