package ec.com.ecuamag.InventarioDigital.controller;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import ec.com.ecuamag.InventarioDigital.enums.Inventario;
import ec.com.ecuamag.InventarioDigital.enums.TipoTroquel;
import ec.com.ecuamag.InventarioDigital.model.Bolsa;
import ec.com.ecuamag.InventarioDigital.model.Caja;
import ec.com.ecuamag.InventarioDigital.model.Troquel;
import ec.com.ecuamag.InventarioDigital.repository.BolsaRepository;
import ec.com.ecuamag.InventarioDigital.repository.CajaRepository;
import ec.com.ecuamag.InventarioDigital.repository.TroquelRepository;
import ec.com.ecuamag.InventarioDigital.service.TroquelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/troqueles")
@CrossOrigin(origins = "*") // Permite peticiones desde cualquier origen
public class TroquelController {

    private final TroquelService troquelService;

    public TroquelController(TroquelService troquelService) {
        this.troquelService = troquelService;
    }


    @Autowired
    private BolsaRepository bolsaRepository;

    @Autowired
    private CajaRepository cajaRepository;
    @Autowired
    private TroquelRepository troquelRepository;

    private static final String CSV_FILE_PATH = "/data/DatosInventarioEcuamag.csv";  // Ruta interna del recurso




    // Obtener todos los troqueles ordenados de menor a mayor por número
    @GetMapping
    public List<Troquel> obtenerTodosLosTroqueles() {
        return troquelService.getTodosLosTroqueles();
    }

    // Filtrar por inventario y devolver ordenado de menor a mayor por número
    @GetMapping("/filtrar/inventario")
    public List<Troquel> filtrarPorInventario(@RequestParam Inventario inventario) {
        return troquelService.getTroquelesPorInventario(inventario);
    }

    // Filtrar por inventario y tipo, devolver ordenado de menor a mayor por número
    @GetMapping("/filtrar/inventario-y-tipo")
    public List<Troquel> filtrarPorInventarioYTipo(@RequestParam Inventario inventario,
                                                   @RequestParam TipoTroquel tipo) {
        return troquelService.getTroquelesPorInventarioYTipo(inventario, tipo);
    }


    private Integer parseInteger(String value) {
        return (value == null || value.isEmpty()) ? null : Integer.parseInt(value.trim());
    }

    private BigDecimal parseBigDecimal(String value) {
        return (value == null || value.isEmpty()) ? null : new BigDecimal(value.trim());
    }

    @GetMapping("/buscar")
    public List<Troquel> buscarTroqueles(@RequestParam String descripcion) {
        return troquelService.buscarPorDescripcion(descripcion);
    }
}

