# Diagnóstico Técnico del Sistema Base

## 1. Alcance del diagnóstico

Se realizó una revisión estática de las clases GestorAcademico,
Estudiante, Responsable y Solicitud de la versión base.

Los hallazgos describen evidencias presentes en el código.
La verificación del comportamiento mediante ejecución y pruebas
se documentará por separado.

## 2. Calidad Externa vs. Calidad Interna

### Calidad Externa (Comportamiento Visible)

El sistema implementa las siguientes operaciones:

- Registrar solicitudes de estudiantes con validaciones básicas de
  legajo, correo electrónico y tipo de trámite.
- Asignar automáticamente un responsable según el tipo de solicitud.
- Reasignar una solicitud a otro responsable.
- Cambiar el estado y registrar un dictamen de resolución.
- Mostrar un listado general y totales de solicitudes, incluyendo
  cantidades pendientes, aprobadas y rechazadas.
- Escribir un registro parcial de cada alta en un archivo de texto.

Estas operaciones permiten representar el flujo básico de gestión
académica. Sin embargo, la implementación presenta limitaciones
funcionales que deben verificarse mediante pruebas:

- Las reasignaciones y los cambios de estado no se escriben en el archivo.
- La clase GestorAcademico no implementa la recuperación de datos
  desde el archivo al iniciar.
- Al reasignar una solicitud, se incrementa el contador del nuevo
  responsable sin descontar el del anterior. Reasignar al mismo
  responsable también incrementa su contador.
- El reporte lista las solicitudes EN_REVISION, pero no incluye
  un subtotal para ese estado.
- Los valores null en tipo o nuevoEstado pueden provocar una
  NullPointerException.
- Si falla la apertura o escritura del archivo con una IOException,
  el flujo continúa y muestra un mensaje de registro exitoso.

Por lo tanto, la existencia de las operaciones no permite afirmar
todavía que el sistema funciona sin fallos.

### Calidad Interna (Estructura y Mantenibilidad)

La estructura del código presenta problemas que dificultan su
mantenimiento, prueba y evolución:

- Baja cohesión: GestorAcademico concentra validaciones, gestión de
  solicitudes, asignación de responsables, escritura en archivo
  y presentación por consola.
- Acoplamiento a infraestructura: registrarSolicitud instancia
  directamente FileWriter y otras clases de escritura.
- Dificultad de extensión: los tipos de trámite admitidos y sus reglas
  de asignación están definidos mediante condicionales dentro del método.
- Uso de cadenas para conceptos del dominio: fechas, estados,
  prioridades y tipos de trámite se representan mediante String,
  sin tipos específicos que restrinjan los valores permitidos.
- Falta de encapsulamiento: las entidades y las listas públicas permiten
  modificar el estado desde otros componentes sin controles.
- Duplicación: la búsqueda de solicitudes por identificador se repite
  en asignarResponsable y cambiarEstado.

Estos problemas aumentan el costo del cambio y el riesgo de regresiones,
aunque algunos flujos visibles puedan ejecutarse correctamente.

## 3. Matriz de Deuda Técnica y Code Smells

| # | Code smell o debilidad | Ubicación | Evidencia | Impacto técnico | Riesgo de mantenimiento | Prioridad |
|---|---|---|---|---|---|---|
| 1 | God Class / concentración de responsabilidades | GestorAcademico.java | La clase gestiona colecciones, valida datos, registra solicitudes, asigna responsables, escribe archivos y presenta reportes. | Baja cohesión e incumplimiento de SRP. | Cambios en una responsabilidad pueden afectar otras operaciones. | Alta |
| 2 | Long Method | GestorAcademico.registrarSolicitud() | Un mismo método valida, crea entidades, asigna responsable, actualiza colecciones, escribe en disco e imprime resultados. | Dificulta la lectura y las pruebas de cada responsabilidad por separado. | Mayor riesgo de introducir errores al modificar el flujo. | Alta |
| 3 | Long Parameter List / Data Clump | GestorAcademico.registrarSolicitud() | Recibe ocho parámetros String; legajo, nombre, email y carrera forman un grupo relacionado con el estudiante. | Firma extensa y posibilidad de intercambiar argumentos del mismo tipo. | Incorporar o modificar datos obliga a revisar los puntos de llamada. | Alta |
| 4 | Primitive Obsession | Solicitud.java / GestorAcademico.java | fechaCreacion, tipo, prioridad y estado son String; se comparan literales como "APROBADA" y "EQUIVALENCIA". | Los tipos utilizados no restringen los valores del dominio ni validan fechas. | Errores de escritura, formatos inválidos y validaciones dispersas. | Media |
| 5 | Falta de encapsulamiento | Estudiante.java, Responsable.java, Solicitud.java y listas de GestorAcademico.java | Todos los atributos de las entidades y las dos listas del gestor son públicos. | El estado puede modificarse directamente sin preservar invariantes. | Otros componentes pueden introducir datos inconsistentes o alterar las colecciones. | Alta |
| 6 | Rigidez ante la extensión / problema de OCP | GestorAcademico.registrarSolicitud() | Los tipos válidos se enumeran en un condicional y la asignación utiliza otro condicional con posiciones fijas de la lista de responsables. | Las reglas de clasificación y asignación están incrustadas en el registro. | Agregar un tipo de trámite exige modificar la lógica existente y verificar sus ramificaciones. | Alta |
| 7 | Acoplamiento a entrada/salida / problema de DIP | GestorAcademico.registrarSolicitud() | Instancia FileWriter, BufferedWriter y PrintWriter dentro de la lógica de registro. | La lógica depende directamente de una implementación de almacenamiento. | Dificulta sustituir la persistencia y ejecutar pruebas aisladas del sistema de archivos. | Alta |
| 8 | Manejo de errores poco consistente | GestorAcademico.java | Combina mensajes por consola, retornos null o false y captura de IOException sin comunicar el fallo al llamador. | Los consumidores no reciben una representación uniforme de los errores. | Posibles accesos a null y dificultad para distinguir un registro persistido de uno que solo quedó en memoria. | Alta |

## 4. Criterio de Priorización

Se asignó prioridad alta a los problemas que afectan la integridad
del estado, la comunicación de errores, la separación de responsabilidades
y la posibilidad de realizar pruebas aisladas.

El uso de tipos específicos para los conceptos del dominio se clasificó
con prioridad media dentro de esta propuesta. Debe abordarse de manera
coordinada con las validaciones y las pruebas.

Las prioridades son una valoración cualitativa basada en la revisión
del código, no en métricas de fallos en producción.

## 5. Orientación de las Mejoras

A partir del diagnóstico, se propone:

- Aplicar Extract Method para separar tareas dentro del registro.
- Reubicar la persistencia y la presentación en componentes específicos.
- Introducir un objeto de parámetros para simplificar el registro.
- Encapsular los atributos y las colecciones.
- Evaluar enums para estados, prioridades y tipos de trámite,
  y LocalDate para fechas.
- Separar las reglas de asignación de responsables.
- Unificar el tratamiento de errores.
- Centralizar la búsqueda de solicitudes por identificador.

Los cambios estructurales deben verificarse mediante pruebas antes
y después. Las correcciones de defectos funcionales deben documentarse
por separado, indicando qué comportamiento cambia y por qué.