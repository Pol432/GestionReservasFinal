package ReservasManagement;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Usuario {
    private String cedula;
    private String nombre;
    private String correo;
    private String direccion;
    private String clave;
    private String telefono;
    private List<DetalleReserva> reservas;

    public Usuario(String cedula, String nombre, String correo, String direccion, String clave, String telefono) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.correo = correo;
        this.direccion = direccion;
        this.clave = clave;
        this.telefono = telefono;
        reservas = new ArrayList<>();
    }

    public String getCedula() {
        return cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getClave() {
        return clave;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public List<DetalleReserva> getReservas() {
        return reservas;
    }

    public DetalleReservaEquipo añadirReservaEquipo(LocalDate fecha, Equipo equipo, int duracion, List<DetalleReservaEquipo> equipos) throws Exception {
        // Verificar el límite de reservas de equipos
        if (contarReservasEquipos() >= 5) {
            throw new Exception("No puede realizar más de 5 reservas de equipos");
        }

        // Verificar si el equipo ya está ocupado
        boolean equipoOcupado = equipos.stream()
                .filter(reserva -> reserva.getEquipo() == equipo)
                .anyMatch(reserva -> hayConflictoFechas(fecha, duracion, reserva.getFecha(), reserva.getDuracion()));

        if (equipoOcupado) {
            throw new Exception("Equipo ya ocupado");
        }

        // Crear y añadir la nueva reserva
        DetalleReservaEquipo reserva = new DetalleReservaEquipo(fecha, this, equipo, duracion);
        reservas.add(reserva);
        return reserva;
    }

    public DetalleReservaLaboratorio añadirReservaLaboratorio(LocalDate fecha, LocalTime horaInicio, LocalTime horaFin,
                                                              String laboratorioReservado, int numeroOcupantes, List<DetalleReservaLaboratorio> laboratorios) throws Exception {
        // Verificar el límite de reservas de laboratorios
        if (contarReservasLaboratorios() >= 3) {
            throw new Exception("No puede realizar más de 3 reservas de laboratorios");
        }

        // Verificar número máximo de ocupantes
        if (numeroOcupantes > 5) {
            throw new Exception("El número de ocupantes no puede superar 5 personas");
        }

        // Verificar si el laboratorio ya está ocupado
        boolean laboratorioOcupado = laboratorios.stream()
                .filter(reserva -> reserva.getLaboratorioReservado().equals(laboratorioReservado))
                .anyMatch(reserva -> hayConflictoFechasYHoras(
                        fecha, horaInicio, horaFin,
                        reserva.getFecha(), reserva.getHoraInicio(), reserva.getHoraFin()
                ));

        if (laboratorioOcupado) {
            throw new Exception("Laboratorio ya ocupado en el horario especificado");
        }

        // Crear y añadir la nueva reserva
        DetalleReservaLaboratorio reserva = new DetalleReservaLaboratorio(
                fecha, this, horaInicio, horaFin, laboratorioReservado, numeroOcupantes);
        reservas.add(reserva);
        return reserva;
    }

    public void eliminarReserva(DetalleReserva reserva) {
        reservas.remove(reserva);
    }

    // Contar las reservas de equipos realizadas por el estudiante
    private long contarReservasEquipos() {
        return reservas.stream()
                .filter(reserva -> reserva instanceof DetalleReservaEquipo)
                .count();
    }

    // Contar las reservas de laboratorios realizadas por el estudiante
    private long contarReservasLaboratorios() {
        return reservas.stream()
                .filter(reserva -> reserva instanceof DetalleReservaLaboratorio)
                .count();
    }

    private boolean hayConflictoFechasYHoras(
            LocalDate fecha1, LocalTime horaInicio1, LocalTime horaFin1,
            LocalDate fecha2, LocalTime horaInicio2, LocalTime horaFin2) {

        // Si las fechas son diferentes, no hay conflicto
        if (!fecha1.isEqual(fecha2)) {
            return false;
        }

        // Verifica si hay solapamiento en las horas
        return (horaInicio1.isBefore(horaFin2) || horaInicio1.equals(horaFin2)) &&
                (horaInicio2.isBefore(horaFin1) || horaInicio2.equals(horaFin1));
    }

    private boolean hayConflictoFechas(LocalDate fecha1, int duracion1, LocalDate fecha2, int duracion2) {
        LocalDate fin1 = fecha1.plusDays(duracion1);
        LocalDate fin2 = fecha2.plusDays(duracion2);

        return (fecha1.isBefore(fin2) || fecha1.isEqual(fin2)) &&
                (fecha2.isBefore(fin1) || fecha2.isEqual(fin1));
    }

    public static boolean validarcedula(String cedula) {
        // Validar que la cédula tenga exactamente 10 caracteres
        if (cedula == null || cedula.length() != 10) {
            return false;
        }

        // Validar que todos los caracteres sean numéricos
        return cedula.matches("\\d+");
    }

    public static boolean validarCorreo(String correo) {
        // Expresión regular para validar correos electrónicos
        String regex = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";
        // Compilar la expresión regular
        Pattern pattern = Pattern.compile(regex);
        // Verificar si el correo coincide con la expresión regular
        return correo != null && pattern.matcher(correo).matches();
    }

    public boolean devolverEquipo(Equipo equipo, DetalleReserva reserva) {
        // Lógica para devolución de equipo
        if (equipo != null && reserva != null) {
            equipo.setPrestado(false);
            return true;
        }
        return false;
    }
}
