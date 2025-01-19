package ReservasManagement;

public class Estudiante extends Usuario {
    private String carrera;
    private int semestre;

    public Estudiante(String cedula, String nombre, String correo, String direccion, String clave, String telefono, String carrera, int semestre) {
        super(cedula, nombre, correo, direccion, clave, telefono);
        this.carrera = carrera;
        this.semestre = semestre;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }
}

