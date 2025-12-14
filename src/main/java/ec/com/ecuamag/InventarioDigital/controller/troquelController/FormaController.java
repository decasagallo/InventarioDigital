package ec.com.ecuamag.InventarioDigital.controller.troquelController;

import ec.com.ecuamag.InventarioDigital.enums.Inventario;
import ec.com.ecuamag.InventarioDigital.enums.TipoForma;
import ec.com.ecuamag.InventarioDigital.model.modelTroquel.Forma;
import ec.com.ecuamag.InventarioDigital.service.serviceTroquel.FormaService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/formas")
@CrossOrigin(origins = "*")
public class FormaController {

    private final FormaService formaService;

    public FormaController(FormaService formaService) {
        this.formaService = formaService;
    }

    @GetMapping("/filtrar")
    public List<Forma> filtrarFormas(
            @RequestParam(required = false) TipoForma tipoForma,
            @RequestParam(required = false) BigDecimal ancho,
            @RequestParam(required = false) BigDecimal largo,
            @RequestParam(required = false) Inventario inventario) {

        return formaService.filtrarFormas(tipoForma, ancho, largo, inventario);
    }

}
