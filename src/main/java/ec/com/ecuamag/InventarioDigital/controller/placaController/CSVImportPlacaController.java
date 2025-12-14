package ec.com.ecuamag.InventarioDigital.controller.placaController;

import ec.com.ecuamag.InventarioDigital.service.servicePlaca.CsvImportPlacaService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CSVImportPlacaController {

    private final CsvImportPlacaService csvImportPlacaService;

    public CSVImportPlacaController(CsvImportPlacaService csvImportPlacaService) {
        this.csvImportPlacaService = csvImportPlacaService;
    }

    @PostMapping("/importar-csv-placas")
    public String importarCsvDesdeArchivo() {
        try {
            csvImportPlacaService.importarDesdeCsv();
            return "Datos de placas importados correctamente desde el archivo CSV.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al importar datos de placas: " + e.getMessage();
        }
    }
}
