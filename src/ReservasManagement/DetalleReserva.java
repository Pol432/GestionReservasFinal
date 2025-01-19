package ReservasManagement;

import java.time.LocalDate;

public class DetalleReserva {
    private static int contadorReservas = 0; // Contador de reservas global
    private int numeroReserva;
    private LocalDate fecha;
    private Usuario usuario;

    // Constructor
    public DetalleReserva(LocalDate fecha, Usuario usuario) {
        this.fecha = fecha;
        this.usuario = usuario;
        this.numeroReserva = generarNumeroReserva();
    }

    public static int getContadorReservas() {
        return contadorReservas;
    }

    private int generarNumeroReserva() {
        contadorReservas++;
        return contadorReservas;
    }

    public int getNumeroReserva() {
        return numeroReserva;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}