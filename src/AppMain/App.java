package AppMain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import ReservasManagement.*;

public class App {
    private static List<Equipo> equipos = new ArrayList<>();
    private static List<DetalleReserva> reservas = new ArrayList<>();
    private static List<Estudiante> estudiantes = new ArrayList<>();
    private static List<Administrador> administradores = new ArrayList<>();
    private static List<RegistroMantenimiento> historialMantenimientos = new ArrayList<>();
    private static List<String> laboratorios = new ArrayList<>();

    public static List<Equipo> getEquipos() {
        return equipos;
    }

    public static List<DetalleReserva> getReservas() {
        return reservas;
    }

    public static List<Estudiante> getEstudiantes() {
        return estudiantes;
    }

    public static List<Administrador> getAdministradores() {
        return administradores;
    }

    public static List<RegistroMantenimiento> getHistorialMantenimientos() {
        return historialMantenimientos;
    }

    public static List<String> getLaboratorios() {
        return laboratorios;
    }

    public static void inicializar() {
        // Inicializar laboratorios
        laboratorios.add("UPE | -321");
        laboratorios.add("UPE | -320");
        laboratorios.add("UPE | -319");

        // Inicializar equipos
        Date fechaActual = new Date();

        Equipo e1 = new Equipo("Cautín", "Disponible", fechaActual);
        e1.setCodigo("EQ001");
        e1.setFechaMantenimientoPreventivo("2025-01-20");

        Equipo e2 = new Equipo("Osciloscopio", "Disponible", fechaActual);
        e2.setCodigo("EQ002");
        e2.setFechaMantenimientoPreventivo("2025-01-22");

        Equipo e3 = new Equipo("ESP32", "Disponible", fechaActual);
        e3.setCodigo("EQ003");
        e3.setFechaMantenimientoPreventivo("2025-01-25");

        equipos.add(e1);
        equipos.add(e2);
        equipos.add(e3);
    }


    public void agregarUsuario(String cedula, String nombre, String correo, String clave, String telefono, String ciudad, String tipoUsuario) throws Exception
    {

        if (cedula.isEmpty() || nombre.isEmpty() || correo.isEmpty() || clave.isEmpty() || telefono.isEmpty() || ciudad.isEmpty()) {
            throw new Exception("Llene todos los campos.");
        }

        if (!Usuario.validarcedula(cedula)) {
            throw new Exception("Cédula inválida. Debe ingresar 10 caracteres numéricos.");
        }

        if (!Usuario.validarCorreo(correo)) {
            throw new Exception("Correo ingresado es inválido");
        }

        // Validar que la cédula y el correo no existan ya
        boolean cedulaExiste = estudiantes.stream().anyMatch(eu -> eu.getCedula().equals(cedula)) ||
                administradores.stream().anyMatch(ad -> ad.getCedula().equals(cedula));

        boolean correoExiste = estudiantes.stream().anyMatch(eu -> eu.getCorreo().equals(correo)) ||
                administradores.stream().anyMatch(ad -> ad.getCorreo().equals(correo));

        if (cedulaExiste) {
            throw new Exception("Cédula ingresada ya existe");
        }

        if (correoExiste) {
            throw new Exception("Correo ingresado ya existe");
        }

        assert tipoUsuario != null;
        if (tipoUsuario.equals("Estudiante")) {
            Estudiante nuevoEstudiante = new Estudiante(cedula, nombre, correo, ciudad, clave, telefono, "Carrera", 1);
            estudiantes.add(nuevoEstudiante);
        } else {
            Administrador nuevoAdministrador = new Administrador(cedula, nombre, correo, ciudad, clave, telefono);
            administradores.add(nuevoAdministrador);
        }
    }


    public Usuario ingresarUsuario(String correo, String clave) throws Exception
    {
        // Validar credenciales en estudiantes
        for (Estudiante estudiante : estudiantes) {
            if (estudiante.getCorreo().equals(correo) && estudiante.getClave().equals(clave)) {
                return (Estudiante) estudiante;
            }
        }
        // Validar credenciales en administradores
        for (Administrador administrador : administradores) {
            if (administrador.getCorreo().equals(correo) && administrador.getClave().equals(clave)) {
                return (Administrador) administrador;
            }
        }

        throw new Exception("Credecianles inválidas");
    }

    public void agregarReserva(DetalleReserva reserva) {
        reservas.add(reserva);
    }

    public void eliminarReserva(DetalleReserva reserva) {
        reservas.remove(reserva);
    }

    public void agregarLaboratorio(String lab) {
        laboratorios.add(lab);
    }

    public void eliminarLaboratorio(String lab) {
        laboratorios.remove(lab);
    }

    public void agregarEquipo(Equipo equipo) {
        equipos.add(equipo);
    }


    public void eliminarEquipo(Equipo equipo) {
        equipos.remove(equipo);
    }

    public Equipo buscarEquipoPorNombre(String nombre) {
        return equipos.stream()
                .filter(eq -> eq.getNombre().equals(nombre))
                .findFirst()
                .orElse(null);
    }

    public List<DetalleReservaLaboratorio> getReservasLaboratorio() {
        return reservas.stream()
                .filter(r -> r instanceof DetalleReservaLaboratorio)
                .map(r -> (DetalleReservaLaboratorio) r)
                .collect(Collectors.toList());
    }

    public List<DetalleReservaEquipo> getReservasEquipo() {
        return reservas.stream()
                .filter(r -> r instanceof DetalleReservaEquipo)
                .map(r -> (DetalleReservaEquipo) r)
                .collect(Collectors.toList());
    }
}
