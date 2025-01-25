package AppMain;


import ReservasManagement.*;
import javax.swing.*;
import java.awt.*;

public class EstudiantePanel extends JPanel {
    private GUIController controller;
    private App app;
    private Estudiante estudiante;
    private ReservaLaboratorioPanel panelLaboratorio;
    private ReservaEquipoPanel panelEquipo;
    private ReservasTablaPanel panelTabla;

    public EstudiantePanel(GUIController controller, App app) {
        this.controller = controller;
        this.app = app;
        initializeComponents();
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
        panelLaboratorio.setUsuario(estudiante);
        panelEquipo.setUsuario(estudiante);
        panelTabla.actualizarTabla(estudiante);
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());

        // Inicializar subpaneles
        panelTabla = new ReservasTablaPanel(app);
        panelLaboratorio = new ReservaLaboratorioPanel(app, panelTabla);
        panelEquipo = new ReservaEquipoPanel(app, panelTabla);

        // Crear pestañas
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Reserva de Laboratorio", panelLaboratorio);
        tabbedPane.addTab("Reserva de Equipo", panelEquipo);

        // Añadir componentes
        add(panelTabla, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton logoutButton = new JButton("Cerrar Sesión");
        logoutButton.addActionListener(e -> controller.mostrarPanelLogin());
        buttonPanel.add(logoutButton);
        return buttonPanel;
    }
}
