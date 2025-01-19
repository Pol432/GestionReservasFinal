package ReservasManagement;

import java.time.LocalDate;

public class DetalleReservaEquipo extends DetalleReserva {
    private Equipo equipo;
    private int duracion; // Duración en días

    public DetalleReservaEquipo(LocalDate fecha, Usuario usuario, Equipo equipo, int duracion) {
        super(fecha, usuario);
        this.equipo = equipo;
        setDuracion(duracion);
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = Math.min(duracion, 3);
    }

    @Override
    public String toString() {
        return "ReservasManagement.DetalleReserva{" +
                ", usuario=" + super.getUsuario().getNombre() +
                ", duración (días)=" + duracion +
                ", equipoReservado='" + this.equipo.getNombre() + '\'' +
                '}';
    }
}
