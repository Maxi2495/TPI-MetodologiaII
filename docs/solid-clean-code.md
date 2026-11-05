# Aplicación de SOLID y Clean Code

## SRP — Responsabilidad única

La versión base concentraba validación, registro, asignación,
escritura y presentación en GestorAcademico.

Se trasladó la escritura a PersistenciaSolicitudesArchivo
y el reporte a ReporteSolicitudes.

Esto reduce los motivos de cambio del gestor. Todavía concentra
validaciones y reglas de asignación, que pueden separarse
en futuras iteraciones.

## OCP — Abierto a extensión y cerrado a modificación

La interfaz PersistenciaSolicitudes permite incorporar otras formas
de almacenamiento mediante nuevas implementaciones y suministrarlas
al constructor de GestorAcademico.

La lógica de asignación por tipo todavía utiliza condicionales
y posiciones de la lista de responsables. Incorporar nuevos tipos
puede requerir modificar esa lógica.

## LSP — Sustitución de implementaciones

GestorAcademico utiliza la operación guardar declarada
en PersistenciaSolicitudes.

Se verificó su sustitución por implementaciones de prueba que
registran llamadas o lanzan IOException. Esto permite comprobar
la delegación del guardado y el tratamiento de errores.

Estas pruebas verifican los casos utilizados, sin demostrar
por sí solas la equivalencia de cualquier implementación futura.

## ISP — Segregación de interfaces

PersistenciaSolicitudes declara únicamente guardar(Solicitud).

El gestor depende de una operación específica para su necesidad
actual. La interfaz puede implementarse sin incorporar operaciones
de consulta, eliminación o reporte.

## DIP — Inversión de dependencias

El gestor recibe PersistenciaSolicitudes mediante su constructor.
La operación registrarSolicitud depende de esa abstracción.

El constructor por defecto conserva la creación de
PersistenciaSolicitudesArchivo para facilitar el uso de Main.
Por ello, la selección de la implementación concreta todavía
aparece en la clase del gestor.

## Clean Code

### Eliminación de duplicación

Se extrajo buscarSolicitudPorId y se reutilizó desde
asignarResponsable y cambiarEstado.

### Agrupación de parámetros

DatosSolicitud agrupa los ocho datos de entrada del registro.
Main y las pruebas utilizan el nuevo objeto.

Su constructor todavía recibe ocho String, por lo que continúa
existiendo el riesgo de intercambiar argumentos.

### Cohesión y límites entre componentes

La escritura y la presentación del reporte se encuentran
en clases específicas, mientras el gestor coordina las operaciones.

### Validaciones y manejo de errores

Se incorporó el rechazo controlado de datos, tipos y estados nulos.

Ante una IOException de persistencia, el registro devuelve null.
La lista, los contadores y el próximo ID se actualizan después
de que guardar termina correctamente.

Continúa pendiente unificar los errores mediante un mecanismo
que permita distinguir claramente sus causas.

### Verificación de cambios

Los refactorings se realizaron de forma incremental y se ejecutaron
pruebas entre cambios.

Las correcciones funcionales cuentan con casos que fallaron antes
y aprobaron después. La suite final contiene 18 pruebas aprobadas.

## Deuda técnica pendiente

- Encapsular los atributos de las entidades y las colecciones.
- Incorporar tipos del dominio para estados, trámites y prioridades.
- Representar y validar fechas mediante un tipo temporal.
- Separar las reglas de asignación.
- Aislar del disco las pruebas que todavía utilizan archivos reales.
- Recuperar los datos y persistir sus modificaciones.
- Incorporar escritura atómica.