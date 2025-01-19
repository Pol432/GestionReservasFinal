package AppMain;


import ReservasManagement.*;
import javax.swing.*;

public class GUIController {
    private JFrame mainFrame;
    private App app;
    private LoginPanel loginPanel;
    private RegistroPanel registroPanel;
    private EstudiantePanel estudiantePanel;
    private AdministradorPanel administradorPanel;

    public GUIController(JFrame mainFrame, App app) {
        this.mainFrame = mainFrame;
        this.app = app;
        initializePanels();
    }

    private void initializePanels() {
        loginPanel = new LoginPanel(this, app);
        registroPanel = new RegistroPanel(this, app);
        estudiantePanel = new EstudiantePanel(this, app);
        administradorPanel = new AdministradorPanel(this, app);
    }

    public void mostrarPanelLogin() {
        switchPanel(loginPanel);
    }

    public void mostrarPanelRegistro() {
        switchPanel(registroPanel);
    }

    public void mostrarPanelEstudiante(Estudiante estudiante) {
        estudiantePanel.setEstudiante(estudiante);
        switchPanel(estudiantePanel);
    }

    public void mostrarPanelAdministrador(Administrador admin) {
        administradorPanel.setAdministrador(admin);
        switchPanel(administradorPanel);
    }

    private void switchPanel(JPanel newPanel) {
        mainFrame.getContentPane().removeAll();
        mainFrame.getContentPane().add(newPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }
}