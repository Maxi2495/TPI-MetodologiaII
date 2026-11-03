package utn.metodologia;

public record DatosSolicitud(
        String legajo,
        String nombre,
        String email,
        String carrera,
        String tipo,
        String descripcion,
        String prioridad,
        String fecha
) {
}