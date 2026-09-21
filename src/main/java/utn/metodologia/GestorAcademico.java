package utn.metodologia;

import java.io.*;
import java.util.*;

public class GestorAcademico {
    // Almacenamiento en memoria y persistencia acoplada
    public List<Solicitud> listaSolicitudes = new ArrayList<>();
    public List<Responsable> listaResponsables = new ArrayList<>();
    private int contadorId = 1;
    private final String ARCHIVO_DATOS = "solicitudes_db.txt";

    public GestorAcademico() {
        // hardcodeo inicial
        listaResponsables.add(new Responsable("R01", "Prof. Gomez", "Sistemas", 0));
        listaResponsables.add(new Responsable("R02", "Lic. Perez", "Alumnos", 0));
    }

    // Acá smells codes: Long method y data clump (demasiados parámetros primitivos sueltos)
    // smell code 3: Violación de SRP (valida, crea, asigna, escribe en disco e imprime por consola)
    public Solicitud registrarSolicitud(String legajo, String nombre, String email, String carrera,
                                        String tipo, String descripcion, String prioridad, String fecha) {

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

        // Persistencia directa en archivo de texto acoplada en el mismo método
        try (FileWriter fw = new FileWriter(ARCHIVO_DATOS, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(sol.id + ";" + est.legajo + ";" + sol.tipo + ";" + sol.estado + ";" + sol.responsable.nombre);
        } catch (IOException e) {
            System.out.println("Error de I/O al guardar: " + e.getMessage());
        }

        System.out.println(">>> Solicitud registrada exitosamente con ID #" + sol.id + " asignada a " + sol.responsable.nombre);
        return sol;
    }

    // Operacion 2: Asignar Responsable
    public boolean asignarResponsable(int idSolicitud, String idResponsable) {
        Solicitud encontrada = null;
        for (Solicitud s : listaSolicitudes) {
            if (s.id == idSolicitud) {
                encontrada = s;
                break;
            }
        }
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
        Solicitud encontrada = null;
        for (Solicitud s : listaSolicitudes) {
            if (s.id == idSolicitud) {
                encontrada = s;
                break;
            }
        }
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
}