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