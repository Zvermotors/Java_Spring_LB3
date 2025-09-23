-- Таблица студентов
CREATE TABLE IF NOT EXISTS students (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    last_name VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    middle_name VARCHAR(255),
    gender VARCHAR(50) NOT NULL,
    nationality VARCHAR(100) NOT NULL,
    height INT NOT NULL,
    weight INT NOT NULL,
    birth_date DATE NOT NULL,
    phone_number VARCHAR(20) NOT NULL UNIQUE,
    university VARCHAR(255) NOT NULL,
    course INT NOT NULL,
    group_name VARCHAR(100) NOT NULL,
    average_grade DOUBLE NOT NULL,
    specialty VARCHAR(255) NOT NULL
);

-- Таблица пользователей
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

-- Таблица ролей
CREATE TABLE IF NOT EXISTS roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE
);

-- Таблица связи пользователей и ролей
CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);