-- ============================================================
-- NEON BREAKER — Script de Base de Datos
-- Ejecutar este archivo en MySQL antes de arrancar el juego
-- ============================================================

-- Creamos la base de datos si no existe
CREATE DATABASE IF NOT EXISTS neon_breaker;

-- Seleccionamos la base de datos
USE neon_breaker;

-- ============================================================
-- TABLA: USUARIOS
-- Guarda el nickname de cada jugador
-- ============================================================
CREATE TABLE IF NOT EXISTS USUARIOS (
    id_usuario     INT          NOT NULL AUTO_INCREMENT,
    username       VARCHAR(50)  NOT NULL,
    fecha_registro DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_usuario)
);

-- ============================================================
-- TABLA: PUNTUACIONES
-- Guarda el resultado de cada partida
-- id_usuario es clave foránea que referencia a USUARIOS
-- ============================================================
CREATE TABLE IF NOT EXISTS PUNTUACIONES (
    id_partida          INT  NOT NULL AUTO_INCREMENT,
    id_usuario          INT  NOT NULL,
    puntuacion_obtenida INT  NOT NULL,
    fecha_partida       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_partida),
    FOREIGN KEY (id_usuario) REFERENCES USUARIOS(id_usuario)
);
