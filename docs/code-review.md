# Code review

## Hallazgo CR-01 — Contadores incorrectos al reasignar

- Ubicación: GestorAcademico.asignarResponsable().
- Prioridad: alta.
- Observación: al cambiar de responsable se incrementaba el contador
  del nuevo sin descontar el caso del anterior. Asignar nuevamente
  al mismo responsable también incrementaba su contador.
- Corrección: actualizar los contadores únicamente cuando cambia
  el responsable, descontando el caso del anterior.
- Criterios de aceptación:
    - El responsable anterior pierde un caso al reasignar.
    - El nuevo responsable recibe un caso.
    - Repetir la asignación al mismo responsable conserva el contador.
- Evidencia previa: las dos pruebas nuevas fallaron.
  Se obtuvo 1 cuando se esperaba 0 para el responsable anterior,
  y 2 cuando se esperaba 1 al repetir la asignación.
- Evidencia posterior: las 13 pruebas de GestorAcademicoTest pasaron.
- Clasificación: corrección de un defecto funcional detectado
  durante la revisión técnica.

## Hallazgo CR-02 — Valores null sin validación

- Ubicación: GestorAcademico.registrarSolicitud() y cambiarEstado().
- Prioridad: alta.
- Observación: datos, tipo o nuevoEstado nulos provocaban
  NullPointerException.
- Corrección: validar datos antes de acceder a sus campos y comparar
  tipos y estados mediante equals invocado sobre los literales.
- Criterios de aceptación:
    - Un registro con datos o tipo nulos devuelve null.
    - El registro rechazado conserva las listas y los contadores.
    - Un cambio con estado nulo devuelve false y conserva el estado
      y el dictamen anteriores.
- Evidencia previa: 13 pruebas aprobadas y 3 fallidas.
- Evidencia posterior: las 16 pruebas pasaron.
- Clasificación: corrección de validaciones detectada en code review.

## Hallazgo CR-03 — Registro exitoso aunque fallaba el guardado

- Ubicación: GestorAcademico.registrarSolicitud()
  y PersistenciaSolicitudesArchivo.guardar().
- Prioridad: alta.
- Observación: el gestor actualizaba la memoria antes de guardar
  y mostraba éxito incluso después de recibir una IOException.
  PrintWriter también podía ocultar errores de escritura.
- Corrección: guardar antes de actualizar la lista, los contadores
  y el próximo ID. Ante IOException, devolver null.
  Se reemplazó PrintWriter por BufferedWriter para propagar errores.
- Criterios de aceptación:
  - Un fallo de guardado devuelve null y conserva el estado en memoria.
  - Un reintento exitoso utiliza el ID que seguía disponible.
- Evidencia previa: 16 pruebas aprobadas y 2 fallidas.
- Evidencia posterior: 18 pruebas aprobadas.
  Main completó el flujo principal con código de salida 0.
- Límite: una escritura interrumpida puede dejar una línea parcial.
  La escritura del archivo todavía no es atómica.
- Clasificación: corrección del manejo de errores de persistencia.

## Hallazgo CR-04 — Subtotal omitido en el reporte

- Ubicación: ReporteSolicitudes.imprimir().
- Prioridad: media.
- Observación: las solicitudes EN_REVISION aparecían en el listado,
  pero faltaban en los subtotales.
- Corrección: agregar el contador y mostrar el subtotal En revision.
- Criterio de aceptación: el ejemplo de Main muestra dos solicitudes,
  una aprobada y una en revisión, con subtotales que suman dos.
- Verificación manual: Main mostró Aprobadas: 1 y En revision: 1,
  y terminó con código de salida 0.
- Clasificación: corrección funcional del reporte.

## Checklist de revisión técnica

| Criterio | Resultado | Evidencia o pendiente |
|---|---|---|
| Búsquedas sin duplicación | Cumple | buscarSolicitudPorId centraliza la búsqueda. |
| Parámetros de registro agrupados | Cumple | Se utiliza DatosSolicitud. |
| Escritura separada del gestor | Cumple | PersistenciaSolicitudesArchivo implementa una interfaz específica. |
| Presentación del reporte separada | Cumple | ReporteSolicitudes concentra el listado y los subtotales. |
| Contadores correctos al reasignar | Cumple | Pruebas de cambio y repetición de responsable. |
| Validación de datos, tipo y estado nulos | Cumple | Tres pruebas de rechazo controlado. |
| Estado en memoria conservado ante IOException | Cumple | Pruebas de fallo de guardado y reintento. |
| Subtotales para los cuatro estados | Cumple | Verificación manual mediante Main. |
| Encapsulamiento de entidades y colecciones | Pendiente | Los atributos y listas continúan siendo públicos. |
| Tipos específicos para estados, tipos y fechas | Pendiente | Continúan representados mediante String. |
| Extensión de reglas de asignación | Pendiente | La asignación sigue utilizando condicionales y posiciones de lista. |
| Recuperación y actualización de datos persistidos | Pendiente | El archivo registra altas y no se recupera al iniciar. |
| Escritura atómica | Pendiente | Un fallo físico puede dejar una línea parcial. |
| Pruebas aisladas del disco | Parcial | Las pruebas nuevas permiten sustituir la persistencia; las iniciales usan archivo. |
| Manejo uniforme de errores | Parcial | Se conservan retornos null o false y mensajes por consola. |

## Criterios de aceptación del alcance realizado

- Los cuatro refactorings están documentados.
- Las 18 pruebas deben aprobar antes de integrar la rama.
- Main debe completar el flujo principal con código de salida 0.
- El reporte debe incluir el subtotal EN_REVISION.
- Los hallazgos CR-01 a CR-04 cuentan con correcciones y evidencias.
- La deuda técnica restante queda identificada para futuras mejoras.

## Mejoras pendientes

Se propone abordar en futuras iteraciones el encapsulamiento,
los tipos del dominio, la extensión de reglas de asignación,
la recuperación de datos y la escritura atómica.

La aceptación se limita a los cambios y casos verificados.