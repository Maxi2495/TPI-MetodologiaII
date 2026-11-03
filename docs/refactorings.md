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

## 3. Reubicación de responsabilidad: persistencia de solicitudes

- Problema: registrarSolicitud incluía directamente la escritura del archivo.
- Cambio: se trasladó la escritura a PersistenciaSolicitudesArchivo,
  que implementa la interfaz PersistenciaSolicitudes.
- GestorAcademico recibe la persistencia mediante su constructor.
  Se conserva un constructor por defecto que utiliza solicitudes_db.txt.
- Beneficio: separa la infraestructura de la gestión de solicitudes
  y permite sustituir el almacenamiento en las pruebas.
- Principios: mejora SRP y aplica DIP mediante una interfaz específica.
- Comportamiento conservado: mismo formato de archivo, escritura
  al final del archivo y manejo de IOException existente.
- Verificación: pasaron las 10 pruebas anteriores y una nueva prueba
  que comprueba la delegación del guardado sin escribir en disco.
- Límite: las pruebas anteriores todavía utilizan la persistencia real.
  No se corrigieron la falta de actualización del archivo ni el manejo
  de errores de escritura.