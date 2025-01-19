package AppMain;

import ReservasManagement.*;
import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class ReservaEquipoPanel extends JPanel {
    private App app;
    private ReservasTablaPanel tablaPanel;
    private Usuario usuario;

    private JComboBox<String> equipoComboBox;
    private JDateChooser fechaChooser;
    private JSpinner duracionSpinner;

    public ReservaEquipoPanel(App app, ReservasTablaPanel tablaPanel) {
        this.app = app;
        this.tablaPanel = tablaPanel;
        initializeComponents();
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        actualizarEquiposDisponibles();
    }

    private void initializeComponents() {
        setLayout(new GridLayout(0, 4, 10, 10));

        // Crear componentes
        equipoComboBox = new JComboBox<>();
        actualizarEquiposDisponibles();

        fechaChooser = new JDateChooser();
        fechaChooser.setDateFormatString("dd/MM/yyyy");
        fechaChooser.setMinSelectableDate(new Date());

        duracionSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 3, 1));

        // Añadir componentes
        add(new JLabel("Equipo:"));
        add(equipoComboBox);
        add(new JLabel("Fecha:"));
        add(fechaChooser);
        add(new JLabel("Duración (días):"));
        add(duracionSpinner);

        // Botón de reservar
        JButton reservarButton = new JButton("Reservar Equipo");
        reservarButton.addActionListener(e -> realizarReserva());

        add(new JLabel(""));
        add(reservarButton);
    }

    private void actualizarEquiposDisponibles() {
        List<String> equiposDisponibles = App.getEquipos().stream()
                .filter(eq -> !eq.isPrestado())
                .map(Equipo::getNombre)
                .toList();

        equipoComboBox.setModel(new DefaultComboBoxModel<>(
                equiposDisponibles.toArray(new String[0])
        ));
    }

    private void realizarReserva() {
        try {
            if (fechaChooser.getDate() == null) {
                throw new Exception("Seleccione una fecha válida");
            }

            String equipoNombre = (String) equipoComboBox.getSelectedItem();
            if (equipoNombre == null) {
                throw new Exception("No hay equipos disponibles");
            }

            Equipo equipo = App.getEquipos().stream()
                    .filter(eq -> eq.getNombre().equals(equipoNombre))
                    .findFirst()
                    .orElseThrow(() -> new Exception("Equipo no encontrado"));

            LocalDate fechaInicio = fechaChooser.getDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            int duracion = (int) duracionSpinner.getValue();

            DetalleReservaEquipo reserva = usuario.añadirReservaEquipo(
                    fechaInicio, equipo, duracion,
                    App.getReservas().stream()
                            .filter(r -> r instanceof DetalleReservaEquipo)
                            .map(r -> (DetalleReservaEquipo) r)
                            .toList()
            );

            app.agregarReserva(reserva);

            equipo.setPrestado(true);
            equipo.agregarDiasDeUso(duracion);

            if (equipo.getDiasDeUso() > 3) {
                JOptionPane.showMessageDialog(this,
                        "El equipo " + equipo.getNombre() + " ahora requiere mantenimiento.",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE);
            }

            tablaPanel.actualizarTabla(usuario);
            actualizarEquiposDisponibles();
            limpiarCampos();

            JOptionPane.showMessageDialog(this,
                    "Equipo reservado con éxito",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al reservar equipo: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        fechaChooser.setDate(null);
        duracionSpinner.setValue(1);
    }
}