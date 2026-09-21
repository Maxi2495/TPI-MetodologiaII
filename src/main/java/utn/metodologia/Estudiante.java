package utn.metodologia;

public class Estudiante {
    public String legajo;
    public String nombreCompleto;
    public String email;
    public String carrera;

    public Estudiante(String legajo, String nombreCompleto, String email, String carrera) {
        this.legajo = legajo;
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.carrera = carrera;
    }
}