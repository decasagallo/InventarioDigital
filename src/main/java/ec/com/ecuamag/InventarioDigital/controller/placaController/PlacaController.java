package ec.com.ecuamag.InventarioDigital.controller.placaController;

import ec.com.ecuamag.InventarioDigital.model.modelClise.ClienteClise;
import ec.com.ecuamag.InventarioDigital.model.modelPlaca.Placa;
import ec.com.ecuamag.InventarioDigital.service.servicePlaca.PlacaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/placas")
@CrossOrigin(origins = "*")
public class PlacaController {

    private final PlacaService placaService;

    public PlacaController(PlacaService placaService) {
        this.placaService = placaService;
    }

    // ✅ LISTAR TODAS
    @GetMapping
    public List<Placa> listarTodas() {
        return placaService.listarTodas();
    }

    // 🔍 BUSCAR
    @GetMapping("/buscar")
    public List<Placa> buscar(
            @RequestParam(required = false) String q
    ) {
        return placaService.buscar(q);
    }
}
