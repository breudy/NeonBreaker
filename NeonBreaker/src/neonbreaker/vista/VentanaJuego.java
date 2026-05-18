package neonbreaker.vista;

import neonbreaker.controlador.ControladorJuego;
import neonbreaker.database.PartidaDAO;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

/**
 * Clase VentanaJuego (Vista)
 * ----------------------------------------------------------
 * La ventana principal donde ocurre el juego.
 * Crea el PanelJuego y el ControladorJuego y los conecta.
 *
 * Cuando la partida termina, guarda la puntuación en la
 * base de datos y ofrece ver el leaderboard o volver a jugar.
 * ----------------------------------------------------------
 */
public class VentanaJuego extends JFrame {

    private PanelJuego       panelJuego;
    private ControladorJuego controlador;
    private String           username;
    private int              idUsuario;

    public VentanaJuego(String username, int idUsuario) {
        this.username  = username;
        this.idUsuario = idUsuario;

        setTitle("Neon Breaker — " + username);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        iniciarPartida();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Crea el panel, el controlador y los conecta.
     * Se llama también al reiniciar la partida.
     */
    private void iniciarPartida() {
        // 1. Crear el panel de dibujo con el nombre del jugador
        panelJuego = new PanelJuego(username);

        // 2. Crear el controlador, pasándole el callback de fin de partida
        controlador = new ControladorJuego(panelJuego, this::onPartidaTerminada);

        // 3. Conectar controlador y panel
        panelJuego.setControlador(controlador);

        // 4. Registrar el teclado en el panel
        panelJuego.addKeyListener(controlador.getKeyAdapter());

        // 5. Añadir el panel a la ventana
        getContentPane().removeAll(); // limpiar si reiniciamos
        add(panelJuego);
        revalidate();
        repaint();

        // El panel necesita el foco para recibir eventos de teclado
        panelJuego.requestFocusInWindow();
    }

    /**
     * Se ejecuta cuando el controlador detecta que la partida terminó.
     * 1. Guarda la puntuación en la base de datos
     * 2. Pregunta al jugador qué quiere hacer
     */
    private void onPartidaTerminada() {
        // Redibujar para mostrar el mensaje de Game Over / Nivel completado
        panelJuego.repaint();

        // Pequeña pausa para que el jugador vea el mensaje antes del diálogo
        Timer espera = new Timer(1200, e -> mostrarDialogoFinal());
        espera.setRepeats(false);
        espera.start();
    }

    private void mostrarDialogoFinal() {
        int puntuacion = controlador.getPuntuacion();

        // ---- Guardar en la base de datos ----
        try {
            PartidaDAO.guardarPuntuacion(idUsuario, puntuacion);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // ---- Opciones al jugador ----
        String[] opciones = { "▷ JUGAR DE NUEVO", "⊞ VER RANKING", "✕ SALIR" };
        int eleccion = JOptionPane.showOptionDialog(
            this,
            "Puntuación final: " + puntuacion + " pts\n¿Qué quieres hacer?",
            "Partida terminada",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            opciones,
            opciones[0]
        );

        switch (eleccion) {
            case 0 -> {
                // Reiniciar partida
                iniciarPartida();
                panelJuego.requestFocusInWindow();
            }
            case 1 -> {
                // Mostrar leaderboard y luego preguntar de nuevo
                new PantallaLeaderboard(this);
                mostrarDialogoFinal(); // volver a preguntar después de cerrar el ranking
            }
            default -> {
                // Salir: volver a la pantalla de inicio
                dispose();
                abrirPantallaInicio();
            }
        }
    }

    /** Vuelve a la pantalla de inicio */
    private void abrirPantallaInicio() {
        new PantallaInicio(
            nombre -> {
                try {
                    int id = PartidaDAO.obtenerOCrearUsuario(nombre);
                    new VentanaJuego(nombre, id);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            },
            () -> new PantallaLeaderboard(null)
        );
    }
}
