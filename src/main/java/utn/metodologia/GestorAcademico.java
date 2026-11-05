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
        if (datos == null) {
            System.out.println("ERROR: Datos de solicitud invalidos");
            return null;
        }
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
        if (!"EQUIVALENCIA".equals(tipo)
                && !"PRORROGA".equals(tipo)
                && !"MESA_ESPECIAL".equals(tipo)) {
            System.out.println("ERROR: Tipo de tramite desconocido: " + tipo);
            return null;
        }

        Estudiante est = new Estudiante(legajo, nombre, email, carrera);

        Solicitud sol = new Solicitud(
                contadorId, tipo, descripcion, prioridad,
                "PENDIENTE", fecha, est
        );

        if ("EQUIVALENCIA".equals(tipo) || "MESA_ESPECIAL".equals(tipo)) {
            sol.responsable = listaResponsables.get(0);
        } else {
            sol.responsable = listaResponsables.get(1);
        }

        try {
            persistencia.guardar(sol);
        } catch (IOException e) {
            System.out.println("Error de I/O al guardar: " + e.getMessage());
            return null;
        }

        listaSolicitudes.add(sol);
        sol.responsable.casosAsignados++;
        contadorId++;

        System.out.println(
                ">>> Solicitud registrada exitosamente con ID #" + sol.id +
                        " asignada a " + sol.responsable.nombre
        );

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

        if (encontrada.responsable != resp) {
            if (encontrada.responsable != null) {
                encontrada.responsable.casosAsignados--;
            }

            encontrada.responsable = resp;
            resp.casosAsignados++;
        }
        System.out.println("Solicitud #" + idSolicitud + " reasignada a " + resp.nombre);
        return true;
    }

    // Operacion 3: Cambiar estado y dictamen
    public boolean cambiarEstado(int idSolicitud, String nuevoEstado, String dictamen) {
        Solicitud encontrada = buscarSolicitudPorId(idSolicitud);

        if (encontrada == null) return false;

        // Validaciones primitivas
        if ("APROBADA".equals(nuevoEstado)
                || "RECHAZADA".equals(nuevoEstado)
                || "EN_REVISION".equals(nuevoEstado)) {
            encontrada.estado = nuevoEstado;
            encontrada.dictamenResolucion = dictamen;
            System.out.println("Solicitud #" + idSolicitud + " paso al estado " + nuevoEstado);
            return true;
        } else {
            System.out.println("Estado invalido: " + nuevoEstado);
            return false;
        }
    }

    // Operacion 4: Reporte general
    public void imprimirReporteGeneral() {
        new ReporteSolicitudes().imprimir(listaSolicitudes);
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