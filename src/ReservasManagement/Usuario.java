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
        // Validaciones iniciales
        if (cedula == null || cedula.length() != 10 || !cedula.matches("\\d+")) {
            return false;
        }

        try {
            // Validar región (2 primeros dígitos)
            int digitoRegion = Integer.parseInt(cedula.substring(0, 2));
            if (digitoRegion < 1 || digitoRegion > 24) {
                return false;
            }

            // Obtener el último dígito
            int ultimoDigito = Integer.parseInt(cedula.substring(9, 10));

            // Calcular suma de pares
            int sumaPares = Character.getNumericValue(cedula.charAt(1)) +
                    Character.getNumericValue(cedula.charAt(3)) +
                    Character.getNumericValue(cedula.charAt(5)) +
                    Character.getNumericValue(cedula.charAt(7));

            // Calcular suma de impares
            int sumaImpares = 0;
            int[] posicionesImpares = {0, 2, 4, 6, 8};

            for (int pos : posicionesImpares) {
                int numero = Character.getNumericValue(cedula.charAt(pos)) * 2;
                if (numero > 9) {
                    numero -= 9;
                }
                sumaImpares += numero;
            }

            // Calcular suma total
            int sumaTotal = sumaPares + sumaImpares;

            // Obtener decena inmediata superior
            int decena = ((sumaTotal / 10) + 1) * 10;

            // Calcular dígito validador
            int digitoValidador = decena - sumaTotal;

            // Si el dígito validador es 10, se convierte a 0
            if (digitoValidador == 10) {
                digitoValidador = 0;
            }

            // Validar que el dígito validador sea igual al último dígito de la cédula
            return digitoValidador == ultimoDigito;

        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            return false;
        }
    }

    public static boolean validarCorreo(String correo) {
        // Expresión regular para validar correos electrónicos
        String regex = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";
        // Compilar la expresión regular
        Pattern pattern = Pattern.compile(regex);
        // Verificar si el correo coincide con la expresión regular
        return correo != null && pattern.matcher(correo).matches();
    }

    public static boolean validarCiudad(String ciudad) {
        String[] CIUDADES_ECUADOR = {
                // Costa
                "Guayaquil", "Manta", "Portoviejo", "Machala", "Babahoyo", "Quevedo", "Esmeraldas",
                "Santa Elena", "Playas", "Daule", "Milagro", "Durán", "La Libertad", "Salinas",

                // Sierra
                "Quito", "Cuenca", "Ambato", "Riobamba", "Loja", "Ibarra", "Latacunga", "Azogues",
                "Guaranda", "Tulcán", "Cayambe", "Otavalo", "Sangolquí", "Conocoto",

                // Oriente
                "Puyo", "Tena", "Macas", "Nueva Loja", "Zamora",
        };
        // Si la ciudad es null o está vacía, retorna false
        if (ciudad == null || ciudad.trim().isEmpty()) {
            return false;
        }

        // Normaliza el nombre de la ciudad (primera letra mayúscula, resto minúsculas)
        String ciudadNormalizada = normalizarNombreCiudad(ciudad);

        // Busca la ciudad en el array
        for (String ciudadEcuador : CIUDADES_ECUADOR) {
            if (ciudadEcuador.equals(ciudadNormalizada)) {
                return true;
            }
        }

        return false;
    }

    private static String normalizarNombreCiudad(String ciudad) {
        ciudad = ciudad.trim();
        if (ciudad.isEmpty()) {
            return ciudad;
        }

        // Maneja ciudades con múltiples palabras
        String[] palabras = ciudad.toLowerCase().split(" ");
        StringBuilder resultado = new StringBuilder();

        for (int i = 0; i < palabras.length; i++) {
            if (!palabras[i].isEmpty()) {
                resultado.append(Character.toUpperCase(palabras[i].charAt(0)))
                        .append(palabras[i].substring(1));
                if (i < palabras.length - 1) {
                    resultado.append(" ");
                }
            }
        }

        return resultado.toString();
    }

    public static boolean validarTelefono(String telefono) {
        if (telefono == null) return false;
        String num = telefono.replaceAll("[ -]", "");
        return num.matches("^(0[2-7][0-9]{7}|09[6-9][0-9]{7})$");
    }

    public static boolean validarNombre(String texto) {
        return texto != null && texto.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+");
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
