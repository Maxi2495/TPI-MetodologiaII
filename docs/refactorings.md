# Registro de refactorings

## 1. Extract Method: búsqueda de solicitudes

- Archivo: src/main/java/utn/metodologia/GestorAcademico.java.
- Problema: asignarResponsable y cambiarEstado repetían la búsqueda
  de una solicitud por identificador.
- Cambio: se extrajo el método privado buscarSolicitudPorId,
  utilizado por ambas operaciones.
- Comportamiento conservado: devuelve la solicitud encontrada
  o null cuando el identificador no existe.
- Beneficio: elimina duplicación y centraliza la búsqueda.
- Verificación: las 10 pruebas de GestorAcademicoTest pasaron
  antes y después del cambio, ejecutadas desde IntelliJ.
- Límite: estas pruebas no cubren todos los comportamientos del sistema.

## 2. Introduce Parameter Object: datos de registro

- Archivos: DatosSolicitud.java, GestorAcademico.java, Main.java
  y GestorAcademicoTest.java.
- Problema: registrarSolicitud recibía ocho parámetros String.
- Cambio: se creó el record DatosSolicitud, se migraron las llamadas
  de Main y de las pruebas y se eliminó la firma anterior.
- Beneficio: agrupa los datos de entrada y simplifica la firma del método.
- Comportamiento conservado: se mantienen las validaciones y operaciones
  del registro; no se agregaron reglas de negocio.
- Verificación: las 10 pruebas pasaron después de migrar las llamadas
  y nuevamente después de eliminar el método anterior.
- Límite: el constructor del record todavía recibe ocho String;
  no elimina por sí solo el riesgo de intercambiar sus argumentos.