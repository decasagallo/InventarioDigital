package ec.com.ecuamag.InventarioDigital.controller.cliseController;

import ec.com.ecuamag.InventarioDigital.service.serviceClise.CsvImportCliseService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CSVImportCliseController {

    private final CsvImportCliseService csvImportCliseService;

    public CSVImportCliseController(CsvImportCliseService csvImportCliseService) {
        this.csvImportCliseService = csvImportCliseService;
    }

    @PostMapping("/importar-csv-clises")
    public String importarCsvDesdeArchivo() {
        try {
            csvImportCliseService.importarDatosDesdeCsv();
            return "Datos de clises importados correctamente desde el archivo CSV.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al importar datos de clises: " + e.getMessage();
        }
    }
}
