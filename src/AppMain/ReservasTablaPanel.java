package AppMain;

import ReservasManagement.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ReservasTablaPanel extends JPanel {
    private App app;
    private JTable tablaReservas;
    private DefaultTableModel modeloReservas;

    public ReservasTablaPanel(App app) {
        this.app = app;
        initializeComponents();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());

        // Crear modelo y tabla
        modeloReservas = new DefaultTableModel(
                new String[]{"Número", "Tipo", "Fecha", "Detalles"}, 0
        );
        tablaReservas = new JTable(modeloReservas);

        // Añadir tabla con scroll
        add(new JScrollPane(tablaReservas), BorderLayout.CENTER);

        // Añadir botón de eliminar
        JButton eliminarButton = new JButton("Eliminar Reserva");
        eliminarButton.addActionListener(e -> eliminarReservaSeleccionada());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(eliminarButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void actualizarTabla(Usuario usuario) {
        modeloReservas.setRowCount(0);
        for (DetalleReserva reserva : usuario.getReservas()) {
            String detalles;
            String tipo;

            if (reserva instanceof DetalleReservaLaboratorio) {
                DetalleReservaLaboratorio reservaLab = (DetalleReservaLaboratorio) reserva;
                tipo = "Laboratorio";
                detalles = String.format("%s, %s-%s, %d ocupantes",
                        reservaLab.getLaboratorioReservado(),
                        reservaLab.getHoraInicio().toString(),
                        reservaLab.getHoraFin().toString(),
                        reservaLab.getNumeroOcupantes());
            } else {
                DetalleReservaEquipo reservaEquipo = (DetalleReservaEquipo) reserva;
                tipo = "Equipo";
                detalles = String.format("%s, %d días",
                        reservaEquipo.getEquipo().getNombre(),
                        reservaEquipo.getDuracion());
            }

            modeloReservas.addRow(new Object[]{
                    reserva.getNumeroReserva(),
                    tipo,
                    reserva.getFecha(),
                    detalles
            });
        }
    }

    private void eliminarReservaSeleccionada() {
        int filaSeleccionada = tablaReservas.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione una reserva para eliminar",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int numeroReserva = (int) modeloReservas.getValueAt(filaSeleccionada, 0);
            DetalleReserva reservaAEliminar = App.getReservas().stream()
                    .filter(r -> r.getNumeroReserva() == numeroReserva)
                    .findFirst()
                    .orElseThrow(() -> new Exception("Reserva no encontrada"));

            if (reservaAEliminar instanceof DetalleReservaEquipo reservaEquipo) {
                reservaEquipo.getEquipo().setPrestado(false);
            }

            Usuario u1 = reservaAEliminar.getUsuario();
            u1.eliminarReserva(reservaAEliminar);

            app.eliminarReserva(reservaAEliminar);
            actualizarTabla(u1);
            JOptionPane.showMessageDialog(this,
                    "Reserva eliminada con éxito",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al eliminar reserva: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}