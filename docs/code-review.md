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