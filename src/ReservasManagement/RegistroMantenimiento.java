package ReservasManagement;

public class RegistroMantenimiento {
    private String equipo;
    private String tipoMantenimiento;
    private String fecha;
    private String responsable;

    public RegistroMantenimiento(String equipo, String tipoMantenimiento, String fecha, String responsable) {
        this.equipo = equipo;
        this.tipoMantenimiento = tipoMantenimiento;
        this.fecha = fecha;
        this.responsable = responsable;
    }

    public String getEquipo() {
        return equipo;
    }

    public String getTipoMantenimiento() {
        return tipoMantenimiento;
    }

    public String getFecha() {
        return fecha;
    }

    public String getResponsable() {
        return responsable;
    }

}
