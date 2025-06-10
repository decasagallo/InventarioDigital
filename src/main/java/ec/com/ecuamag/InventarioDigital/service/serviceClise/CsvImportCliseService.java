package ec.com.ecuamag.InventarioDigital.service.serviceClise;

import ec.com.ecuamag.InventarioDigital.model.modelClise.Clise;
import ec.com.ecuamag.InventarioDigital.repository.repositoryClise.CliseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class CsvImportCliseService {

    @Autowired
    private CliseRepository cliseRepository;

    private static final Logger LOGGER = Logger.getLogger(CsvImportCliseService.class.getName());
    private static final String CSV_PATH = "data/DatosInventarioEcuamagClises.csv"; // debe estar en src/main/resources

    public void importarDatosDesdeCsv() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(CSV_PATH)) {
            if (inputStream == null) {
                throw new IOException("No se encontró el archivo CSV en los recursos.");
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String linea;
            boolean primeraLinea = true;

            while ((linea = br.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }

                String[] registro = linea.split(",");

                if (registro.length < 4) {
                    LOGGER.log(Level.WARNING, "Fila ignorada por campos insuficientes: {0}", linea);
                    continue;
                }

                try {
                    Clise clise = new Clise();
                    clise.setLetra(get(registro, 0));
                    clise.setNumero(Integer.parseInt(get(registro, 1)));
                    clise.setNombre(get(registro, 2));
                    clise.setDescripcion(get(registro, 3));

                    cliseRepository.save(clise);
                    LOGGER.log(Level.INFO, "Clise guardado: {0}", clise.getNombre());

                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Error al procesar fila: " + linea, e);
                }
            }

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error al leer el archivo CSV", e);
        }
    }

    private String get(String[] registro, int index) {
        return (registro.length > index && registro[index] != null && !registro[index].trim().isBlank())
                ? registro[index].trim()
                : null;
    }
}
