package neonbreaker.modelo;

/**
 * Clase Puntuacion (Modelo)
 * ----------------------------------------------------------
 * Representa una fila del leaderboard: nombre del jugador,
 * puntuación y fecha de la partida.
 *
 *
 * constructor y getters. Sin lógica de negocio.
 * ----------------------------------------------------------
 */
public class Puntuacion {

    private String username;
    private int    puntos;
    private String fecha;

    public Puntuacion(String username, int puntos, String fecha) {
        this.username = username;
        this.puntos   = puntos;
        this.fecha    = fecha;
    }

    public String getUsername() { return username; }
    public int    getPuntos()   { return puntos;   }
    public String getFecha()    { return fecha;    }
}
