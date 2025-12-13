package ec.com.ecuamag.InventarioDigital.service.servicePlaca;

import ec.com.ecuamag.InventarioDigital.model.modelPlaca.Placa;
import ec.com.ecuamag.InventarioDigital.repository.repositoryPlaca.PlacaRepository;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

@Service
public class CsvImportPlacaService {

    private final PlacaRepository repository;

    public CsvImportPlacaService(PlacaRepository repository) {
        this.repository = repository;
    }

    public void importarDesdeCsv() {

        // 🚫 evita duplicar datos
        if (repository.count() > 0) {
            System.out.println("⚠️ Placas ya cargadas, no se importa CSV.");
            return;
        }

        try {
            InputStream is = getClass()
                    .getClassLoader()
                    .getResourceAsStream("data/DatosInventarioEcuamagPlacas.csv");

            if (is == null) {
                throw new RuntimeException("No se encontró placas.csv");
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String linea;
            boolean primera = true;

            while ((linea = br.readLine()) != null) {
                if (primera) {
                    primera = false;
                    continue;
                }

                String[] col = linea.split(",");

                Placa p = new Placa();
                p.setNumero(Integer.parseInt(col[0].trim()));
                p.setCliente(col[1].trim());
                p.setDescripcion(col[2].trim());
                p.setCantidad(Integer.parseInt(col[3].trim()));

                repository.save(p);
            }

            System.out.println("✅ CSV de placas importado correctamente");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
