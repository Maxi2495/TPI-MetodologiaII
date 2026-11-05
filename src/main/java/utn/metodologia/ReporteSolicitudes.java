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
        int enRevision = 0;

        for (Solicitud solicitud : solicitudes) {
            if ("PENDIENTE".equals(solicitud.estado)) {
                pendientes++;
            } else if ("APROBADA".equals(solicitud.estado)) {
                aprobadas++;
            } else if ("RECHAZADA".equals(solicitud.estado)) {
                rechazadas++;
            } else if ("EN_REVISION".equals(solicitud.estado)) {
                enRevision++;
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
                        " | Rechazadas: " + rechazadas +
                        " | En revision: " + enRevision
        );
        System.out.println("=======================================================\n");
    }
}