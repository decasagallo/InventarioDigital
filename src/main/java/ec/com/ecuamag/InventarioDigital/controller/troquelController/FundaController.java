package ec.com.ecuamag.InventarioDigital.controller.troquelController;

import ec.com.ecuamag.InventarioDigital.enums.Inventario;
import ec.com.ecuamag.InventarioDigital.model.modelTroquel.Funda;
import ec.com.ecuamag.InventarioDigital.service.serviceTroquel.FundaService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/fundas")
@CrossOrigin(origins = "*")
public class FundaController {

    private final FundaService fundaService;

    public FundaController(FundaService fundaService) {
        this.fundaService = fundaService;
    }

    @GetMapping("/filtrar")
    public List<Funda> filtrarCarpetas(
            @RequestParam(required = false) BigDecimal ancho,
            @RequestParam(required = false) BigDecimal largo,
            @RequestParam(required = false) Inventario inventario) {

        return fundaService.filtrarFundas( ancho, largo, inventario);
    }
}