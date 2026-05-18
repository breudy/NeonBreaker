package neonbreaker.vista;

import neonbreaker.controlador.ControladorJuego;
import neonbreaker.modelo.Ladrillo;
import neonbreaker.modelo.Paleta;
import neonbreaker.modelo.Pelota;

import javax.swing.*;
import java.awt.*;

/**
 * Clase PanelJuego (Vista)
 * ----------------------------------------------------------
 * Es el lienzo donde se dibuja todo el juego.
 * Extiende JPanel y sobreescribe paintComponent() para
 * dibujar frame a frame con Graphics2D.
 *
 * Esta clase no tiene lógica de juego: solo dibuja lo que
 * le dice el Controlador.
 * ----------------------------------------------------------
 */
public class PanelJuego extends JPanel {

    // Colores del tema Neon Breaker
    private static final Color COLOR_FONDO        = new Color(0, 0, 0);
    private static final Color COLOR_PELOTA       = new Color(255, 255, 255);
    private static final Color COLOR_PALETA       = new Color(255, 0, 127);    // neon-pink
    private static final Color COLOR_LADRILLO_1   = new Color(255, 0, 127);    // fila 1
    private static final Color COLOR_LADRILLO_2   = new Color(203, 48, 224);   // fila 2 — magenta
    private static final Color COLOR_LADRILLO_3   = new Color(180, 0, 100);    // fila 3
    private static final Color COLOR_LADRILLO_4   = new Color(140, 30, 180);   // fila 4
    private static final Color COLOR_LADRILLO_5   = new Color(100, 0, 80);     // fila 5
    private static final Color COLOR_TEXTO        = new Color(255, 255, 255);
    private static final Color COLOR_GRID         = new Color(255, 0, 127, 15);// grid sutil

    private static final Color[] COLORES_FILAS = {
        COLOR_LADRILLO_1, COLOR_LADRILLO_2, COLOR_LADRILLO_3,
        COLOR_LADRILLO_4, COLOR_LADRILLO_5
    };

    // Referencia al controlador para obtener el estado del juego
    private ControladorJuego controlador;
    private String           username;

    public PanelJuego(String username) {
        this.username = username;
        setPreferredSize(new Dimension(
            ControladorJuego.ANCHO_PANEL,
            ControladorJuego.ALTO_PANEL
        ));
        setBackground(COLOR_FONDO);
        setFocusable(true); // necesario para capturar teclas
    }

    /** El controlador se asigna después de construir el panel */
    public void setControlador(ControladorJuego controlador) {
        this.controlador = controlador;
    }

    /**
     * paintComponent: se llama automáticamente cada vez que
     * hacemos repaint(). Aquí dibujamos todo el juego.
     *
     * IMPORTANTE: siempre llamar a super.paintComponent(g) primero
     * para limpiar el frame anterior.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Graphics2D nos da más opciones que Graphics básico
        Graphics2D g2d = (Graphics2D) g;

        // Antialiasing: hace los bordes más suaves
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (controlador == null) return;

        // Dibujar en orden: fondo → grid → ladrillos → pelota → paleta → HUD → mensajes
        dibujarGrid(g2d);
        dibujarLadrillos(g2d);
        dibujarPelota(g2d);
        dibujarPaleta(g2d);
        dibujarHUD(g2d);

        // Mensajes de estado (pausado, game over, nivel completado)
        if (!controlador.isJuegoActivo()) {
            dibujarMensajeEstado(g2d);
        }
    }

    /** Dibuja una cuadrícula sutil de fondo (efecto retro) */
    private void dibujarGrid(Graphics2D g2d) {
        g2d.setColor(COLOR_GRID);
        int tam = 40;
        for (int x = 0; x < getWidth(); x += tam) {
            g2d.drawLine(x, 0, x, getHeight());
        }
        for (int y = 0; y < getHeight(); y += tam) {
            g2d.drawLine(0, y, getWidth(), y);
        }
    }

    /** Dibuja todos los ladrillos no destruidos */
    private void dibujarLadrillos(Graphics2D g2d) {
        int totalFilas  = 5;
        int margenY     = 60;
        int espacioY    = 8;

        for (Ladrillo l : controlador.getLadrillos()) {
            if (l.isDestruido()) continue;

            // Calculamos a qué fila pertenece para asignarle el color
            int indiceFila = (l.getY() - margenY) / (Ladrillo.ALTO + espacioY);
            indiceFila     = Math.min(indiceFila, COLORES_FILAS.length - 1);
            Color color    = COLORES_FILAS[indiceFila];

            // Relleno del ladrillo
            g2d.setColor(color);
            g2d.fillRect(l.getX(), l.getY(), Ladrillo.ANCHO, Ladrillo.ALTO);

            // Borde más oscuro para darle profundidad
            g2d.setColor(color.darker());
            g2d.drawRect(l.getX(), l.getY(), Ladrillo.ANCHO, Ladrillo.ALTO);
        }
    }

    /** Dibuja la pelota como un círculo blanco con resplandor */
    private void dibujarPelota(Graphics2D g2d) {
        Pelota p = controlador.getPelota();

        // Resplandor exterior (círculo más grande y semitransparente)
        g2d.setColor(new Color(255, 255, 255, 60));
        g2d.fillOval(
            p.getX() - 4, p.getY() - 4,
            Pelota.TAMANYO + 8, Pelota.TAMANYO + 8
        );

        // Pelota real
        g2d.setColor(COLOR_PELOTA);
        g2d.fillOval(p.getX(), p.getY(), Pelota.TAMANYO, Pelota.TAMANYO);
    }

    /** Dibuja la paleta como un rectángulo rosa con resplandor */
    private void dibujarPaleta(Graphics2D g2d) {
        Paleta p = controlador.getPaleta();

        // Resplandor
        g2d.setColor(new Color(255, 0, 127, 60));
        g2d.fillRect(p.getX() - 3, p.getY() - 3, Paleta.ANCHO + 6, Paleta.ALTO + 6);

        // Paleta real
        g2d.setColor(COLOR_PALETA);
        g2d.fillRect(p.getX(), p.getY(), Paleta.ANCHO, Paleta.ALTO);
    }

    /**
     * HUD (Heads-Up Display): la barra de información superior.
     * Muestra nombre, puntuación, vidas y combo.
     */
    private void dibujarHUD(Graphics2D g2d) {
        // Fondo semitransparente para el HUD
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRect(0, 0, getWidth(), 50);

        // Línea separadora rosa
        g2d.setColor(COLOR_PALETA);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawLine(0, 50, getWidth(), 50);

        // Texto del HUD
        g2d.setColor(COLOR_TEXTO);
        g2d.setFont(new Font("Monospaced", Font.BOLD, 13));

        // Nombre del jugador
        g2d.drawString("[" + username + "]", 10, 20);

        // Puntuación
        g2d.drawString("PTS: " + controlador.getPuntuacion(), 10, 40);

        // Vidas (mostramos corazones ♥)
        StringBuilder vidas = new StringBuilder("VIDAS: ");
        for (int i = 0; i < controlador.getVidas(); i++) vidas.append("♥ ");
        g2d.drawString(vidas.toString(), 170, 30);

        // Combo (solo si hay combo activo)
        if (controlador.getCombo() > 1) {
            g2d.setColor(new Color(255, 220, 0)); // amarillo para el combo
            g2d.setFont(new Font("Monospaced", Font.BOLD, 14));
            g2d.drawString("COMBO x" + controlador.getCombo(), 340, 30);
        }
    }

    /**
     * Dibuja mensajes de estado en el centro de la pantalla:
     * - Al inicio / después de perder una vida: "Pulsa ESPACIO"
     * - Game Over
     * - ¡Nivel completado!
     */
    private void dibujarMensajeEstado(Graphics2D g2d) {
        // Overlay oscuro semitransparente
        g2d.setColor(new Color(0, 0, 0, 160));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        g2d.setColor(COLOR_PALETA);

        if (controlador.isPartidaTerminada()) {
            // Comprobar si ganó (todos los ladrillos destruidos) o perdió
            boolean todoDestruido = controlador.getLadrillos().stream()
                .allMatch(Ladrillo::isDestruido);

            if (todoDestruido) {
                dibujarTextosCentrados(g2d, "¡NIVEL COMPLETADO!", "Puntuación: " + controlador.getPuntuacion());
            } else {
                dibujarTextosCentrados(g2d, "GAME OVER", "Puntuación final: " + controlador.getPuntuacion());
            }
        } else {
            // Pausa entre vidas o inicio de partida
            g2d.setFont(new Font("Monospaced", Font.BOLD, 18));
            String msg = controlador.getVidas() == 3 ? "NEON BREAKER" : "VIDAS: " + controlador.getVidas();
            int w = g2d.getFontMetrics().stringWidth(msg);
            g2d.drawString(msg, (getWidth() - w) / 2, getHeight() / 2 - 20);

            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Monospaced", Font.PLAIN, 14));
            String sub = "Pulsa ESPACIO para " + (controlador.getVidas() == 3 ? "jugar" : "continuar");
            int ws = g2d.getFontMetrics().stringWidth(sub);
            g2d.drawString(sub, (getWidth() - ws) / 2, getHeight() / 2 + 10);
        }
    }

    /** Dibuja título y subtítulo centrados */
    private void dibujarTextosCentrados(Graphics2D g2d, String titulo, String subtitulo) {
        g2d.setFont(new Font("Monospaced", Font.BOLD, 22));
        int w = g2d.getFontMetrics().stringWidth(titulo);
        g2d.drawString(titulo, (getWidth() - w) / 2, getHeight() / 2 - 20);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Monospaced", Font.PLAIN, 15));
        int ws = g2d.getFontMetrics().stringWidth(subtitulo);
        g2d.drawString(subtitulo, (getWidth() - ws) / 2, getHeight() / 2 + 15);
    }
}
