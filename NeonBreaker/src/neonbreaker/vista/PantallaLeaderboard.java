package neonbreaker.vista;

import neonbreaker.database.PartidaDAO;
import neonbreaker.modelo.Puntuacion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Clase PantallaLeaderboard (Vista)
 * ----------------------------------------------------------
 * Muestra el TOP 10 de puntuaciones consultando la base
 * de datos. Usa un JTable para mostrar los datos en tabla.
 * ----------------------------------------------------------
 */
public class PantallaLeaderboard extends JDialog {

    public PantallaLeaderboard(JFrame padre) {
        super(padre, "Neon Breaker — TOP 10", true); // modal: bloquea la ventana padre
        setResizable(false);

        // ---- Panel principal ----
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(0, 0, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ---- Título ----
        JLabel titulo = new JLabel("▶ TOP 10 — MEJORES PARTIDAS", SwingConstants.CENTER);
        titulo.setFont(new Font("Monospaced", Font.BOLD, 16));
        titulo.setForeground(new Color(255, 0, 127));
        panel.add(titulo, BorderLayout.NORTH);

        // ---- Tabla ----
        // Las columnas de la tabla
        String[] columnas = { "#", "JUGADOR", "PUNTOS", "FECHA" };
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0) {
            // Hacemos la tabla no editable (el usuario no puede modificar las celdas)
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };

        // Cargar datos de la base de datos
        try {
            List<Puntuacion> top10 = PartidaDAO.obtenerTop10();
            int posicion = 1;
            for (Puntuacion p : top10) {
                modeloTabla.addRow(new Object[]{
                    posicion++,
                    p.getUsername(),
                    p.getPuntos(),
                    p.getFecha()
                });
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        JTable tabla = new JTable(modeloTabla);
        tabla.setBackground(new Color(15, 15, 15));
        tabla.setForeground(Color.WHITE);
        tabla.setFont(new Font("Monospaced", Font.PLAIN, 13));
        tabla.setRowHeight(28);
        tabla.setGridColor(new Color(50, 50, 50));
        tabla.getTableHeader().setBackground(new Color(255, 0, 127));
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.getTableHeader().setFont(new Font("Monospaced", Font.BOLD, 13));

        // Centrar el contenido de todas las columnas
        DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
        centrado.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < columnas.length; i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(centrado);
        }

        // Ancho de la columna # (posición)
        tabla.getColumnModel().getColumn(0).setMaxWidth(40);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setPreferredSize(new Dimension(460, 300));
        scroll.setBorder(BorderFactory.createLineBorder(new Color(203, 48, 224), 1));
        panel.add(scroll, BorderLayout.CENTER);

        // ---- Botón cerrar ----
        JButton botonCerrar = new JButton("CERRAR");
        botonCerrar.setFont(new Font("Monospaced", Font.BOLD, 13));
        botonCerrar.setBackground(new Color(203, 48, 224));
        botonCerrar.setForeground(Color.WHITE);
        botonCerrar.setFocusPainted(false);
        botonCerrar.setBorderPainted(false);
        botonCerrar.addActionListener(e -> dispose());

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBoton.setBackground(new Color(0, 0, 0));
        panelBoton.add(botonCerrar);
        panel.add(panelBoton, BorderLayout.SOUTH);

        add(panel);
        pack();
        setLocationRelativeTo(padre);
        setVisible(true);
    }
}
