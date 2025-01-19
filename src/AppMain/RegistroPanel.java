package AppMain;

import ReservasManagement.*;

import javax.swing.*;
import java.awt.*;

public class RegistroPanel extends JPanel {
    private App app;
    private GUIController guiController;

    public RegistroPanel(GUIController guiController, App app) {
        this.app = app;
        this.guiController = guiController;
        initializePanel();
    }

    private void initializePanel() {
        setLayout(new GridLayout(10, 2));

        // Componentes del formulario
        JTextField cedulaField = new JTextField();
        JTextField nombreField = new JTextField();
        JTextField correoField = new JTextField();
        JPasswordField claveField = new JPasswordField();
        JTextField telefonoField = new JTextField();
        JTextField ciudadField = new JTextField();
        JComboBox<String> tipoUsuarioBox = new JComboBox<>(new String[]{"Estudiante", "Administrador"});

        JButton registrarButton = new JButton("Registrar");
        JButton regresarButton = new JButton("Regresar");

        // Agregar componentes al panel
        add(new JLabel("Cédula:"));
        add(cedulaField);
        add(new JLabel("Nombre:"));
        add(nombreField);
        add(new JLabel("Correo:"));
        add(correoField);
        add(new JLabel("Contraseña:"));
        add(claveField);
        add(new JLabel("Teléfono:"));
        add(telefonoField);
        add(new JLabel("Ciudad:"));
        add(ciudadField);
        add(new JLabel("Tipo de Usuario:"));
        add(tipoUsuarioBox);
        add(registrarButton);
        add(regresarButton);

        // Acción del botón "Registrar"
        registrarButton.addActionListener(e -> {
            String cedula = cedulaField.getText();
            String nombre = nombreField.getText();
            String correo = correoField.getText();
            String clave = new String(claveField.getPassword());
            String telefono = telefonoField.getText();
            String ciudad = ciudadField.getText();
            String tipoUsuario = (String) tipoUsuarioBox.getSelectedItem();

            try {
                // Llama al método `agregarUsuario` de `App`
                app.agregarUsuario(cedula, nombre, correo, clave, telefono, ciudad, tipoUsuario);

                // Mostrar mensaje de éxito
                if ("Estudiante".equals(tipoUsuario)) {
                    JOptionPane.showMessageDialog(this, "Estudiante registrado con éxito.");
                } else {
                    JOptionPane.showMessageDialog(this, "Administrador registrado con éxito.");
                }

                // Limpiar campos después del registro
                limpiarCampos(cedulaField, nombreField, correoField, claveField, telefonoField, ciudadField, tipoUsuarioBox);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Acción del botón "Regresar"
        regresarButton.addActionListener(e -> guiController.mostrarPanelLogin());
    }

    private void limpiarCampos(JTextField cedulaField, JTextField nombreField, JTextField correoField,
                               JPasswordField claveField, JTextField telefonoField, JTextField ciudadField,
                               JComboBox<String> tipoUsuarioBox) {
        cedulaField.setText("");
        nombreField.setText("");
        correoField.setText("");
        claveField.setText("");
        telefonoField.setText("");
        ciudadField.setText("");
        tipoUsuarioBox.setSelectedIndex(0);
    }
}
