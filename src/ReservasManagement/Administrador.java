package ReservasManagement;

import java.util.List;
import java.util.stream.Collectors;

public class Administrador extends Usuario {

    public Administrador(String cedula, String nombre, String correo, String direccion, String clave, String telefono) {
        super(cedula, nombre, correo, direccion, clave, telefono);
    }

    public void realizarMantenimiento(Equipo equipo, String tipoMantenimiento, String fechaMantenimiento, String responsable, List<RegistroMantenimiento> historialMantenimientos) {
        if (equipo == null) {
            throw new IllegalArgumentException("Equipo no puede ser null");
        }

        validarDatosMantenimiento(tipoMantenimiento, fechaMantenimiento, responsable);

        if ("Correctivo".equalsIgnoreCase(tipoMantenimiento)) {
            equipo.setEstado("Operativo");
            equipo.setFechaMantenimientoCorrectivo(fechaMantenimiento);
        } else if ("Preventivo".equalsIgnoreCase(tipoMantenimiento)) {
            equipo.setEstado("Operativo");
            equipo.setFechaMantenimientoPreventivo(fechaMantenimiento);
        } else {
            throw new IllegalArgumentException("Tipo de mantenimiento no válido");
        }

        // Registrar el mantenimiento en el historial
        registrarMantenimiento(equipo.getNombre(), tipoMantenimiento, fechaMantenimiento, responsable, historialMantenimientos);
    }

    private void validarDatosMantenimiento(String tipoMantenimiento, String fechaMantenimiento, String responsable) {
        if (responsable == null || responsable.trim().isEmpty()) {
            throw new IllegalArgumentException("Responsable no puede estar vacío");
        }
        if (fechaMantenimiento == null || fechaMantenimiento.trim().isEmpty()) {
            throw new IllegalArgumentException("Fecha no puede estar vacía");
        }
        if (tipoMantenimiento == null || (!tipoMantenimiento.equalsIgnoreCase("Correctivo") &&
                !tipoMantenimiento.equalsIgnoreCase("Preventivo"))) {
            throw new IllegalArgumentException("Tipo de mantenimiento no válido");
        }
    }

    private void registrarMantenimiento(String nombreEquipo, String tipoMantenimiento,
                                        String fecha, String responsable,
                                        List<RegistroMantenimiento> historialMantenimientos) {
        RegistroMantenimiento registro = new RegistroMantenimiento(
                nombreEquipo, tipoMantenimiento, fecha, responsable);
        historialMantenimientos.add(registro);
    }

    public List<Equipo> obtenerEquiposParaMantenimiento(List<Equipo> equipos) {
        return equipos.stream()
                .filter(eq -> eq.requiereMantenimientoCorrectivo() || eq.requiereMantenimientoPreventivo())
                .collect(Collectors.toList());
    }
}