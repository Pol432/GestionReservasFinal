package AppMain;

import ReservasManagement.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminGestionPanel extends JPanel {
    private App app;
    private Administrador administrador;

    private JTable tablaEquipos;
    private JTable tablaLaboratorios;

    private JTextField txtNombreEquipo;
    private JTextField txtLaboratorio;

    public AdminGestionPanel(App app) {
        this.app = app;
        initializePanel();
    }

    public void setAdministrador(Administrador administrador) {
        this.administrador = administrador;
        actualizarTablaEquipos();
        actualizarTablaLaboratorios();
    }

    private void initializePanel() {
        setLayout(new BorderLayout());

        // Panel superior con tablas
        JPanel panelTablas = crearPanelTablas();
        add(panelTablas, BorderLayout.CENTER);

        // Panel inferior con opciones de gestión
        JPanel panelGestion = crearPanelGestion();
        add(panelGestion, BorderLayout.SOUTH);
    }

    private JPanel crearPanelTablas() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 10));

        // Tabla de equipos
        JPanel panelEquipos = new JPanel(new BorderLayout());
        tablaEquipos = new JTable(new DefaultTableModel(
                new String[]{"Nombre", "Estado"}, 0
        ));
        JScrollPane scrollEquipos = new JScrollPane(tablaEquipos);
        panelEquipos.add(new JLabel("Equipos", SwingConstants.CENTER), BorderLayout.NORTH);
        panelEquipos.add(scrollEquipos, BorderLayout.CENTER);
        panel.add(panelEquipos);

        // Tabla de laboratorios
        JPanel panelLaboratorios = new JPanel(new BorderLayout());
        tablaLaboratorios = new JTable(new DefaultTableModel(
                new String[]{"Laboratorio"}, 0
        ));
        JScrollPane scrollLaboratorios = new JScrollPane(tablaLaboratorios);
        panelLaboratorios.add(new JLabel("Laboratorios", SwingConstants.CENTER), BorderLayout.NORTH);
        panelLaboratorios.add(scrollLaboratorios, BorderLayout.CENTER);
        panel.add(panelLaboratorios);

        return panel;
    }

    private JPanel crearPanelGestion() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));

        // Gestión de equipos
        JPanel panelEquipos = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        panelEquipos.setBorder(BorderFactory.createTitledBorder("Gestión de Equipos"));

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelEquipos.add(new JLabel("Nombre del equipo:"), gbc);

        txtNombreEquipo = new JTextField(15);
        gbc.gridx = 1;
        panelEquipos.add(txtNombreEquipo, gbc);

        JButton btnAgregarEquipo = new JButton("Agregar Equipo");
        JButton btnEliminarEquipo = new JButton("Eliminar Equipo");

        gbc.gridx = 0;
        gbc.gridy = 1;
        panelEquipos.add(btnAgregarEquipo, gbc);

        gbc.gridx = 1;
        panelEquipos.add(btnEliminarEquipo, gbc);

        btnAgregarEquipo.addActionListener(e -> agregarEquipo());
        btnEliminarEquipo.addActionListener(e -> eliminarEquipo());

        // Gestión de laboratorios
        JPanel panelLaboratorios = new JPanel(new GridBagLayout());
        panelLaboratorios.setBorder(BorderFactory.createTitledBorder("Gestión de Laboratorios"));

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelLaboratorios.add(new JLabel("Nombre del laboratorio:"), gbc);

        txtLaboratorio = new JTextField(15);
        gbc.gridx = 1;
        panelLaboratorios.add(txtLaboratorio, gbc);

        JButton btnAgregarLaboratorio = new JButton("Agregar Laboratorio");
        JButton btnEliminarLaboratorio = new JButton("Eliminar Laboratorio");

        gbc.gridx = 0;
        gbc.gridy = 1;
        panelLaboratorios.add(btnAgregarLaboratorio, gbc);

        gbc.gridx = 1;
        panelLaboratorios.add(btnEliminarLaboratorio, gbc);

        btnAgregarLaboratorio.addActionListener(e -> agregarLaboratorio());
        btnEliminarLaboratorio.addActionListener(e -> eliminarLaboratorio());

        panel.add(panelEquipos);
        panel.add(panelLaboratorios);

        return panel;
    }

    private void actualizarTablaEquipos() {
        DefaultTableModel model = (DefaultTableModel) tablaEquipos.getModel();
        model.setRowCount(0); // Limpiar tabla
        List<Equipo> equipos = app.getEquipos();
        for (Equipo equipo : equipos) {
            model.addRow(new Object[]{equipo.getNombre(), equipo.getEstado()});
        }
    }

    private void actualizarTablaLaboratorios() {
        DefaultTableModel model = (DefaultTableModel) tablaLaboratorios.getModel();
        model.setRowCount(0); // Limpiar tabla
        List<String> laboratorios = app.getLaboratorios();
        for (String laboratorio : laboratorios) {
            model.addRow(new Object[]{laboratorio});
        }
    }

    private void agregarEquipo() {
        String nombreEquipo = txtNombreEquipo.getText().trim();
        if (nombreEquipo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un nombre válido para el equipo.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Equipo nuevoEquipo = new Equipo(nombreEquipo, "Disponible", new java.util.Date());
        app.agregarEquipo(nuevoEquipo);
        JOptionPane.showMessageDialog(this, "Equipo agregado exitosamente.");
        txtNombreEquipo.setText("");
        actualizarTablaEquipos();
    }

    private void eliminarEquipo() {
        int selectedRow = tablaEquipos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un equipo para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nombreEquipo = (String) tablaEquipos.getValueAt(selectedRow, 0);
        Equipo equipo = app.buscarEquipoPorNombre(nombreEquipo);
        if (equipo != null) {
            app.eliminarEquipo(equipo);
            JOptionPane.showMessageDialog(this, "Equipo eliminado exitosamente.");
            actualizarTablaEquipos();
        }
    }

    private void agregarLaboratorio() {
        String nombreLaboratorio = txtLaboratorio.getText().trim();
        if (nombreLaboratorio.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un nombre válido para el laboratorio.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        app.agregarLaboratorio(nombreLaboratorio);
        JOptionPane.showMessageDialog(this, "Laboratorio agregado exitosamente.");
        txtLaboratorio.setText("");
        actualizarTablaLaboratorios();
    }

    private void eliminarLaboratorio() {
        int selectedRow = tablaLaboratorios.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un laboratorio para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nombreLaboratorio = (String) tablaLaboratorios.getValueAt(selectedRow, 0);
        app.eliminarLaboratorio(nombreLaboratorio);
        JOptionPane.showMessageDialog(this, "Laboratorio eliminado exitosamente.");
        actualizarTablaLaboratorios();
    }
}
