package AppMain;

import ReservasManagement.*;
import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private GUIController controller;
    private App app;
    private JTextField correoField;
    private JPasswordField claveField;

    public LoginPanel(GUIController controller, App app) {
        this.controller = controller;
        this.app = app;
        initializeComponents();
    }

    private void initializeComponents() {
        setLayout(new GridLayout(3, 2));

        correoField = new JTextField();
        claveField = new JPasswordField();
        JButton loginButton = new JButton("Iniciar Sesión");
        JButton registroButton = new JButton("Registrar Usuario");

        add(new JLabel("Correo:"));
        add(correoField);
        add(new JLabel("Contraseña:"));
        add(claveField);
        add(loginButton);
        add(registroButton);

        setupActions(loginButton, registroButton);
    }

    private void setupActions(JButton loginButton, JButton registroButton) {
        loginButton.addActionListener(e -> handleLogin());
        registroButton.addActionListener(e -> controller.mostrarPanelRegistro());
    }

    private void handleLogin() {
        try {
            Usuario usuario = app.ingresarUsuario(
                    correoField.getText(),
                    new String(claveField.getPassword())
            );

            correoField.setText("");
            claveField.setText("");

            if (usuario instanceof Estudiante) {
                controller.mostrarPanelEstudiante((Estudiante) usuario);
            } else {
                controller.mostrarPanelAdministrador((Administrador) usuario);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
}