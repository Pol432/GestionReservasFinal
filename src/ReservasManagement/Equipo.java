package ReservasManagement;

import java.util.Date;

public class Equipo {
    private String codigo;
    private String nombre;
    private String estado;
    private boolean prestado;
    private int diasDeUso; // Días acumulados de uso
    private Date fechaAdquisicion;
    private String fechaMantenimientoCorrectivo; // Fecha del último mantenimiento correctivo
    private String fechaMantenimientoPreventivo; // Fecha del último mantenimiento preventivo
    private boolean requiereMantenimientoCorrectivo;
    private boolean requiereMantenimientoPreventivo;

    public Equipo(String nombre, String estado, Date date) {
        this.nombre = nombre;
        this.diasDeUso = 0;
        this.estado = estado;
        this.prestado = false; // Por defecto, el equipo no está prestado
        this.fechaAdquisicion = date;
    }

    // Métodos getter y setter
    public String getCodigo() {
        return codigo;
    }

    public int getDiasDeUso() {
        return diasDeUso;
    }

    public void agregarDiasDeUso(int dias) {
        this.diasDeUso += dias;

        // Si supera 3 días, marcar el equipo como que requiere mantenimiento
        if (this.diasDeUso > 1) {
            this.requiereMantenimientoCorrectivo = true;
            this.requiereMantenimientoPreventivo = true;
        }
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public boolean isPrestado() {
        return prestado;
    }

    public void setPrestado(boolean prestado) {
        this.prestado = prestado;
    }

    public Date getFechaAdquisicion() {
        return fechaAdquisicion;
    }

    public void setFechaAdquisicion(Date fechaAdquisicion) {
        this.fechaAdquisicion = fechaAdquisicion;
    }

    public String getFechaMantenimientoCorrectivo() {
        return fechaMantenimientoCorrectivo;
    }

    public void setFechaMantenimientoCorrectivo(String fechaMantenimientoCorrectivo) {
        this.fechaMantenimientoCorrectivo = fechaMantenimientoCorrectivo;
    }

    public String getFechaMantenimientoPreventivo() {
        return fechaMantenimientoPreventivo;
    }

    public void setFechaMantenimientoPreventivo(String fechaMantenimientoPreventivo) {
        this.fechaMantenimientoPreventivo = fechaMantenimientoPreventivo;
    }

    // Métodos adicionales
    public boolean requiereMantenimientoCorrectivo() {
        return "Dañado".equals(estado); // Determina si el equipo necesita mantenimiento correctivo
    }

    public boolean requiereMantenimientoPreventivo() {
        return "Requiere mantenimiento preventivo".equals(estado); // Determina si requiere mantenimiento preventivo
    }

    public boolean requiereCalibracion() {
        // Lógica para determinar si el equipo necesita calibración
        return false; // Ejemplo: actualmente no se implementa
    }

    public int cantidadVecesReservada() {
        // Lógica para retornar la cantidad de veces que ha sido reservado
        return 0; // Ejemplo: actualmente no se implementa
    }
}

