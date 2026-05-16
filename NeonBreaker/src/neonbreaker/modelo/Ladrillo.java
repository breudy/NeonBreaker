package neonbreaker.modelo;

import java.awt.Rectangle;

/**
 * Clase Ladrillo (Modelo)
 * ----------------------------------------------------------
 * Representa cada uno de los ladrillos que hay que romper.
 *
 * Usamos Rectangle de Java AWT porque nos da gratis:
 * - La posición (x, y) y el tamaño (width, height)
 * - El método intersects() para detectar colisiones fácilmente
 * ----------------------------------------------------------
 */
public class Ladrillo {

    // Tamaño fijo de todos los ladrillos
    public static final int ANCHO  = 60;
    public static final int ALTO   = 20;

    private int       x, y;
    private boolean   destruido;  // false = visible, true = ya no existe
    private int       puntos;     // cuántos puntos da al romperlo

    public Ladrillo(int x, int y, int puntos) {
        this.x         = x;
        this.y         = y;
        this.puntos    = puntos;
        this.destruido = false;
    }

    /**
     * Devuelve el área del ladrillo como Rectangle.
     * Se usa para detectar colisiones con la pelota.
     */
    public Rectangle getRectangulo() {
        return new Rectangle(x, y, ANCHO, ALTO);
    }

    // --- Getters y setters ---
    public int     getX()          { return x;         }
    public int     getY()          { return y;         }
    public int     getPuntos()     { return puntos;     }
    public boolean isDestruido()   { return destruido;  }
    public void    destruir()      { this.destruido = true; }
}
