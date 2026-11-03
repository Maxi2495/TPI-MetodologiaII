# Verificación del sistema base

## Entorno

- Proyecto Maven configurado para Java 17.
- Ejecución realizada desde IntelliJ con JDK 25.
- Versión base identificada mediante el tag estado-inicial.

## Procedimiento

Se ejecutó Main.main(), que registra dos solicitudes, reasigna
la segunda, cambia sus estados y genera un reporte general.
Luego se inspeccionó solicitudes_db.txt.

## Resultados observados

| Operación | Resultado |
|---|---|
| Registrar EQUIVALENCIA | Solicitud 1 creada y asignada a Prof. Gomez. |
| Registrar PRORROGA | Solicitud 2 creada y asignada a Lic. Perez. |
| Reasignar solicitud 2 | Responsable actualizado a Prof. Gomez en memoria. |
| Cambiar estado de solicitud 1 | Estado APROBADA en memoria. |
| Cambiar estado de solicitud 2 | Estado EN_REVISION en memoria. |
| Generar reporte | Se listaron las dos solicitudes con sus datos actualizados. |
| Finalizar ejecución | Código de salida 0. |

## Limitaciones verificadas

### Reporte incompleto por estado

El reporte mostró dos solicitudes, pero los subtotales sumaron una:

- Pendientes: 0.
- Aprobadas: 1.
- Rechazadas: 0.

La solicitud EN_REVISION se muestra en el listado, pero no tiene
un subtotal propio.

### Archivo sin actualizaciones posteriores al alta

El archivo contenía:

    1;LEG-1001;EQUIVALENCIA;PENDIENTE;Prof. Gomez
    2;LEG-1002;PRORROGA;PENDIENTE;Lic. Perez

Los estados y el responsable de la segunda solicitud no coinciden
con el estado final en memoria. La escritura registra datos del alta,
pero no las modificaciones posteriores.

## Alcance de la evidencia

Esta ejecución verifica el flujo principal con datos válidos.
No demuestra ausencia de errores ni cubre entradas inválidas,
valores null, fallos de escritura o recuperación de datos al reiniciar.

Los defectos observados deben distinguirse de los cambios estructurales
al comparar la versión base con la versión refactorizada.