package neonbreaker.modelo;

import java.awt.Rectangle;

/**
 * Clase Paleta (Modelo)
 * ----------------------------------------------------------
 * Representa la paleta que controla el jugador.
 * Se mueve horizontalmente con las teclas ← y →.
 * ----------------------------------------------------------
 */
public class Paleta {

    public static final int ANCHO     = 90;
    public static final int ALTO      = 12;
    public static final int VELOCIDAD = 6; // píxeles por tecla pulsada

    private int x, y;

    public Paleta(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /** Mueve la paleta a la izquierda, sin salirse del panel */
    public void moverIzquierda() {
        if (x > 0) {
            x -= VELOCIDAD;
        }
    }

    /** Mueve la paleta a la derecha, sin pasarse del ancho del panel */
    public void moverDerecha(int anchoPantalla) {
        if (x + ANCHO < anchoPantalla) {
            x += VELOCIDAD;
        }
    }

    /** Devuelve el área de la paleta para detectar colisiones */
    public Rectangle getRectangulo() {
        return new Rectangle(x, y, ANCHO, ALTO);
    }

    // --- Getters ---
    public int getX() { return x; }
    public int getY() { return y; }
}
