package utn.metodologia;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class PersistenciaSolicitudesArchivo implements PersistenciaSolicitudes {

    private final String rutaArchivo;

    public PersistenciaSolicitudesArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    @Override
    public void guardar(Solicitud solicitud) throws IOException {
        try (FileWriter fw = new FileWriter(rutaArchivo, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            out.println(
                    solicitud.id + ";" +
                            solicitud.estudiante.legajo + ";" +
                            solicitud.tipo + ";" +
                            solicitud.estado + ";" +
                            solicitud.responsable.nombre
            );
        }
    }
}