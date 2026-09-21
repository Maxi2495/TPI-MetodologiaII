package utn.metodologia;

public class Solicitud {
    public int id;
    public String tipo; // "EQUIVALENCIA", "PRORROGA", "MESA_ESPECIAL"
    public String descripcion;
    public String prioridad; // "ALTA", "MEDIA", "BAJA"
    public String estado; // "PENDIENTE", "EN_REVISION", "APROBADA", "RECHAZADA"
    public String fechaCreacion; // String crudo "dd/MM/yyyy"
    public Estudiante estudiante;
    public Responsable responsable;
    public String dictamenResolucion;

    public Solicitud(int id, String tipo, String descripcion, String prioridad,
                     String estado, String fechaCreacion, Estudiante estudiante) {
        this.id = id;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.prioridad = prioridad;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.estudiante = estudiante;
        this.dictamenResolucion = "SIN_RESOLUCION";
    }
}