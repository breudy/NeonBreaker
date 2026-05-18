package neonbreaker.database;

import neonbreaker.modelo.Puntuacion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase PartidaDAO (Data Access Object)
 * ----------------------------------------------------------
 * Contiene todos los métodos que hacen consultas SQL.
 * Separar la lógica de base de datos aquí hace el código
 * mucho más limpio y fácil de mantener.
 *
 *
 * ----------------------------------------------------------
 */
public class PartidaDAO {

    /**
     * Busca un usuario por su nombre. Si no existe, lo crea.
     * Devuelve el id_usuario en ambos casos.
     *
     * @param username El nickname introducido por el jugador
     * @return El id del usuario en la base de datos
     */
    public static int obtenerOCrearUsuario(String username) throws SQLException {

        // Primero buscamos si ya existe ese nombre
        String sqlBuscar = "SELECT id_usuario FROM USUARIOS WHERE username = ?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sqlBuscar)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            // Si encontramos una fila, devolvemos su id directamente
            if (rs.next()) {
                return rs.getInt("id_usuario");
            }
        }

        // Si no existía, lo insertamos y devolvemos el id generado
        String sqlInsertar = "INSERT INTO USUARIOS (username) VALUES (?)";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sqlInsertar, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, username);
            ps.executeUpdate();

            // RETURN_GENERATED_KEYS nos da el id_usuario asignado por AUTO_INCREMENT
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1);
            }
        }

        throw new SQLException("No se pudo crear el usuario.");
    }

    /**
     * Guarda la puntuación de una partida en la tabla PUNTUACIONES.
     *
     * @param idUsuario  El id del jugador
     * @param puntuacion Los puntos obtenidos en la partida
     */
    public static void guardarPuntuacion(int idUsuario, int puntuacion) throws SQLException {

        String sql = "INSERT INTO PUNTUACIONES (id_usuario, puntuacion_obtenida) VALUES (?, ?)";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ps.setInt(2, puntuacion);
            ps.executeUpdate();
        }
    }

    /**
     * Obtiene el TOP 10 de puntuaciones para el leaderboard.
     * Hace un JOIN entre PUNTUACIONES y USUARIOS para mostrar
     * el nombre del jugador junto a su puntuación.
     *
     * @return Lista de objetos Puntuacion ordenados de mayor a menor
     */
    public static List<Puntuacion> obtenerTop10() throws SQLException {

        List<Puntuacion> lista = new ArrayList<>();

        // JOIN: combinamos las dos tablas por id_usuario
        // ORDER BY DESC: de mayor a menor puntuación
        // LIMIT 10: solo los 10 primeros
        String sql = """
                SELECT u.username, p.puntuacion_obtenida, p.fecha_partida
                FROM PUNTUACIONES p
                JOIN USUARIOS u ON p.id_usuario = u.id_usuario
                ORDER BY p.puntuacion_obtenida DESC
                LIMIT 10
                """;

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String username    = rs.getString("username");
                int    puntos      = rs.getInt("puntuacion_obtenida");
                String fecha       = rs.getString("fecha_partida");
                lista.add(new Puntuacion(username, puntos, fecha));
            }
        }

        return lista;
    }
}
