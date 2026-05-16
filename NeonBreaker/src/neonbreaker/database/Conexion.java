package neonbreaker.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase Conexion
 * ----------------------------------------------------------
 * Su única responsabilidad es abrir y cerrar la conexión
 * con la base de datos MySQL.
 *
 * Usamos el patrón de "clase utilitaria": no se instancia,
 * solo llamamos a sus métodos estáticos.
 * ----------------------------------------------------------
 */
public class Conexion {

    // Datos de conexión — cámbialos si tu MySQL usa otro puerto o contraseña
    private static final String URL      = "jdbc:mysql://localhost:3306/neonbreaker";
    private static final String USUARIO  = "root";       // tu usuario de MySQL
    private static final String PASSWORD = "mysql";       // tu contraseña de MySQL

    /**
     * Abre y devuelve una conexión a la base de datos.
     * Si falla, lanza una SQLException que hay que capturar.
     */
    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }
}
