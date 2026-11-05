package utn.metodologia;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class PersistenciaSolicitudesArchivo implements PersistenciaSolicitudes {

    private final String rutaArchivo;

    public PersistenciaSolicitudesArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    @Override
    public void guardar(Solicitud solicitud) throws IOException {
        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(rutaArchivo, true))) {

            writer.write(
                    solicitud.id + ";" +
                            solicitud.estudiante.legajo + ";" +
                            solicitud.tipo + ";" +
                            solicitud.estado + ";" +
                            solicitud.responsable.nombre
            );
            writer.newLine();
        }
    }
}