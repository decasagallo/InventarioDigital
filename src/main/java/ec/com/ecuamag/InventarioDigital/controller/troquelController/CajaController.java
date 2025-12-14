package ec.com.ecuamag.InventarioDigital.controller.troquelController;

import ec.com.ecuamag.InventarioDigital.enums.Inventario;
import ec.com.ecuamag.InventarioDigital.model.modelTroquel.Caja;
import ec.com.ecuamag.InventarioDigital.service.serviceTroquel.CajaService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/cajas")
@CrossOrigin(origins = "*")
public class CajaController {

    private final CajaService cajaService;

    public CajaController(CajaService cajaService) {
        this.cajaService = cajaService;
    }

    @GetMapping("/filtrar")
    public List<Caja> filtrarCajas(
            @RequestParam(required = false) BigDecimal ancho,
            @RequestParam(required = false) BigDecimal largo,
            @RequestParam(required = false) BigDecimal alto,
            @RequestParam(required = false) Inventario inventario) {

        return cajaService.filtrarCajas(ancho, largo, alto, inventario);
    }
}
