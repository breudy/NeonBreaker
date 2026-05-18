package neonbreaker.controlador;

import neonbreaker.modelo.Ladrillo;
import neonbreaker.modelo.Paleta;
import neonbreaker.modelo.Pelota;
import neonbreaker.vista.PanelJuego;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase ControladorJuego
 * ----------------------------------------------------------
 * Es el cerebro del juego. Aquí está toda la lógica:
 * - El bucle de juego (Timer que se dispara cada 16ms ≈ 60fps)
 * - Detección de colisiones
 * - Sistema de puntuación y combos
 * - Control de teclado
 *
 * Patrón usado: el Controlador conoce el modelo (Pelota,
 * Paleta, Ladrillos) y actualiza la Vista (PanelJuego).
 * ----------------------------------------------------------
 */
public class ControladorJuego {

    // ---- Constantes del panel de juego ----
    public static final int ANCHO_PANEL  = 480;
    public static final int ALTO_PANEL   = 560;

    // ---- Referencias al modelo ----
    private Pelota         pelota;
    private Paleta         paleta;
    private List<Ladrillo> ladrillos;

    // ---- Estado del juego ----
    private int     puntuacion  = 0;
    private int     vidas       = 3;
    private int     combo       = 0;   // ladrillos rotos en un mismo rebote
    private boolean juegoActivo = false;
    private boolean partidaTerminada = false;

    // ---- Timer: el bucle de juego ----
    // Se dispara cada 16 milisegundos (~60 FPS)
    private Timer timer;

    // ---- Control de teclas ----
    private boolean teclaIzquierda = false;
    private boolean teclaDerecha   = false;

    // ---- Referencia a la vista ----
    private PanelJuego panelJuego;

    // ---- Callback para cuando termina la partida ----
    // Usamos una interfaz funcional para avisar a la ventana principal
    private Runnable onPartidaTerminada;

    public ControladorJuego(PanelJuego panelJuego, Runnable onPartidaTerminada) {
        this.panelJuego          = panelJuego;
        this.onPartidaTerminada  = onPartidaTerminada;
        inicializarJuego();
        configurarTimer();
    }

    /**
     * Inicializa (o reinicia) todos los objetos del juego
     * a su estado inicial.
     */
    public void inicializarJuego() {
        // Pelota: empieza en el centro
        pelota = new Pelota(ANCHO_PANEL / 2, ALTO_PANEL - 120);

        // Paleta: centrada abajo
        paleta = new Paleta(
            (ANCHO_PANEL - Paleta.ANCHO) / 2,
            ALTO_PANEL - 50
        );

        // Crear la cuadrícula de ladrillos
        ladrillos = crearLadrillos();

        puntuacion       = 0;
        vidas            = 3;
        combo            = 0;
        juegoActivo      = false;
        partidaTerminada = false;
    }

    /**
     * Crea los ladrillos en filas y columnas.
     * Los ladrillos de la fila superior valen más puntos.
     */
    private List<Ladrillo> crearLadrillos() {
        List<Ladrillo> lista = new ArrayList<>();

        int filas    = 5;
        int columnas = 7;
        int margenX  = 15;
        int margenY  = 60;  // margen desde arriba (dejamos espacio al HUD)
        int espacioX = 5;
        int espacioY = 8;

        for (int fila = 0; fila < filas; fila++) {
            // Los ladrillos de arriba valen más (fila 0 = 50 pts, fila 4 = 10 pts)
            int puntosFila = (filas - fila) * 10;

            for (int col = 0; col < columnas; col++) {
                int x = margenX + col * (Ladrillo.ANCHO + espacioX);
                int y = margenY + fila * (Ladrillo.ALTO + espacioY);
                lista.add(new Ladrillo(x, y, puntosFila));
            }
        }

        return lista;
    }

    /**
     * Configura el Timer: cada 16ms ejecuta el método actualizar().
     * Esto es el "bucle de juego" o "game loop".
     */
    private void configurarTimer() {
        timer = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizar();
            }
        });
    }

    /**
     * Método principal del bucle: se ejecuta ~60 veces por segundo.
     * Orden: mover → colisionar → comprobar estado → redibujar.
     */
    private void actualizar() {
        if (!(!juegoActivo || partidaTerminada)) {


        // 1. Mover paleta según teclas pulsadas
        if (teclaIzquierda) paleta.moverIzquierda();
        if (teclaDerecha)   paleta.moverDerecha(ANCHO_PANEL);

        // 2. Mover pelota
        pelota.mover();

        // 3. Detectar colisiones
        comprobarColisiones();

        // 4. Redibujar la pantalla
        panelJuego.repaint();
        }
    }

    /**
     * Comprueba todas las colisiones posibles:
     * - Pelota con bordes del panel
     * - Pelota con paleta
     * - Pelota con ladrillos
     * - Pelota cayendo por debajo (pierde vida)
     */
    private void comprobarColisiones() {

        // --- Borde izquierdo y derecho ---
        if (pelota.getX() <= 0 || pelota.getX() + Pelota.TAMANYO >= ANCHO_PANEL) {
            pelota.rebotarX();
        }

        // --- Borde superior ---
        if (pelota.getY() <= 0) {
            pelota.rebotarY();
        }

        // --- Paleta ---
        if (pelota.getRectangulo().intersects(paleta.getRectangulo())) {
            pelota.rebotarY();
            // Resetear combo al tocar la paleta (nuevo rebote, nuevo combo)
            combo = 0;
            // Aseguramos que la pelota vaya hacia arriba (evita que quede atrapada)
            if (pelota.getVelocidadY() > 0) pelota.rebotarY();
        }

        // --- Ladrillos ---
        for (Ladrillo ladrillo : ladrillos) {
            if (ladrillo.isDestruido()) continue;

            if (pelota.getRectangulo().intersects(ladrillo.getRectangulo())) {
                ladrillo.destruir();
                pelota.rebotarY();

                // Sistema de combo: cada ladrillo extra en el mismo rebote
                // vale 10 puntos adicionales
                combo++;
                int puntosGanados = ladrillo.getPuntos() + (combo - 1) * 10;
                puntuacion += puntosGanados;

                // Solo puede romper un ladrillo por tick (evita bugs)
                break;
            }
        }

        // --- Pelota cae por debajo de la paleta ---
        if (pelota.getY() > ALTO_PANEL) {
            vidas--;
            combo = 0;

            if (vidas <= 0) {
                // Sin vidas: game over
                terminarPartida();
            } else {
                // Quedan vidas: reiniciar posición de pelota y paleta
                resetearPelotaYPaleta();
            }
        }

        // Todos los ladrillos destruidos: nivel completado
        boolean todosDestruidos = ladrillos.stream().allMatch(Ladrillo::isDestruido);
        if (todosDestruidos) {
            terminarPartida();
        }
    }

    /** Reinicia la posición de la pelota y la paleta sin tocar la puntuación */
    private void resetearPelotaYPaleta() {
        pelota  = new Pelota(ANCHO_PANEL / 2, ALTO_PANEL - 120);
        paleta  = new Paleta((ANCHO_PANEL - Paleta.ANCHO) / 2, ALTO_PANEL - 50);
        juegoActivo = false; // pausamos hasta que el jugador pulse espacio
        panelJuego.repaint();
    }

    /** Para el timer y avisa a la ventana principal que la partida terminó */
    private void terminarPartida() {
        partidaTerminada = true;
        juegoActivo      = false;
        timer.stop();
        // Ejecutamos el callback (guardará la puntuación y mostrará el leaderboard)
        if (onPartidaTerminada != null) {
            onPartidaTerminada.run();
        }
    }

    // CONTROL DE TECLADO
    // KeyAdapter: solo sobreescribimos los métodos que
    // necesitamos (no hace falta implementar todos).

    /**
     * Devuelve un KeyAdapter listo para añadir al PanelJuego.
     * Se registra qué teclas están pulsadas (para movimiento suave)
     * y reacciona a la barra espaciadora para lanzar la pelota.
     */
    public KeyAdapter getKeyAdapter() {
        return new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT  -> teclaIzquierda = true;
                    case KeyEvent.VK_RIGHT -> teclaDerecha   = true;
                    case KeyEvent.VK_SPACE -> {
                        // Espacio: lanzar la pelota (si la partida no ha terminado)
                        if (!partidaTerminada) {
                            juegoActivo = true;
                            if (!timer.isRunning()) timer.start();
                        }
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // Al soltar la tecla, dejamos de mover en esa dirección
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT  -> teclaIzquierda = false;
                    case KeyEvent.VK_RIGHT -> teclaDerecha   = false;
                }
            }
        };
    }

    public Pelota         getPelota()           { return pelota;           }
    public Paleta         getPaleta()           { return paleta;           }
    public List<Ladrillo> getLadrillos()        { return ladrillos;        }
    public int            getPuntuacion()       { return puntuacion;       }
    public int            getVidas()            { return vidas;            }
    public int            getCombo()            { return combo;            }
    public boolean        isJuegoActivo()       { return juegoActivo;      }
    public boolean        isPartidaTerminada()  { return partidaTerminada; }
}
