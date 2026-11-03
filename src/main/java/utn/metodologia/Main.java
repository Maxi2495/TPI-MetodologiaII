package utn.metodologia;

public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando Sistema de Gestion de Solicitudes Academicas...\n");

        GestorAcademico gestor = new GestorAcademico();

        // 1. Registrar solicitudes
        Solicitud s1 = gestor.registrarSolicitud(new DatosSolicitud(
                "LEG-1001", "Maximiliano Niemiec", "maxi@alumnos.utn.edu.ar",
                "TUP", "EQUIVALENCIA", "Solicito equivalencia de Algoritmos",
                "ALTA", "21/09/2026"
        ));

        Solicitud s2 = gestor.registrarSolicitud(new DatosSolicitud(
                "LEG-1002", "Paola Pasallo", "paola@alumnos.utn.edu.ar",
                "TUP", "PRORROGA", "Prorroga entrega trabajo final",
                "MEDIA", "21/09/2026"
        ));

        // 2. Asignar responsable
        gestor.asignarResponsable(s2.id, "R01");

        // 3. Cambiar estado
        gestor.cambiarEstado(s1.id, "APROBADA", "Cumple con correlatividades del plan de estudios.");
        gestor.cambiarEstado(s2.id, "EN_REVISION", "En evaluacion por comite de carrera.");

        // 4. Reporte general
        gestor.imprimirReporteGeneral();
    }
}