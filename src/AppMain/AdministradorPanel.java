package AppMain;

import ReservasManagement.*;

import javax.swing.*;
import java.awt.*;

public class AdministradorPanel extends JPanel {
    private GUIController guiController;
    private App app;
    private Administrador administrador;
    private CardLayout cardLayout;
    private JPanel contentPanel;

    // Paneles de reserva
    private ReservasTablaPanel reservaTablaPanel;
    private ReservaEquipoPanel reservaEquipoPanel;
    private ReservaLaboratorioPanel reservaLaboratorioPanel;

    // Otros paneles
    private AdminMantenimientoPanel mantenimientoPanel;
    private AdminGestionPanel gestionPanel;

    // Panel contenedor para reservas
    private JPanel reservasContainer;

    public AdministradorPanel(GUIController guiController, App app) {
        this.guiController = guiController;
        this.app = app;
        initializePanel();
    }

    public void setAdministrador(Administrador administrador) {
        this.administrador = administrador;
        // Actualizar todos los paneles con el nuevo administrador
        mantenimientoPanel.setAdministrador(administrador);
        gestionPanel.setAdministrador(administrador);

        // Actualizar paneles de reserva
        reservaTablaPanel.actualizarTabla(administrador);
        reservaEquipoPanel.setUsuario(administrador);
        reservaLaboratorioPanel.setUsuario(administrador);
    }

    private void initializePanel() {
        setLayout(new BorderLayout());

        // Panel de menú superior
        JPanel menuPanel = new JPanel();
        JButton btnReservas = new JButton("Gestionar Reservas");
        JButton btnGestion = new JButton("Gestionar Equipos/Labs");
        JButton btnMantenimiento = new JButton("Gestionar Mantenimientos");
        JButton btnSalir = new JButton("Salir");

        menuPanel.add(btnReservas);
        menuPanel.add(btnGestion);
        menuPanel.add(btnMantenimiento);
        menuPanel.add(btnSalir);
        add(menuPanel, BorderLayout.NORTH);

        // Panel de contenido con CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // Inicializar todos los paneles
        initializePanels();

        // Agregar subpaneles al contenido
        contentPanel.add(reservasContainer, "RESERVAS");
        contentPanel.add(gestionPanel, "GESTION");
        contentPanel.add(mantenimientoPanel, "MANTENIMIENTO");

        add(contentPanel, BorderLayout.CENTER);

        // Configurar acciones de botones
        btnReservas.addActionListener(e -> cardLayout.show(contentPanel, "RESERVAS"));
        btnGestion.addActionListener(e -> cardLayout.show(contentPanel, "GESTION"));
        btnMantenimiento.addActionListener(e -> cardLayout.show(contentPanel, "MANTENIMIENTO"));
        btnSalir.addActionListener(e -> guiController.mostrarPanelLogin());
    }

    private void initializePanels() {
        // Inicializar paneles administrativos
        mantenimientoPanel = new AdminMantenimientoPanel(app);
        gestionPanel = new AdminGestionPanel(app);

        // Inicializar paneles de reserva
        reservaTablaPanel = new ReservasTablaPanel(app);
        reservaEquipoPanel = new ReservaEquipoPanel(app, reservaTablaPanel);
        reservaLaboratorioPanel = new ReservaLaboratorioPanel(app, reservaTablaPanel);

        // Crear contenedor para los paneles de reserva
        reservasContainer = new JPanel(new BorderLayout());

        // Panel superior para la tabla de reservas
        reservasContainer.add(reservaTablaPanel, BorderLayout.NORTH);

        // Panel inferior con pestañas para reserva de equipo y laboratorio
        JTabbedPane reservasTabbedPane = new JTabbedPane();
        reservasTabbedPane.addTab("Reservar Equipo", reservaEquipoPanel);
        reservasTabbedPane.addTab("Reservar Laboratorio", reservaLaboratorioPanel);

        reservasContainer.add(reservasTabbedPane, BorderLayout.CENTER);
    }
}
