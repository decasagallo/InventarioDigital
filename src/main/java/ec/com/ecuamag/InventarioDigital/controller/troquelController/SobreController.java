package ec.com.ecuamag.InventarioDigital.controller.troquelController;

import ec.com.ecuamag.InventarioDigital.enums.Inventario;
import ec.com.ecuamag.InventarioDigital.enums.Orientacion;
import ec.com.ecuamag.InventarioDigital.enums.TipoSobre;
import ec.com.ecuamag.InventarioDigital.enums.TipoSolapa;
import ec.com.ecuamag.InventarioDigital.model.modelTroquel.Sobre;
import ec.com.ecuamag.InventarioDigital.service.serviceTroquel.SobreService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/sobres")
@CrossOrigin(origins = "*")
public class SobreController {

    private final SobreService sobreService;

    public SobreController(SobreService sobreService) {
        this.sobreService = sobreService;
    }

    @GetMapping("/filtrar")
    public List<Sobre> filtrarSobres(
            @RequestParam(required = false) TipoSobre tipoSobre,
            @RequestParam(required = false) Orientacion orientacion,
            @RequestParam(required = false) TipoSolapa tipoSolapa,
            @RequestParam(required = false) BigDecimal ancho,
            @RequestParam(required = false) BigDecimal largo,
            @RequestParam(required = false) Inventario inventario) {

        return sobreService.filtrarSobres(tipoSobre, orientacion, tipoSolapa, ancho, largo, inventario);
    }

}
