package AppMain;

import ReservasManagement.*;
import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

public class ReservaLaboratorioPanel extends JPanel {
    private App app;
    private ReservasTablaPanel tablaPanel;
    private Usuario usuario;

    private JComboBox<String> laboratorioComboBox;
    private JDateChooser fechaChooser;
    private JSpinner horaInicioSpinner;
    private JSpinner horaFinSpinner;
    private JTextField ocupantesField;

    public ReservaLaboratorioPanel(App app, ReservasTablaPanel tablaPanel) {
        this.app = app;
        this.tablaPanel = tablaPanel;
        initializeComponents();
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    private void initializeComponents() {
        setLayout(new GridLayout(0, 4, 10, 10));

        // Crear componentes
        laboratorioComboBox = new JComboBox<>(App.getLaboratorios().toArray(new String[0]));

        fechaChooser = new JDateChooser();
        fechaChooser.setDateFormatString("dd/MM/yyyy");
        fechaChooser.setMinSelectableDate(new Date());

        SpinnerDateModel modeloHoraInicio = new SpinnerDateModel();
        SpinnerDateModel modeloHoraFin = new SpinnerDateModel();
        horaInicioSpinner = new JSpinner(modeloHoraInicio);
        horaFinSpinner = new JSpinner(modeloHoraFin);

        JSpinner.DateEditor editorInicio = new JSpinner.DateEditor(horaInicioSpinner, "HH:mm");
        JSpinner.DateEditor editorFin = new JSpinner.DateEditor(horaFinSpinner, "HH:mm");
        horaInicioSpinner.setEditor(editorInicio);
        horaFinSpinner.setEditor(editorFin);

        ocupantesField = new JTextField();

        // Añadir componentes
        add(new JLabel("Laboratorio:"));
        add(laboratorioComboBox);
        add(new JLabel("Fecha:"));
        add(fechaChooser);
        add(new JLabel("Hora inicio:"));
        add(horaInicioSpinner);
        add(new JLabel("Hora fin:"));
        add(horaFinSpinner);
        add(new JLabel("Número de Ocupantes:"));
        add(ocupantesField);

        // Botón de reservar
        JButton reservarButton = new JButton("Reservar Laboratorio");
        reservarButton.addActionListener(e -> realizarReserva());

        add(new JLabel(""));
        add(reservarButton);
    }

    private void realizarReserva() {
        try {
            if (fechaChooser.getDate() == null) {
                throw new Exception("Seleccione una fecha válida");
            }

            LocalDate fecha = fechaChooser.getDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            LocalTime horaInicio = ((Date) horaInicioSpinner.getValue()).toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalTime();

            LocalTime horaFin = ((Date) horaFinSpinner.getValue()).toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalTime();

            String laboratorio = (String) laboratorioComboBox.getSelectedItem();
            int ocupantes = Integer.parseInt(ocupantesField.getText());

            DetalleReservaLaboratorio reserva = usuario.añadirReservaLaboratorio(
                    fecha, horaInicio, horaFin, laboratorio, ocupantes,
                    app.getReservasLaboratorio()
            );

            app.agregarReserva(reserva);

            tablaPanel.actualizarTabla(usuario);
            limpiarCampos();

            JOptionPane.showMessageDialog(this,
                    "Laboratorio reservado con éxito",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al reservar laboratorio: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        fechaChooser.setDate(null);
        ocupantesField.setText("");
    }
}
