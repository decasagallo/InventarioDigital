package ec.com.ecuamag.InventarioDigital.service.serviceClise;

import ec.com.ecuamag.InventarioDigital.model.modelClise.ClienteClise;
import ec.com.ecuamag.InventarioDigital.repository.repositoryClise.ClienteCliseRepository;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

@Service
public class CsvImportClienteCliseService {

    private final ClienteCliseRepository repository;

    public CsvImportClienteCliseService(ClienteCliseRepository repository) {
        this.repository = repository;
    }

    public void importarDesdeCsv() {

        // ⚠️ evita duplicar si ya hay datos
        if (repository.count() > 0) {
            System.out.println("⚠️ ClienteClise ya tiene datos, no se importa CSV.");
            return;
        }

        try {
            InputStream is = getClass()
                    .getClassLoader()
                    .getResourceAsStream("data/DatosInventarioEcuamagClises.csv");

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String linea;
            boolean primera = true;

            while ((linea = br.readLine()) != null) {
                if (primera) {
                    primera = false;
                    continue;
                }

                String[] col = linea.split(",");

                ClienteClise c = new ClienteClise();
                c.setNombreCliente(col[0].trim());
                c.setLetra(col[1].trim());
                c.setNumero(Integer.parseInt(col[2].trim()));
                c.setImpresion(Integer.parseInt(col[3].trim()));
                c.setRepujado(Integer.parseInt(col[4].trim()));

                repository.save(c);
            }

            System.out.println("✅ CSV de clises importado correctamente");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
