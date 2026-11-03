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