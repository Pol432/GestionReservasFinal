package ReservasManagement;

import java.time.LocalDate;
import java.time.LocalTime;

public class DetalleReservaLaboratorio extends DetalleReserva{
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String laboratorioReservado; // Puede ser "Electrónica", "Automatización" o "Robótica"
    private int numeroOcupantes;
    private static int reservasElectronica = 0;
    private static int reservasAutomatizacion = 0;
    private static int reservasRobotica = 0;

    public DetalleReservaLaboratorio(LocalDate fecha, Usuario usuario, LocalTime horaInicio, LocalTime horaFin,
                          String laboratorioReservado, int numeroOcupantes) {
        super(fecha, usuario);
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.laboratorioReservado = laboratorioReservado;
        this.numeroOcupantes = numeroOcupantes;
        registrarReserva(laboratorioReservado);
    }

    // Método para registrar una reserva en el laboratorio correspondiente
    private void registrarReserva(String laboratorioReservado) {
        switch (laboratorioReservado.toLowerCase()) {
            case "electrónica":
                reservasElectronica++;
                break;
            case "automatización":
                reservasAutomatizacion++;
                break;
            case "robótica":
                reservasRobotica++;
                break;
            default:
                System.out.println("Laboratorio no válido");
                break;
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

    public String getLaboratorioReservado() {
        return laboratorioReservado;
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

    public void setLaboratorioReservado(String laboratorioReservado) {
        this.laboratorioReservado = laboratorioReservado;
    }

    public void setNumeroOcupantes(int numeroOcupantes) {
        this.numeroOcupantes = numeroOcupantes;
    }

    @Override
    public String toString() {
        return "ReservasManagement.DetalleReserva{" +
                ", horaInicio=" + horaInicio +
                ", horaFin=" + horaFin +
                ", laboratorioReservado='" + laboratorioReservado + '\'' +
                ", numeroOcupantes=" + numeroOcupantes +
                '}';
    }
}
