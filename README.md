# TPI - Metodología de Sistemas II (UTN)

## Sistema de Gestión de Solicitudes Académicas

### Integrantes

- Maximiliano Nicolás Niemiec
- Paola Pasallo

## Descripción y alcance

Aplicación de consola para registrar solicitudes académicas,
asignar responsables, cambiar estados y generar un reporte general.

El trabajo compara una versión base con una versión mejorada mediante
diagnóstico de calidad, refactoring, pruebas y revisión técnica.

## Tecnologías

- Java 17 como versión objetivo del proyecto.
- Maven para la gestión del proyecto y sus dependencias.
- JUnit Jupiter 5.10.0 para pruebas.
- IntelliJ IDEA como entorno de desarrollo.

Las ejecuciones documentadas se realizaron con JDK 25 desde IntelliJ.

## Entidades principales

- Estudiante representa al alumno solicitante.
- Solicitud contiene el trámite, su estado, prioridad y resolución.
- Responsable representa al personal asignado para atender el trámite.

## Funcionalidades

1. Registrar solicitudes con validaciones básicas.
2. Asignar automáticamente responsables según el tipo de trámite.
3. Reasignar una solicitud y actualizar los contadores de responsables.
4. Cambiar el estado y registrar un dictamen.
5. Listar solicitudes y mostrar subtotales por estado.

Los tipos admitidos son EQUIVALENCIA, PRORROGA y MESA_ESPECIAL.

Las solicitudes se crean como PENDIENTE y pueden cambiar a EN_REVISION,
APROBADA o RECHAZADA.

## Instalación y ejecución

1. Clonar el repositorio.

   git clone https://github.com/Maxi2495/TPI-MetodologiaII.git

2. Abrir la carpeta TPI-MetodologiaII en IntelliJ IDEA.
3. Importar el proyecto Maven desde pom.xml y descargar las dependencias.
4. Configurar un JDK compatible con Java 17.
5. Ejecutar el método main de la clase utn.metodologia.Main.

Main contiene una demostración que registra dos solicitudes, reasigna
una de ellas, cambia sus estados y muestra el reporte.

## Pruebas

Las pruebas se encuentran en:

    src/test/java/utn/metodologia/GestorAcademicoTest.java

Para ejecutarlas desde IntelliJ, abrir GestorAcademicoTest y seleccionar
Run sobre la clase completa.

La versión revisada cuenta con 18 pruebas aprobadas que cubren:

- Registro y asignación inicial.
- Rechazo de legajo, correo y tipo inválidos.
- Reasignación de responsables.
- Contadores al cambiar o repetir responsable.
- Cambios de estado y dictamen.
- Identificadores inexistentes.
- Datos, tipos y estados nulos.
- Delegación del guardado.
- Fallos de persistencia y reintento.

El reporte también se verificó manualmente mediante Main.

Parte de las pruebas utiliza el archivo real de persistencia.
Las pruebas que sustituyen la persistencia pueden ejecutarse sin
escribir en disco.

## Persistencia

Las altas se agregan al archivo solicitudes_db.txt, ubicado en
el directorio de trabajo de la ejecución.

Cada línea contiene:

    id;legajo;tipo;estado;nombreResponsable

La aplicación conserva las solicitudes en memoria durante la ejecución.
El archivo registra los datos del alta. Las reasignaciones y los cambios
de estado posteriores permanecen en memoria.

Cada ejecución de Main agrega nuevas líneas al archivo. Los datos
del archivo no se recuperan al iniciar y el contador de ID comienza
nuevamente en 1.

## Mejoras realizadas

- Extract Method para centralizar la búsqueda de solicitudes por ID.
- Introduce Parameter Object mediante el record DatosSolicitud.
- Separación de la escritura en PersistenciaSolicitudesArchivo,
  mediante la interfaz PersistenciaSolicitudes.
- Reubicación del reporte en ReporteSolicitudes.
- Corrección de contadores al reasignar responsables.
- Validación controlada de valores nulos.
- Conservación del estado en memoria ante IOException de persistencia.
- Inclusión del subtotal EN_REVISION.

## Documentación

- [Diagnóstico de calidad](docs/diagnostico-calidad.md)
- [Verificación del sistema base](docs/verificacion-base.md)
- [Refactorings aplicados](docs/refactorings.md)
- [Code review y checklist](docs/code-review.md)

## Versiones e hitos

- estado-inicial identifica el sistema base.
- etapa-diagnostico identifica el hito de diagnóstico.
- etapa-refactoring identifica la integración de los cuatro refactorings.
- etapa-pruebas identifica la versión con las 18 pruebas aprobadas.
- estadorefactorizado identifica la versión final del código revisado.

El tag etapa-docs se incorporará al cerrar la documentación.

## Limitaciones y mejoras pendientes

- Encapsular atributos y colecciones públicas.
- Incorporar tipos específicos para estados, trámites, prioridades y fechas.
- Separar las reglas condicionales de asignación.
- Recuperar los datos al iniciar y persistir sus modificaciones.
- Implementar escritura atómica para evitar registros parciales.
- Unificar la comunicación de errores.
- Ampliar el aislamiento y la cobertura de las pruebas.