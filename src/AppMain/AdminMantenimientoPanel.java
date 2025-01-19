package AppMain;

import ReservasManagement.*;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class AdminMantenimientoPanel extends JPanel {
    private App app;
    private Administrador administrador;
    private JTable tablaEquipos;
    private JComboBox<String> equipoComboBox;

    public AdminMantenimientoPanel(App app) {
        this.app = app;
        initializePanel();
    }

    public void setAdministrador(Administrador administrador) {
        this.administrador = administrador;
        actualizarTablaEquipos();
        actualizarComboBoxEquipos();
    }

    private void initializePanel() {
        setLayout(new BorderLayout());

        // Panel superior con la tabla de equipos
        JPanel panelTabla = crearPanelTabla();
        add(panelTabla, BorderLayout.NORTH);

        // Panel central con opciones de mantenimiento
        JPanel panelMantenimiento = crearPanelMantenimiento();
        add(panelMantenimiento, BorderLayout.CENTER);
    }

    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());

        // Crear tabla de equipos
        tablaEquipos = new JTable(new DefaultTableModel(
                new String[]{"Equipo", "Mantenimiento Correctivo", "Mantenimiento Preventivo"}, 0
        ));
        JScrollPane scrollPane = new JScrollPane(tablaEquipos);

        // Agregar título
        JLabel titulo = new JLabel("Estado de Equipos", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(titulo, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelMantenimiento() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título de la sección
        JLabel titulo = new JLabel("Gestión de Mantenimiento", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titulo, gbc);

        // ComboBox de equipos
        gbc.gridy++;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Equipo:"), gbc);

        equipoComboBox = new JComboBox<>();
        gbc.gridx = 1;
        panel.add(equipoComboBox, gbc);

        // Botones de mantenimiento
        JButton btnPreventivo = new JButton("Realizar Mantenimiento Preventivo");
        JButton btnCorrectivo = new JButton("Realizar Mantenimiento Correctivo");
        JButton btnHistorial = new JButton("Ver Historial de Mantenimientos");

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        panel.add(btnPreventivo, gbc);

        gbc.gridy++;
        panel.add(btnCorrectivo, gbc);

        gbc.gridy++;
        panel.add(btnHistorial, gbc);

        // Configurar acciones de botones
        btnPreventivo.addActionListener(e -> realizarMantenimiento("Preventivo"));
        btnCorrectivo.addActionListener(e -> realizarMantenimiento("Correctivo"));
        btnHistorial.addActionListener(e -> mostrarHistorial());

        return panel;
    }

    private void actualizarTablaEquipos() {
        DefaultTableModel model = (DefaultTableModel) tablaEquipos.getModel();
        model.setRowCount(0); // Limpiar tabla

        List<Equipo> equipos = app.getEquipos();
        for (Equipo equipo : equipos) {
            model.addRow(new Object[]{
                    equipo.getNombre(),
                    equipo.requiereMantenimientoCorrectivo(),
                    equipo.requiereMantenimientoPreventivo()
            });
        }
    }

    private void actualizarComboBoxEquipos() {
        equipoComboBox.removeAllItems();
        List<Equipo> equiposParaMantenimiento = administrador.obtenerEquiposParaMantenimiento(app.getEquipos());
        equiposParaMantenimiento.forEach(eq -> equipoComboBox.addItem(eq.getNombre()));
    }

    private void realizarMantenimiento(String tipoMantenimiento) {
        String equipoSeleccionado = (String) equipoComboBox.getSelectedItem();
        if (equipoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un equipo.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Solicitar responsable
        String responsable = JOptionPane.showInputDialog(this, "Ingrese el nombre del responsable:");
        if (responsable == null || responsable.trim().isEmpty()) {
            return;
        }

        // Selector de fecha
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd/MM/yyyy");
        int resultado = JOptionPane.showConfirmDialog(this, dateChooser,
                "Seleccione la fecha del mantenimiento", JOptionPane.OK_CANCEL_OPTION);

        if (resultado == JOptionPane.OK_OPTION && dateChooser.getDate() != null) {
            LocalDate fechaMantenimiento = dateChooser.getDate().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate();

            Equipo equipo = app.buscarEquipoPorNombre(equipoSeleccionado);

            try {
                administrador.realizarMantenimiento(
                        equipo,
                        tipoMantenimiento,
                        fechaMantenimiento.toString(),
                        responsable,
                        app.getHistorialMantenimientos()
                );

                JOptionPane.showMessageDialog(this,
                        "Mantenimiento " + tipoMantenimiento + " realizado exitosamente.");

                actualizarTablaEquipos();
                actualizarComboBoxEquipos();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void mostrarHistorial() {
        JDialog historialDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this),
                "Historial de Mantenimientos", true);
        historialDialog.setSize(800, 400);

        // Crear tabla de historial
        String[] columnas = {"Equipo", "Tipo", "Fecha", "Responsable"};
        DefaultTableModel model = new DefaultTableModel(columnas, 0);
        JTable tablaHistorial = new JTable(model);

        // Llenar tabla con datos
        for (RegistroMantenimiento registro : app.getHistorialMantenimientos()) {
            model.addRow(new Object[]{
                    registro.getEquipo(),
                    registro.getTipoMantenimiento(),
                    registro.getFecha(),
                    registro.getResponsable()
            });
        }

        // Configurar diálogo
        JScrollPane scrollPane = new JScrollPane(tablaHistorial);
        historialDialog.add(scrollPane);

        // Botón de cerrar
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> historialDialog.dispose());
        JPanel panelBoton = new JPanel();
        panelBoton.add(btnCerrar);
        historialDialog.add(panelBoton, BorderLayout.SOUTH);

        historialDialog.setLocationRelativeTo(this);
        historialDialog.setVisible(true);
    }
}