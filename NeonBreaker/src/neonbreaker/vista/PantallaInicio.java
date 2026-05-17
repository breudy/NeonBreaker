package neonbreaker.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Clase PantallaInicio (Vista)
 * ----------------------------------------------------------
 * Primera pantalla que ve el jugador.
 * Pide el nickname y lanza el juego al pulsar el botón.
 *
 * Extiende JFrame: es una ventana completa por sí misma.
 * ----------------------------------------------------------
 */
public class PantallaInicio extends JFrame {

    private JTextField campoNombre;
    private JButton    botonJugar;
    private JButton    botonLeaderboard;

    /**
     * @param onJugar        Callback que recibe el username cuando pulsa Jugar
     * @param onLeaderboard  Callback para abrir el leaderboard sin jugar
     */
    public PantallaInicio(java.util.function.Consumer<String> onJugar, Runnable onLeaderboard) {

        setTitle("Neon Breaker — Inicio");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // ---- Panel principal con fondo negro ----
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Fondo negro con grid sutil (igual que en el juego)
                g.setColor(new Color(0, 0, 0));
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(new Color(255, 0, 127, 12));
                for (int x = 0; x < getWidth(); x += 40) g.drawLine(x, 0, x, getHeight());
                for (int y = 0; y < getHeight(); y += 40) g.drawLine(0, y, getWidth(), y);
            }
        };
        panel.setLayout(new GridBagLayout()); // para centrar todo fácilmente
        panel.setPreferredSize(new Dimension(400, 380));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20); // márgenes entre elementos
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        // ---- Título ----
        JLabel titulo = new JLabel("NEON BREAKER", SwingConstants.CENTER);
        titulo.setFont(new Font("Monospaced", Font.BOLD, 28));
        titulo.setForeground(new Color(255, 0, 127));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titulo, gbc);

        // ---- Subtítulo ----
        JLabel subtitulo = new JLabel("ARCADE EXPERIENCE", SwingConstants.CENTER);
        subtitulo.setFont(new Font("Monospaced", Font.PLAIN, 12));
        subtitulo.setForeground(new Color(203, 48, 224));
        gbc.gridy = 1;
        panel.add(subtitulo, gbc);

        // ---- Separador visual ----
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(255, 0, 127));
        gbc.gridy = 2; gbc.insets = new Insets(5, 20, 20, 20);
        panel.add(sep, gbc);

        // ---- Etiqueta del campo ----
        JLabel etiqueta = new JLabel("INTRODUCE TU NICKNAME:");
        etiqueta.setFont(new Font("Monospaced", Font.BOLD, 13));
        etiqueta.setForeground(Color.WHITE);
        gbc.gridy = 3; gbc.insets = new Insets(5, 20, 5, 20);
        panel.add(etiqueta, gbc);

        // ---- Campo de texto ----
        campoNombre = new JTextField();
        campoNombre.setFont(new Font("Monospaced", Font.PLAIN, 16));
        campoNombre.setBackground(new Color(20, 20, 20));
        campoNombre.setForeground(new Color(255, 0, 127));
        campoNombre.setCaretColor(Color.WHITE);
        campoNombre.setBorder(BorderFactory.createLineBorder(new Color(203, 48, 224), 2));
        campoNombre.setHorizontalAlignment(JTextField.CENTER);
        gbc.gridy = 4;
        panel.add(campoNombre, gbc);

        // ---- Botón JUGAR ----
        botonJugar = new JButton("▷  JUGAR AHORA");
        estilizarBoton(botonJugar, new Color(255, 0, 127));
        gbc.gridy = 5; gbc.insets = new Insets(20, 20, 5, 20);
        panel.add(botonJugar, gbc);

        // ---- Botón LEADERBOARD ----
        botonLeaderboard = new JButton("⊞  VER RANKING");
        estilizarBoton(botonLeaderboard, new Color(203, 48, 224));
        gbc.gridy = 6; gbc.insets = new Insets(5, 20, 20, 20);
        panel.add(botonLeaderboard, gbc);

        // ---- Lógica del botón Jugar ----
        botonJugar.addActionListener(e -> {
            String nombre = campoNombre.getText().trim();
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Por favor, introduce un nickname.",
                    "Nickname vacío",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (nombre.length() > 20) {
                JOptionPane.showMessageDialog(this,
                    "El nickname no puede tener más de 20 caracteres.",
                    "Nickname muy largo",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            dispose(); // cerramos esta ventana
            onJugar.accept(nombre); // abrimos el juego con el nombre
        });

        // Pulsar Enter en el campo también lanza el juego
        campoNombre.addActionListener(e -> botonJugar.doClick());

        // ---- Lógica del botón Leaderboard ----
        botonLeaderboard.addActionListener(e -> onLeaderboard.run());

        add(panel);
        pack();
        setLocationRelativeTo(null); // centrar en pantalla
        setVisible(true);
    }

    /** Aplica el estilo neon a un botón */
    private void estilizarBoton(JButton boton, Color color) {
        boton.setFont(new Font("Monospaced", Font.BOLD, 14));
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Efecto hover: oscurece el botón al pasar el ratón
        boton.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { boton.setBackground(color.darker()); }
            @Override public void mouseExited(MouseEvent e)  { boton.setBackground(color); }
        });
    }
}
