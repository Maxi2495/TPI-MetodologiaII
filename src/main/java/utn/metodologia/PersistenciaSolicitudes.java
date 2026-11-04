package utn.metodologia;

import java.io.IOException;

public interface PersistenciaSolicitudes {
    void guardar(Solicitud solicitud) throws IOException;
}