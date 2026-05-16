package neonbreaker.modelo;

import java.awt.Rectangle;

/**
 * Clase Pelota (Modelo)
 * ----------------------------------------------------------
 * Controla la posición y el movimiento de la pelota.
 *
 * La pelota se mueve sumando velocidadX y velocidadY a su
 * posición en cada tick del bucle de juego.
 * Cuando choca con algo, invertimos el signo de la velocidad.
 * ----------------------------------------------------------
 */
public class Pelota {

    public static final int TAMANYO = 12; // diámetro en píxeles

    private int x, y;
    private int velocidadX, velocidadY;

    public Pelota(int x, int y) {
        this.x          =  x;
        this.y          =  y;
        this.velocidadX =  3;  // se mueve 3px a la derecha por tick
        this.velocidadY = -3;  // se mueve 3px hacia arriba por tick
    }

    /**
     * Mueve la pelota sumando su velocidad a la posición actual.
     * Se llama en cada tick del Timer (bucle de juego).
     */
    public void mover() {
        x += velocidadX;
        y += velocidadY;
    }

    /** Invierte la dirección horizontal (choque con pared lateral) */
    public void rebotarX() { velocidadX = -velocidadX; }

    /** Invierte la dirección vertical (choque con techo, paleta o ladrillo) */
    public void rebotarY() { velocidadY = -velocidadY; }

    /** Devuelve el área de la pelota para detectar colisiones */
    public Rectangle getRectangulo() {
        return new Rectangle(x, y, TAMANYO, TAMANYO);
    }

    // --- Getters y setters ---
    public int getX() { return x; }
    public int getY() { return y; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public int getVelocidadY() { return velocidadY; }
}
