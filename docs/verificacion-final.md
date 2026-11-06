# Verificación de la versión final

## Entorno y procedimiento

- Proyecto Maven con versión objetivo Java 17.
- Ejecuciones realizadas desde IntelliJ IDEA con JDK 25.
- Pruebas ejecutadas mediante la clase GestorAcademicoTest.
- Flujo principal verificado mediante Main.
- Versión del código identificada por el tag estadorefactorizado.

## Comparación antes y después

| Aspecto | Sistema base | Versión final |
|---|---|---|
| Búsqueda por ID | Duplicada en dos operaciones | Centralizada en buscarSolicitudPorId |
| Registro | Ocho parámetros String | Objeto DatosSolicitud |
| Escritura del archivo | Incluida en el gestor | Componente que implementa PersistenciaSolicitudes |
| Reporte | Construido en el gestor | Delegado a ReporteSolicitudes |
| Reasignación | Contadores inconsistentes | Descuenta al anterior y suma al nuevo |
| Asignación al mismo responsable | Incrementaba el contador | Conserva el contador |
| Datos, tipo o estado nulos | Provocaban NullPointerException | Rechazo controlado |
| IOException al guardar | Dejaba el registro en memoria y mostraba éxito | Rechaza el registro y conserva el estado en memoria |
| Subtotal EN_REVISION | Ausente | Incluido |

## Resultados de pruebas

La suite final contiene 18 pruebas, todas aprobadas.

Durante las correcciones se observaron los siguientes resultados:

| Corrección | Antes de corregir | Después de corregir |
|---|---|---|
| Contadores de responsables | 11 aprobadas y 2 fallidas | 13 aprobadas |
| Validación de valores nulos | 13 aprobadas y 3 fallidas | 16 aprobadas |
| Fallos de persistencia | 16 aprobadas y 2 fallidas | 18 aprobadas |

Después del cambio en el reporte se ejecutaron nuevamente
las 18 pruebas y todas aprobaron.

## Verificación manual del flujo principal

Main realizó las siguientes operaciones:

1. Registró una EQUIVALENCIA y una PRORROGA.
2. Reasignó la segunda solicitud al Prof. Gomez.
3. Cambió la primera a APROBADA y la segunda a EN_REVISION.
4. Mostró ambas solicitudes con sus datos actualizados en memoria.

El resumen final fue:

    TOTALES: 2 solicitudes procesadas.
    Pendientes: 0 | Aprobadas: 1 | Rechazadas: 0 | En revision: 1

La ejecución terminó con código de salida 0.

## Alcance y limitaciones

Las pruebas verifican los casos implementados y las correcciones
documentadas en code-review.md.

El reporte se verificó manualmente. Parte de las pruebas utiliza
el archivo real y otras sustituyen la persistencia.

Continúan pendientes la recuperación de datos al iniciar,
la persistencia de cambios posteriores al alta y la escritura atómica.
Una interrupción física puede dejar una línea parcial en el archivo.

## Complemento posterior a las 18 pruebas

Se agregó buscarPorEstado (complemento funcional, no es refactoring) y una
prueba que lo verifica, por lo que la suite final tiene 19 pruebas
aprobadas. Además, las pruebas iniciales dejaron de escribir en
solicitudes_db.txt al usar una persistencia sustituta.
Las evidencias anteriores (18 pruebas) se conservan como registro histórico.