package ec.com.ecuamag.InventarioDigital.controller.troquelController;
import ec.com.ecuamag.InventarioDigital.enums.Inventario;
import ec.com.ecuamag.InventarioDigital.enums.TipoTroquel;
import ec.com.ecuamag.InventarioDigital.model.modelTroquel.Troquel;
import ec.com.ecuamag.InventarioDigital.service.serviceTroquel.TroquelService;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/buscar")
    public List<Troquel> buscarTroqueles(@RequestParam String descripcion) {
        return troquelService.buscarPorDescripcion(descripcion);
    }

    @GetMapping("/filtrar")
    public List<Troquel> filtrarTroquelesGenerico(
            @RequestParam(required = false) Inventario inventario,
            @RequestParam(required = false) TipoTroquel tipo,
            @RequestParam(required = false) BigDecimal ancho,
            @RequestParam(required = false) BigDecimal largo) {
        return troquelService.filtrarTroquelesGenerico(inventario, tipo, ancho, largo);
    }


}

