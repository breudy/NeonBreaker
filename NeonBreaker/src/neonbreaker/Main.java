package neonbreaker;

import neonbreaker.database.PartidaDAO;
import neonbreaker.vista.PantallaInicio;
import neonbreaker.vista.PantallaLeaderboard;
import neonbreaker.vista.VentanaJuego;

import javax.swing.*;
import java.sql.SQLException;

/**
 * Clase Main — Punto de entrada del programa
 * ----------------------------------------------------------
 * Es lo primero que ejecuta Java al arrancar.
 * Solo hace una cosa: lanzar la pantalla de inicio.
 *
 * SwingUtilities.invokeLater() asegura que toda la interfaz
 * gráfica se crea en el hilo correcto (Event Dispatch Thread).
 * Esto es importante en Swing para evitar problemas raros.
 * ----------------------------------------------------------
 */
public class Main {

    public static void main(String[] args) {

        // Intentamos poner el Look and Feel del sistema operativo
        // (así los diálogos se ven más nativos)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Si falla, Swing usa su aspecto por defecto — no es crítico
            System.out.println("No se pudo cargar el LookAndFeel del sistema.");
        }

        // Lanzar la UI en el hilo de Swing
        SwingUtilities.invokeLater(() -> {

            new PantallaInicio(

                // Callback "onJugar": se ejecuta cuando el jugador pulsa JUGAR
                // Recibe el nickname como parámetro (String nombre)
                nombre -> {
                    try {
                        // 1. Obtener o crear el usuario en la base de datos
                        int idUsuario = PartidaDAO.obtenerOCrearUsuario(nombre);

                        // 2. Abrir la ventana del juego
                        new VentanaJuego(nombre, idUsuario);

                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null,
                            "Error al conectar con la base de datos:\n" + ex.getMessage()
                            + "\n\nComprueba que MySQL está encendido y que los datos\n"
                            + "de conexión en Conexion.java son correctos.",
                            "Error de conexión",
                            JOptionPane.ERROR_MESSAGE);
                    }
                },

                // Callback "onLeaderboard": se ejecuta cuando pulsa VER RANKING
                () -> new PantallaLeaderboard(null)
            );
        });
    }
}
