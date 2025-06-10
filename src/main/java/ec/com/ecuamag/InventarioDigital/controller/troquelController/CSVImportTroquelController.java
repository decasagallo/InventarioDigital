package ec.com.ecuamag.InventarioDigital.controller.troquelController;
import ec.com.ecuamag.InventarioDigital.service.serviceTroquel.CsvImportTroquelService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CSVImportTroquelController {

    private final CsvImportTroquelService csvImportTroquelService;

    public CSVImportTroquelController(CsvImportTroquelService csvImportTroquelService) {
        this.csvImportTroquelService = csvImportTroquelService;
    }

    @PostMapping("/importar-csv")
    public String importarCsvDesdeArchivo() {
        try {
            csvImportTroquelService.importarDatosDesdeCsv();
            return "Datos importados correctamente desde el archivo CSV.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al importar datos: " + e.getMessage();
        }
    }
}
