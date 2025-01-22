package ReservasManagement;

import java.time.LocalDate;
import java.time.LocalTime;

public class DetalleReservaLaboratorio extends DetalleReserva{
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private int numeroOcupantes;
    private String laboratorioReservado;
    private static int reservasElectronica = 0;
    private static int reservasAutomatizacion = 0;
    private static int reservasRobotica = 0;

    public DetalleReservaLaboratorio(LocalDate fecha, Usuario usuario, LocalTime horaInicio, LocalTime horaFin,
                                     String laboratorioReservado, int numeroOcupantes) throws Exception {
        super(fecha, usuario);
        try {
            validarHoras(horaInicio, horaFin);
        } catch (Exception e) {
            throw new Exception(e);
        }
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.numeroOcupantes = numeroOcupantes;
        this.laboratorioReservado = laboratorioReservado;
    }

    public String getLaboratorioReservado() {
        return laboratorioReservado;
    }

    public void setLaboratorioReservado(String laboratorioReservado) {
        this.laboratorioReservado = laboratorioReservado;
    }

    private void validarHoras(LocalTime horaInicio, LocalTime horaFin) throws Exception {
        if (horaInicio.isAfter(horaFin)) {
            throw new Exception("La hora de inicio no puede ser posterior a la hora de fin");
        }
    }

    public static int getReservasElectronica() {
        return reservasElectronica;
    }

    public static int getReservasAutomatizacion() {
        return reservasAutomatizacion;
    }

    public static int getReservasRobotica() {
        return reservasRobotica;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public int getNumeroOcupantes() {
        return numeroOcupantes;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public void setNumeroOcupantes(int numeroOcupantes) {
        this.numeroOcupantes = numeroOcupantes;
    }

    @Override
    public String toString() {
        return "ReservasManagement.DetalleReserva{" +
                ", horaInicio=" + horaInicio +
                ", horaFin=" + horaFin +
                ", numeroOcupantes=" + numeroOcupantes +
                '}';
    }
}
