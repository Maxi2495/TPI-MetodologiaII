package utn.metodologia;

public class Responsable {
    public String id;
    public String nombre;
    public String departamento;
    public int casosAsignados;

    public Responsable(String id, String nombre, String departamento, int casosAsignados) {
        this.id = id;
        this.nombre = nombre;
        this.departamento = departamento;
        this.casosAsignados = casosAsignados;
    }
}