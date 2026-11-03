package utn.metodologia;

import java.util.List;

public class ReporteSolicitudes {

    public void imprimir(List<Solicitud> solicitudes) {
        System.out.println("\n=======================================================");
        System.out.println("              REPORTE DE GESTION ACADEMICA             ");
        System.out.println("=======================================================");

        int pendientes = 0;
        int aprobadas = 0;
        int rechazadas = 0;

        for (Solicitud solicitud : solicitudes) {
            if (solicitud.estado.equals("PENDIENTE")) {
                pendientes++;
            } else if (solicitud.estado.equals("APROBADA")) {
                aprobadas++;
            } else if (solicitud.estado.equals("RECHAZADA")) {
                rechazadas++;
            }

            System.out.println(
                    "ID: " + solicitud.id +
                            " | Tipo: " + solicitud.tipo +
                            " | Estudiante: " + solicitud.estudiante.nombreCompleto +
                            " | Estado: " + solicitud.estado +
                            " | Responsable: " + solicitud.responsable.nombre
            );
        }

        System.out.println("-------------------------------------------------------");
        System.out.println("TOTALES: " + solicitudes.size() + " solicitudes procesadas.");
        System.out.println(
                "Pendientes: " + pendientes +
                        " | Aprobadas: " + aprobadas +
                        " | Rechazadas: " + rechazadas
        );
        System.out.println("=======================================================\n");
    }
}