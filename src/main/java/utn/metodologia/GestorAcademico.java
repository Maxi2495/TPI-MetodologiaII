package utn.metodologia;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GestorAcademico {
    // Almacenamiento en memoria y persistencia acoplada
    public List<Solicitud> listaSolicitudes = new ArrayList<>();
    public List<Responsable> listaResponsables = new ArrayList<>();
    private int contadorId = 1;
    private final PersistenciaSolicitudes persistencia;

    public GestorAcademico() {
        this(new PersistenciaSolicitudesArchivo("solicitudes_db.txt"));
    }

    public GestorAcademico(PersistenciaSolicitudes persistencia) {
        this.persistencia = persistencia;

        listaResponsables.add(new Responsable("R01", "Prof. Gomez", "Sistemas", 0));
        listaResponsables.add(new Responsable("R02", "Lic. Perez", "Alumnos", 0));
    }

    public Solicitud registrarSolicitud(DatosSolicitud datos) {
        String legajo = datos.legajo();
        String nombre = datos.nombre();
        String email = datos.email();
        String carrera = datos.carrera();
        String tipo = datos.tipo();
        String descripcion = datos.descripcion();
        String prioridad = datos.prioridad();
        String fecha = datos.fecha();

        // Validaciones defensivas mezcladas con lógica de negocio
        if (legajo == null || legajo.trim().isEmpty()) {
            System.out.println("ERROR: Legajo invalido");
            return null;
        }
        if (email == null || !email.contains("@")) {
            System.out.println("ERROR: Correo electronico invalido");
            return null;
        }
        if (!tipo.equals("EQUIVALENCIA") && !tipo.equals("PRORROGA") && !tipo.equals("MESA_ESPECIAL")) {
            System.out.println("ERROR: Tipo de tramite desconocido: " + tipo);
            return null;
        }

        Estudiante est = new Estudiante(legajo, nombre, email, carrera);
        Solicitud sol = new Solicitud(contadorId++, tipo, descripcion, prioridad, "PENDIENTE", fecha, est);

        // Lógica condicional compleja
        if (tipo.equals("EQUIVALENCIA") || tipo.equals("MESA_ESPECIAL")) {
            sol.responsable = listaResponsables.get(0); // Prof. Gomez
            listaResponsables.get(0).casosAsignados++;
        } else {
            sol.responsable = listaResponsables.get(1); // Lic. Perez
            listaResponsables.get(1).casosAsignados++;
        }

        listaSolicitudes.add(sol);

        try {
            persistencia.guardar(sol);
        } catch (IOException e) {
            System.out.println("Error de I/O al guardar: " + e.getMessage());
        }

        System.out.println(">>> Solicitud registrada exitosamente con ID #" + sol.id + " asignada a " + sol.responsable.nombre);
        return sol;
    }

    // Operacion 2: Asignar Responsable
    public boolean asignarResponsable(int idSolicitud, String idResponsable) {
        Solicitud encontrada = buscarSolicitudPorId(idSolicitud);

        if (encontrada == null) {
            System.out.println("No se encontro la solicitud con ID: " + idSolicitud);
            return false;
        }

        Responsable resp = null;
        for (Responsable r : listaResponsables) {
            if (r.id.equals(idResponsable)) {
                resp = r;
                break;
            }
        }
        if (resp == null) {
            System.out.println("No se encontro responsable con ID: " + idResponsable);
            return false;
        }

        encontrada.responsable = resp;
        resp.casosAsignados++;
        System.out.println("Solicitud #" + idSolicitud + " reasignada a " + resp.nombre);
        return true;
    }

    // Operacion 3: Cambiar estado y dictamen
    public boolean cambiarEstado(int idSolicitud, String nuevoEstado, String dictamen) {
        Solicitud encontrada = buscarSolicitudPorId(idSolicitud);

        if (encontrada == null) return false;

        // Validaciones primitivas
        if (nuevoEstado.equals("APROBADA") || nuevoEstado.equals("RECHAZADA") || nuevoEstado.equals("EN_REVISION")) {
            encontrada.estado = nuevoEstado;
            encontrada.dictamenResolucion = dictamen;
            System.out.println("Solicitud #" + idSolicitud + " paso al estado " + nuevoEstado);
            return true;
        } else {
            System.out.println("Estado invalido: " + nuevoEstado);
            return false;
        }
    }

    // Operacion 4: Buscar, Filtrar y Reporte
    public void imprimirReporteGeneral() {
        System.out.println("\n=======================================================");
        System.out.println("              REPORTE DE GESTION ACADEMICA             ");
        System.out.println("=======================================================");
        int pendientes = 0;
        int aprobadas = 0;
        int rechazadas = 0;

        for (Solicitud s : listaSolicitudes) {
            if (s.estado.equals("PENDIENTE")) pendientes++;
            else if (s.estado.equals("APROBADA")) aprobadas++;
            else if (s.estado.equals("RECHAZADA")) rechazadas++;

            System.out.println("ID: " + s.id + " | Tipo: " + s.tipo + " | Estudiante: " + s.estudiante.nombreCompleto +
                    " | Estado: " + s.estado + " | Responsable: " + s.responsable.nombre);
        }

        System.out.println("-------------------------------------------------------");
        System.out.println("TOTALES: " + listaSolicitudes.size() + " solicitudes procesadas.");
        System.out.println("Pendientes: " + pendientes + " | Aprobadas: " + aprobadas + " | Rechazadas: " + rechazadas);
        System.out.println("=======================================================\n");
    }
    private Solicitud buscarSolicitudPorId(int idSolicitud) {
        for (Solicitud solicitud : listaSolicitudes) {
            if (solicitud.id == idSolicitud) {
                return solicitud;
            }
        }
        return null;
    }
}