-- ============================================================
-- NEON BREAKER — Script de creación de la base de datos
-- Ejecutar en MySQL Workbench o en la terminal de MySQL
-- ============================================================

-- Crear la base de datos si no existe
CREATE DATABASE IF NOT EXISTS neonbreaker;

-- Seleccionarla para trabajar con ella
USE neonbreaker;

-- ============================================================
-- TABLA USUARIOS
-- Guarda el nombre (nickname) y cuándo se registró.
-- id_usuario es AUTO_INCREMENT: MySQL le asigna el número solo.
-- ============================================================
CREATE TABLE IF NOT EXISTS USUARIOS (
    id_usuario     INT          NOT NULL AUTO_INCREMENT,
    username       VARCHAR(50)  NOT NULL,
    fecha_registro DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_usuario)
);

-- ============================================================
-- TABLA PUNTUACIONES
-- Cada fila es una partida jugada.
-- id_usuario es clave foránea: apunta a USUARIOS.id_usuario.
-- ============================================================
CREATE TABLE IF NOT EXISTS PUNTUACIONES (
    id_partida         INT  NOT NULL AUTO_INCREMENT,
    id_usuario         INT  NOT NULL,
    puntuacion_obtenida INT NOT NULL,
    fecha_partida      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_partida),
    FOREIGN KEY (id_usuario) REFERENCES USUARIOS(id_usuario)
);
