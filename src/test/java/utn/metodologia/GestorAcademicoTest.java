package utn.metodologia;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GestorAcademicoTest {

    @Test
    void debeRegistrarEquivalenciaYAsignarResponsable() {
        GestorAcademico gestor = new GestorAcademico();

        Solicitud solicitud = gestor.registrarSolicitud(new DatosSolicitud(
                "LEG-TEST-001",
                "Estudiante de prueba",
                "estudiante@example.com",
                "TUP",
                "EQUIVALENCIA",
                "Solicitud de prueba",
                "ALTA",
                "02/11/2026"
        ));

        assertNotNull(solicitud);
        assertEquals(1, solicitud.id);
        assertEquals("PENDIENTE", solicitud.estado);
        assertNotNull(solicitud.responsable);
        assertEquals("R01", solicitud.responsable.id);
        assertEquals(1, gestor.listaSolicitudes.size());
        assertEquals(1, solicitud.responsable.casosAsignados);
    }

    @Test
    void debeRechazarLegajoVacio() {
        GestorAcademico gestor = new GestorAcademico();

        Solicitud solicitud = gestor.registrarSolicitud(new DatosSolicitud(
                "", "Estudiante", "estudiante@example.com", "TUP",
                "EQUIVALENCIA", "Prueba", "ALTA", "02/11/2026"
        ));

        assertNull(solicitud);
        assertTrue(gestor.listaSolicitudes.isEmpty());
        assertEquals(0, gestor.listaResponsables.get(0).casosAsignados);
    }

    @Test
    void debeRechazarCorreoInvalido() {
        GestorAcademico gestor = new GestorAcademico();

        Solicitud solicitud = gestor.registrarSolicitud(new DatosSolicitud(
                "LEG-001", "Estudiante", "correo-invalido", "TUP",
                "EQUIVALENCIA", "Prueba", "ALTA", "02/11/2026"
        ));

        assertNull(solicitud);
        assertTrue(gestor.listaSolicitudes.isEmpty());
    }

    @Test
    void debeRechazarTipoDesconocido() {
        GestorAcademico gestor = new GestorAcademico();

        Solicitud solicitud = gestor.registrarSolicitud(new DatosSolicitud(
                "LEG-001", "Estudiante", "estudiante@example.com", "TUP",
                "DESCONOCIDO", "Prueba", "ALTA", "02/11/2026"
        ));

        assertNull(solicitud);
        assertTrue(gestor.listaSolicitudes.isEmpty());
    }

    @Test
    void debeAsignarProrrogaAlAreaDeAlumnos() {
        GestorAcademico gestor = new GestorAcademico();

        Solicitud solicitud = registrarProrroga(gestor);

        assertNotNull(solicitud);
        assertEquals("R02", solicitud.responsable.id);
        assertEquals(1, solicitud.responsable.casosAsignados);
    }

    @Test
    void debeReasignarResponsable() {
        GestorAcademico gestor = new GestorAcademico();
        Solicitud solicitud = registrarProrroga(gestor);

        boolean resultado = gestor.asignarResponsable(solicitud.id, "R01");

        assertTrue(resultado);
        assertEquals("R01", solicitud.responsable.id);
    }

    @Test
    void debeRechazarResponsableInexistenteSinCambiarAsignacion() {
        GestorAcademico gestor = new GestorAcademico();
        Solicitud solicitud = registrarProrroga(gestor);

        boolean resultado = gestor.asignarResponsable(solicitud.id, "R99");

        assertFalse(resultado);
        assertEquals("R02", solicitud.responsable.id);
    }

    @Test
    void debeCambiarEstadoYGuardarDictamen() {
        GestorAcademico gestor = new GestorAcademico();
        Solicitud solicitud = registrarProrroga(gestor);

        boolean resultado = gestor.cambiarEstado(
                solicitud.id, "APROBADA", "Documentacion completa"
        );

        assertTrue(resultado);
        assertEquals("APROBADA", solicitud.estado);
        assertEquals("Documentacion completa", solicitud.dictamenResolucion);
    }

    @Test
    void debeRechazarEstadoInvalidoSinModificarSolicitud() {
        GestorAcademico gestor = new GestorAcademico();
        Solicitud solicitud = registrarProrroga(gestor);

        boolean resultado = gestor.cambiarEstado(
                solicitud.id, "DESCONOCIDO", "Dictamen de prueba"
        );

        assertFalse(resultado);
        assertEquals("PENDIENTE", solicitud.estado);
        assertEquals("SIN_RESOLUCION", solicitud.dictamenResolucion);
    }

    @Test
    void debeDevolverFalseCuandoLaSolicitudNoExiste() {
        GestorAcademico gestor = new GestorAcademico();

        assertFalse(gestor.asignarResponsable(999, "R01"));
        assertFalse(gestor.cambiarEstado(999, "APROBADA", "Prueba"));
    }

    private Solicitud registrarProrroga(GestorAcademico gestor) {
        return gestor.registrarSolicitud(new DatosSolicitud(
                "LEG-TEST-002", "Estudiante de prueba",
                "estudiante@example.com", "TUP",
                "PRORROGA", "Solicitud de prueba", "MEDIA", "02/11/2026"
        ));
    }
    @Test
    void debeGuardarSolicitudUsandoLaPersistenciaRecibida() {
        java.util.List<Solicitud> guardadas = new java.util.ArrayList<>();

        PersistenciaSolicitudes persistenciaDePrueba = solicitud -> {
            guardadas.add(solicitud);
        };

        GestorAcademico gestor = new GestorAcademico(persistenciaDePrueba);

        Solicitud solicitud = registrarProrroga(gestor);

        assertNotNull(solicitud);
        assertEquals(1, guardadas.size());
        assertSame(solicitud, guardadas.get(0));
        assertEquals("R02", guardadas.get(0).responsable.id);
    }
}