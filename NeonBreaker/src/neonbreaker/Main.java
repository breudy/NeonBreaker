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
 * <p>
 * SwingUtilities.invokeLater() asegura que toda la interfaz
 * gráfica se crea en el hilo correcto (Event Dispatch Thread).
 * Esto es importante en Swing para evitar problemas raros.
 * ----------------------------------------------------------
 */
public class Main {

    public static void main(String[] args) {

            new PantallaInicio(

                    // Callback "onJugar": se ejecuta cuando el jugador pulsa JUGAR
                    // Recibe el nickname como parámetro (String nombre)
                    nombre -> {
                        // 1. Obtener o crear el usuario en la base de datos
                        int idUsuario = PartidaDAO.obtenerOCrearUsuario(nombre);

                        // 2. Abrir la ventana del juego
                        new VentanaJuego(nombre, idUsuario);

                    },

                    // Callback "onLeaderboard": se ejecuta cuando pulsa VER RANKING
                    () -> new PantallaLeaderboard(null)
            );
    }
}
