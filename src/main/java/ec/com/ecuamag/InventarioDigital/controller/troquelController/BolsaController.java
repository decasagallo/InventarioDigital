package ec.com.ecuamag.InventarioDigital.controller.troquelController;
import ec.com.ecuamag.InventarioDigital.enums.Inventario;
import ec.com.ecuamag.InventarioDigital.model.modelTroquel.Bolsa;
import ec.com.ecuamag.InventarioDigital.service.serviceTroquel.BolsaService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/bolsas")
@CrossOrigin(origins = "*")
public class BolsaController {

    private final BolsaService bolsaService;

    public BolsaController(BolsaService bolsaService) {
        this.bolsaService = bolsaService;
    }

    @GetMapping("/filtrar")
    public List<Bolsa> filtrarBolsas(
            @RequestParam(required = false) BigDecimal ancho,
            @RequestParam(required = false) BigDecimal largo,
            @RequestParam(required = false) BigDecimal alto,
            @RequestParam(required = false) Inventario inventario) {

        return bolsaService.filtrarBolsas( ancho, largo, alto,  inventario);
    }
}
